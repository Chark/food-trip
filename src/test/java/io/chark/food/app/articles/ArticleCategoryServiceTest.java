package io.chark.food.app.articles;

import io.chark.food.FoodTripIntegrationTest;
import io.chark.food.app.article.ArticleCategoryService;
import io.chark.food.domain.article.ArticleCategoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@FoodTripIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ArticleCategoryServiceTest {

    @Resource
    ArticleCategoryRepository categoryRepository;

    ArticleCategoryService service;

    @Before
    public void setUp() {
        service = new ArticleCategoryService(categoryRepository);
    }

    @After
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    public void someTest() {
        // todo include tests
    }
}
