package com.sapient.cart.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Cart {
	
	private String cartId;
	
	private String customerId;
	
	private List<String> products;

	
	public Cart() {
		// TODO Auto-generated constructor stub
	}
	
	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}
	
}
