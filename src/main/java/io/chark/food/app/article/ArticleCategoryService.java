package io.chark.food.app.article;

import io.chark.food.app.account.AccountService;
import io.chark.food.domain.article.ArticleCategory;
import io.chark.food.domain.article.ArticleCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ArticleCategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final ArticleCategoryRepository categoryRepository;

    @Autowired
    public ArticleCategoryService(ArticleCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Initialize article categories used by the system.
     */
    @PostConstruct
    public void init() {
        addCategory("Lorem Ipsum", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        addCategory("Sed ut perspiciatis", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.");
    }

    public List<ArticleCategory> getCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Create a new article category in the system.
     *
     * @param title       title of the article category.
     * @param description description of the article category.
     */
    private void addCategory(String title, String description) {
        LOGGER.debug("Creating new ArticleCategory{title='{}'}", title);
        categoryRepository.save(new ArticleCategory(title, description));
    }
}
