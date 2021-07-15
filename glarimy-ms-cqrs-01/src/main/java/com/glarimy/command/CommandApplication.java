package com.glarimy.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@RestController
@EnableSwagger2
public class CommandApplication {
	@Autowired
	private KafkaTemplate<String, String> kafka;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	@PostMapping("/tweet")
	public String post(@RequestBody Tweet tweet) {
		kafka.send("tweet", tweet.getMessage());
		return tweet.getMessage();
	}

}
