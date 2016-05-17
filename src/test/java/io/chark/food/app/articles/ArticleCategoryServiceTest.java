package io.chark.food.app.articles;

import io.chark.food.FoodTripIntegrationTest;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.article.category.ArticleCategoryService;
import io.chark.food.domain.article.ArticleCategoryRepository;
import io.chark.food.domain.article.ArticleRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@FoodTripIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ArticleCategoryServiceTest {

    @Resource
    private ArticleCategoryRepository categoryRepository;

    @Resource
    private ArticleRepository articleRepository;

    private ArticleCategoryService service;

    @Before
    public void setUp() {
        service = new ArticleCategoryService(
                categoryRepository,
                Mockito.mock(AuditService.class));
    }

    @After
    public void tearDown() {
        articleRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void someTest() {
        // todo include tests
    }
}
