package com.sapient.cart.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.DeleteResult;
import com.sapient.cart.model.Cart;

@Repository
public class CartDAO {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Cart getbyCustomerId(String customerId){
		Query query = new Query();
		query.addCriteria(Criteria.where("customerId").is(customerId));
		return mongoTemplate.findOne(query, Cart.class);
	}
	
	public void update(Cart cart) {
		Query query = new Query();
		query.addCriteria(Criteria.where("cartId").is(cart.getCartId()));
		Update update = new Update();
		update.set("products", cart.getProducts());
		mongoTemplate.updateFirst(query, update, Cart.class);
	}
	
	public void save(Cart cart) {
		mongoTemplate.save(cart);
	}
	
	public boolean removebyCustomerId(String customerId){
		Query query = new Query();
		query.addCriteria(Criteria.where("customerId").is(customerId));
		DeleteResult remove = mongoTemplate.remove(query, Cart.class);
		
		return remove.getDeletedCount() > 0 ? true : false;
	}
	  
}
