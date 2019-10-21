package com.sapient.cart.events;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.sapient.cart.dao.CartDAO;

@EnableBinding(PaymentSink.class)
public class PaymentEventSubscriber {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CartDAO cartDao;

	@StreamListener(PaymentSink.INPUT)
	public void receive(String customerId) {

		if (StringUtils.isNotBlank(customerId)) {

			logger.info("Request received to reset Customer Cart: {}",
					customerId);
			boolean status = cartDao.removebyCustomerId(customerId);
			logger.info("Customer cart reset sattus: {} for {}", status,
					customerId);
		}
	}

}
