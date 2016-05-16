package io.chark.food;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WebAppConfiguration
@ActiveProfiles("test")
@Retention(RetentionPolicy.RUNTIME)
@SpringApplicationConfiguration(classes = FoodTripApplication.class)
public @interface FoodTripIntegrationTest {
}