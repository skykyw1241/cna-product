package com.example.product;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.kafka.dsl.Kafka;

@SpringBootApplication
//@EnableBinding(Processor.class)
@EnableBinding(KafkaProcessor.class)
public class ProductApplication {
	public static ApplicationContext applicationContext;
	public static void main(String[] args) {
		applicationContext = SpringApplication.run(ProductApplication.class, args);
	}

}
