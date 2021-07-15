package com.glarimy.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableKafka
public class QueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}
	
    @KafkaListener(topics = "tweet", groupId = "glarimy")
	public void onMessage(String tweet) {
		System.out.println("Recevied " + tweet);
	}

}
