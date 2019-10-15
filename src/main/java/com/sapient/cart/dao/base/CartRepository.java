package com.sapient.cart.dao.base;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sapient.cart.model.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart,String>  {

}
