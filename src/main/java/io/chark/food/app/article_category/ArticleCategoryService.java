package io.chark.food.app.article_category;

import io.chark.food.app.account.AccountService;
import io.chark.food.domain.article_category.ArticleCategory;
import io.chark.food.domain.article_category.ArticleCategoryRepository;
import org.hibernate.mapping.List;
import org.hibernate.mapping.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ArticleCategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final ArticleCategoryRepository categoryRepository;

    @Autowired
    public ArticleCategoryService(ArticleCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Initialize roles used by the system.
     */
    @PostConstruct
    public void init() {
        addCategory("Lorem Ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
    }

    private List<ArticleCategory> getCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Create a new article category in the system.
     *
     * @param title       title of the article category.
     * @param description description of the article category.
     */
    private void addCategory(String title, String description) {
        LOGGER.info("Creating new ArticleCategory{title='{}'}", title);

        categoryRepository.save(new ArticleCategory(title, description));
    }
}
