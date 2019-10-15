package com.sapient.cart.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sapient.cart.dao.CartDAO;
import com.sapient.cart.dao.base.CartRepository;
import com.sapient.cart.model.Cart;
import com.sapient.cart.service.kafka.KafkaService;
import com.sapient.ecomm_commons.domain.Product;

@Service
public class CartService {

	@Autowired
	private CartDAO cartDAO;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private KafkaService kafkaService;

	@Autowired
	private RestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(CartService.class);

	public Boolean addToCart(String productId, String customerId) throws Exception {
		try {
			Cart cart = cartDAO.getbyCustomerId(customerId);
			if (cart == null) {
				cart = new Cart();
				cart.setCartId(ObjectId.get().toHexString());
				cart.setCustomerId(customerId);
				cart.setProducts(new ArrayList<String>());
				cart.getProducts().add(productId);
				cartRepository.insert(cart);
			} else {
				cart.getProducts().add(productId);
				cartRepository.save(cart);
			}

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
			
			
			return kafkaService.send( customerId, cart);
		} catch (Exception e) {
			logger.error("error writing into the destination mango/kafka");
			throw new Exception(e);
		}

	}

	public Boolean remove(String productId, String customerId) throws Exception {

		try {

			Cart cart = cartDAO.getbyCustomerId(customerId);

			if (cart == null) {
				logger.error("No items are there to remove from cart");
			} else {
				cart.getProducts().removeIf(e -> e.equals(productId));
			}

			return kafkaService.remove(customerId, cart);

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
		} catch (Exception e) {
			logger.error("error removing from kafka");
			throw new Exception(e);
		}

	}

	public Product getItemById(String itemId) {

		return restTemplate.getForObject("http://10.150.223.229:8091/catalogue/itemById/" + itemId, Product.class);

	}

	public Product getItemByName(String itemName) {

		return restTemplate.getForObject("http://10.150.223.229:8091/catalogue/itemByName/" + itemName, Product.class);

	}

	public List<String> getAllCartItems(String customerId) {
		try {
			Cart getbyCustomerId = cartDAO.getbyCustomerId(customerId);
			if (getbyCustomerId == null) {
				logger.error("No Items in the cart");
			} else {
				return getbyCustomerId.getProducts().stream().collect(Collectors.toList());
			}

		} catch (Exception e) {
			logger.error("Error while fetching items", e);

		}
		return Collections.emptyList();
	}

}
