package io.chark.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FoodTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodTripApplication.class, args);
	}
}