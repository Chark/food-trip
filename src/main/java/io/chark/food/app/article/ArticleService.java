package io.chark.food.app.article;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.category.ArticleCategoryRepository;
import io.chark.food.domain.article.ArticleRepository;
import io.chark.food.domain.article.photo.ArticlePhoto;
import io.chark.food.domain.article.photo.ArticlePhotoRepository;
import io.chark.food.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository categoryRepository;
    private final ArticlePhotoRepository photoRepository;
    private final AuditService auditService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository,
                          ArticleCategoryRepository categoryRepository,
                          ArticlePhotoRepository photoRepository,
                          AuditService auditService) {

        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.photoRepository = photoRepository;
        this.auditService = auditService;
    }

    /**
     * Register a new Article.
     *
     * @param title             title of the article.
     * @param description       description of the article.
     * @param shortDescription  short description of the article.
     * @param metaKeywords      meta keywords of the article.
     * @param metaDescription   meta description of the article.
     * @return  created article or empty optional.
     */
    public Optional<Article> register(String title,
                                      String description,
                                      String shortDescription,
                                      String metaKeywords,
                                      String metaDescription) {

        Article article = new Article(
                title,
                description,
                shortDescription,
                metaKeywords,
                metaDescription);

        try {
            article = articleRepository.save(article);
            LOGGER.debug("Created new user Article{title='{}'}", title);

            auditService.info("Created a new Article using title: %s", title);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new user Article{title='{}'}", title, e);

            auditService.error("Failed to create a new Article using title: %s", title);
            return Optional.empty();
        }
        return Optional.of(article);
    }

    /**
     * Add specified article category to specified article.
     *
     * @param article   article.
     * @param category  article category.
     * @return          article optional with added category.
     */
    public Optional<Article> addCategory(Article article, ArticleCategory category) {
        article.addCategory(category);

        try {
            LOGGER.debug("Adding ArticleCategory{id={}} to Article{id={}}", category.getId(), article.getId());
            return Optional.ofNullable(articleRepository.save(article));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not add article category to article", e);

            auditService.error("Failed to add article category to article");
            return Optional.empty();
        }
    }

    /**
     * Add specified article photo to specified article.
     *
     * @param article   article.
     * @param photo  article photo.
     * @return          article optional with added photo.
     */
    public Optional<Article> addPhoto(Article article, ArticlePhoto photo) {
        article.addPhoto(photo);

        try {
            LOGGER.debug("Adding ArticlePhoto{id={}} to Article{id={}}", photo.getId(), article.getId());
            return Optional.ofNullable(articleRepository.save(article));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not add article photo to article", e);

            auditService.error("Failed to add article photo to article");
            return Optional.empty();
        }
    }

    /**
     * Get an article by id.
     *
     * @return article category.
     * @throws NotFoundException if the article category is not found.
     */
    public Article getArticle(long id) {
        Article article = articleRepository.findOne(id);
        if (article == null) {
            auditService.warn("Attempted to query non-existing Article with id: %d", id);
            throw new NotFoundException(Article.class, id);
        }
        return article;
    }
}
