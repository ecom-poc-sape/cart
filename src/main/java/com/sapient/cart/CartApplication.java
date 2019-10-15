package com.sapient.cart;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.client.RestTemplate;

import com.sapient.cart.model.DTO.CartItems;

@SpringBootApplication
public class CartApplication {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	
	  @Value("${kafka.topic.name}") private String topicName;
	 

	public static void main(String[] args) {
		SpringApplication.run(CartApplication.class, args);
	}

	@Bean
	public RestTemplate getTemplate() {
		return new RestTemplate();
	}

	/*
	 * @Bean public MongoTemplate mongoTemplate() throws IOException {
	 * EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
	 * mongo.setBindIp(MONGO_DB_URL); MongoClient mongoClient = mongo.getObject();
	 * MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
	 * return mongoTemplate; }
	 */

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, CartItems> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, CartItems> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, CartItems> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, CartItems> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, CartItems> consumerFactory() {
		
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "cart-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.sapient.cart.model.DTO");
		return props;
	}

	@Bean
	public Consumer<String, CartItems> getConsumer() {
		Consumer<String,CartItems> createConsumer = consumerFactory().createConsumer();
		createConsumer.subscribe(Collections.singleton(topicName));
		return createConsumer;

	}

}
