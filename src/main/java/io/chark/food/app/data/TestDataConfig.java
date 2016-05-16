package io.chark.food.app.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * Configuration class to initialize test data during startup, only done on dev profile.
 */
@Configuration
@Profile("dev")
public class TestDataConfig {

    @Autowired
    private TestDataService testDataService;

    @PostConstruct
    public void init() {
        testDataService.initTestData();
    }
}