package com.sapient.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.cart.service.CartService;
import com.sapient.ecomm_commons.domain.Product;

@RestController()
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("/add/{customerId}/{productId}")
	public Boolean addToCart(@PathVariable String customerId,@PathVariable String productId) {
		
		try {
			return cartService.addToCart(productId,customerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	@DeleteMapping("/remove/{customerId}/{productId}")
	public Boolean removeFromCart(@PathVariable String customerId,@PathVariable String productId) {
		try {
			return cartService.remove(productId,customerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@GetMapping("/viewDetailById/{productId}")
	public Product viewDetailById( @PathVariable String productId) {
		return cartService.getItemById(productId);
	}
	
	@GetMapping("/viewDetailByName/{itemName}")
	public Product viewDetailByName(@PathVariable String itemName) {
		return cartService.getItemByName(itemName);
	}
	
	@GetMapping("/getAllCartItems/{customerId}")
	public List<String> getAllCartItems(@PathVariable String customerId) {
		return cartService.getAllCartItems(customerId);
	}
}
