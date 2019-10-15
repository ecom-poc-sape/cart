package com.sapient.cart.model.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sapient.cart.model.Product;

public class CartItems implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String custemerId;

	private List<Product> items = new ArrayList<Product>();

	public CartItems() {
		// TODO Auto-generated constructor stub
	}

	public String getCustemerId() {
		return custemerId;
	}

	public void setCustemerId(String custemerId) {
		this.custemerId = custemerId;
	}

	public List<Product> getItems() {
		return items;
	}

	public void setItems(List<Product> items) {
		this.items = items;
	}

}
