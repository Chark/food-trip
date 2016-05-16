package io.chark.food.app.administrate.article;

import io.chark.food.domain.article.ArticleCategory;
import io.chark.food.domain.article.ArticleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleCategoryAdministrationService {

    private final ArticleCategoryRepository categoryRepository;

    @Autowired
    public ArticleCategoryAdministrationService(ArticleCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<ArticleCategory> getCategory(long id) {
        return Optional.of(categoryRepository.findOne(id));
    }

    List<ArticleCategory> getCategories() {
        return categoryRepository.findAll();
    }
}
