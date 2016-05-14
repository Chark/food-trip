package io.chark.food.app.article;

import io.chark.food.app.account.AccountService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.ArticleCategory;
import io.chark.food.domain.article.ArticleCategoryRepository;
import io.chark.food.domain.article.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ArticleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository categoryRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, ArticleCategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Initialize articles used by the system.
     */
    @PostConstruct
    public void init() {
        addArticle("Pirmas straipsnis", "Pilnas straipsnio aprašymas.", "Trumpas straipsnio aprašymas", null, null);
        addArticle("Antras straipsnis", "Pilnas straipsnio aprašymas.", "Trumpas straipsnio aprašymas", null, null);
        addArticle("Trečias straispnis", "Pilnas straipsnio aprašymas.", "Trumpas straipsnio aprašymas", null, null);
    }

    private void addArticle(String title, String description, String shortDescription, String metaKeywords, String metaDescription) {
        LOGGER.debug("Creating new Article{title='{}'}", title);
        Article article = new Article(title, description, shortDescription, metaKeywords, metaDescription);

        // Adds article category
        long num = articleRepository.count() % categoryRepository.count();
        ArticleCategory category = categoryRepository.findOne(num + 1);
        article.addCategory(category);

        articleRepository.save(article);
    }
}
