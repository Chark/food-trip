package io.chark.food.app.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service to seed database with test data.
 */
@Service
public class TestDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataService.class);

    /**
     * Fill database with test data.
     */
    @Async
    public void initTestData() {
        LOGGER.info("Initializing test data");
        // todo add test data initialization.
        LOGGER.info("Finished initializing test data");
    }
}