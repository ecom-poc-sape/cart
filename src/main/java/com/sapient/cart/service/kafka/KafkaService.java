package com.sapient.cart.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.sapient.cart.model.Cart;

@Service
public class KafkaService {

	@Autowired
	private KafkaTemplate<String, Cart> kafkaTemplate;

	@Value("${kafka.topic.name}")
	private String topicName;

	private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

	public Boolean send(String customerId, Cart cart) throws Exception {
		logger.info(String.format("$$ -> Producing message --> %s", cart));

		// simply poll to move pointer to next
		/*
		 * ConsumerRecords<String, Cart> poll = consumer.poll(100);
		 * consumer.commitAsync();
		 */

		/*
		 * 
		 * CartItems cartItems = null; if (poll.partitions().isEmpty()) { cartItems =
		 * new CartItems(); cartItems.setCustemerId(customerId);
		 * cartItems.getItems().add(product); } else { Iterator<ConsumerRecord<String,
		 * CartItems>> iterator = poll.iterator(); while (iterator.hasNext()) {
		 * ConsumerRecord<String, CartItems> next = iterator.next(); cartItems =
		 * next.value(); List<Product> items = cartItems.getItems(); items.add(product);
		 * consumer.commitAsync(); } }
		 */
		kafkaTemplate.send(topicName, customerId, cart);
		return true;

	}

	public Boolean remove(String customerId, Cart cart) throws Exception {
		try {
			// just poll to move offset
			/*
			 * ConsumerRecords<String, Cart> poll = consumer.poll(100);
			 * consumer.commitAsync();
			 */
			/*
			 * CartItems cartItem = null;
			 * 
			 * if (poll.partitions().isEmpty()) {
			 * logger.error("No items are there to remove from cart"); } else {
			 * Iterator<ConsumerRecord<String, CartItems>> iterator = poll.iterator(); while
			 * (iterator.hasNext()) { cartItem = iterator.next().value(); List<Product>
			 * items = cartItem.getItems(); items.removeIf(e ->
			 * e.getId().equals(String.valueOf(customerId))); consumer.commitAsync(); } }
			 */
			kafkaTemplate.send(topicName, customerId, cart);

			return true;

		} catch (Exception e) {
			logger.error("error pushing to kafka", e);
			throw new Exception(e);
		}
	}

}
