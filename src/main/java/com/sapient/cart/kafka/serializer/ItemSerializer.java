package com.sapient.cart.kafka.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.ecomm_commons.domain.Product;

public class ItemSerializer implements Serializer<Product> {
	
	
	@Override public void configure(Map<String, ?> map, boolean b) {

	  }

	  @Override public byte[] serialize(String arg0, Product arg1) {
	    byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(arg1).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
	  }

	  @Override public void close() {

	  }


}
