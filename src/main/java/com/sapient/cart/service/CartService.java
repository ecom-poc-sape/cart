package com.sapient.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sapient.cart.util.kafka.KafkaUtil;
import com.sapient.ecomm_commons.domain.Product;

@Service
public class CartService {

	@Autowired
	private KafkaUtil kafkaUtil;

	@Autowired
	private RestTemplate restTemplate;

	public Boolean addToCart(Product item, String cutomerId) {
		return kafkaUtil.sendMessage(item, cutomerId);

	}

	public Boolean remove(int productId, String cutomerId) {

		return kafkaUtil.remove(productId, cutomerId);

	}

	public Product getItemById(Integer itemId) {

		return restTemplate.getForObject("http://10.150.223.229:8091/catalogue/itemById/" + itemId, Product.class);

	}

	public Product getItemByName(String itemName) {

		return restTemplate.getForObject("http://10.150.223.229:8091/catalogue/itemByName/" + itemName, Product.class);

	}

	public List<String> getAllCartItems(String customerId) {
		return kafkaUtil.getAllCartItems(customerId);
	}
	
	

}
