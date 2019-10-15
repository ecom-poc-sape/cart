package com.sapient.cart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.cart.model.Product;
import com.sapient.cart.model.DTO.CartItems;
import com.sapient.cart.service.CartService;

@RestController()
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("/add/{cutomerId}")
	public Boolean addToCart(@PathVariable String cutomerId,@RequestBody Product item) {
		
		return cartService.addToCart(item,cutomerId);
		
	}
	
	@DeleteMapping("/remove/{itemId}/{cutomerId}")
	public Boolean removeFromCart(@PathVariable Integer itemId,@PathVariable String cutomerId) {
		return cartService.remove(itemId,cutomerId);
		
	}
	
	@GetMapping("/viewDetailById/{itemId}")
	public Product viewDetailById( @PathVariable Integer itemId) {
		return cartService.getItemById(itemId);
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
