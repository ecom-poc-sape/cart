package com.sapient.cart.kafka.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.ecomm_commons.domain.Product;

public class ItemDerializer implements Deserializer<Product>

{
	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {

	}

	@Override
	public Product deserialize(String arg0, byte[] arg1) {
		ObjectMapper mapper = new ObjectMapper();
		Product user = null;
		try {
			user = mapper.readValue(arg1, Product.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return user;
	}
}
