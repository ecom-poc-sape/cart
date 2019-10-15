package com.sapient.cart.util.kafka;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sapient.cart.model.Product;
import com.sapient.cart.model.DTO.CartItems;

@Service
public class KafkaUtil {

	@Autowired
	private KafkaTemplate<String, CartItems> kafkaTemplate;

	@Value("${kafka.topic.name}")
	private String topicName;

	@Autowired
	private Consumer<String, CartItems> consumer;

	private static final Logger logger = LoggerFactory.getLogger(KafkaUtil.class);

	public Boolean sendMessage(Product message, String cutomerId) {
		try {
			logger.info(String.format("$$ -> Producing message --> %s", message));
			ConsumerRecords<String, CartItems> poll = consumer.poll(100);
			CartItems cartItems = null;
			if (poll.partitions().isEmpty()) {
				cartItems = new CartItems();
				cartItems.setCustemerId(cutomerId);
				cartItems.getItems().add(message);
			} else {
				Iterator<ConsumerRecord<String, CartItems>> iterator = poll.iterator();
				while (iterator.hasNext()) {
					ConsumerRecord<String, CartItems> next = iterator.next();
					cartItems = next.value();
					List<Product> items = cartItems.getItems();
					items.add(message);
					consumer.commitAsync();
				}
			}
			kafkaTemplate.send(topicName, cutomerId, cartItems);
			return true;
		} catch (Exception e) {
			logger.error("error in writting to kafka", e);
		}
		return false;

	}

	public Boolean remove(int productId, String customerId) {
		try {

			ConsumerRecords<String, CartItems> poll = consumer.poll(100);
			CartItems cartItem = null;

			if (poll.partitions().isEmpty()) {
				logger.error("No items are there to remove from cart");
			} else {
				Iterator<ConsumerRecord<String, CartItems>> iterator = poll.iterator();
				while (iterator.hasNext()) {
					cartItem = iterator.next().value();
					List<Product> items = cartItem.getItems();
					items.removeIf(e -> e.getId().equals(String.valueOf(customerId)));
					consumer.commitAsync();
				}
			}
			kafkaTemplate.send(topicName, customerId, cartItem);

			return true;

		} catch (Exception e) {
			logger.error("error removing from kafka", e);
		}
		return false;
	}

	public List<String> getAllCartItems(String customerId) {

		ConsumerRecords<String, CartItems> poll = consumer.poll(100);
		CartItems cartItem = null;

		if (poll.partitions().isEmpty()) {
			logger.error("No items are there to in cart");
		} else {
			Iterator<ConsumerRecord<String, CartItems>> iterator = poll.iterator();
			while (iterator.hasNext()) {
				cartItem = iterator.next().value();
				consumer.commitAsync();
			}
			kafkaTemplate.send(topicName, customerId, cartItem);
		}

		return cartItem.getItems().stream().map(Product::getId).collect(Collectors.toList());

	}
}
