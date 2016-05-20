package io.chark.food.app.article;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.ArticleRepository;
import io.chark.food.domain.article.photo.ArticlePhoto;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final AuditService auditService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository,
                          AuditService auditService) {

        this.articleRepository = articleRepository;
        this.auditService = auditService;
    }

    /**
     * Register a new Article.
     *
     * @param title            title of the article.
     * @param description      description of the article.
     * @param shortDescription short description of the article.
     * @param metaKeywords     meta keywords of the article.
     * @param metaDescription  meta description of the article.
     * @return created article or empty optional.
     */
    public Optional<Article> register(Restaurant restaurant,
                                      String title,
                                      String description,
                                      String shortDescription,
                                      String metaKeywords,
                                      String metaDescription) {

        Article article = new Article(
                restaurant,
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
     * Update specified article details.
     *
     * @param article        article which is to be updated.
     * @param articleDetails details which are used in updating.
     * @return updated article optional.
     */
    public Optional<Article> update(Article article, Article articleDetails) {

        // Update other stuff.
        article.setTitle(articleDetails.getTitle());
        article.setDescription(articleDetails.getDescription());
        article.setShortDescription(articleDetails.getShortDescription());
        article.setMetaKeywords(articleDetails.getMetaKeywords());
        article.setMetaDescription(articleDetails.getMetaDescription());

        try {
            LOGGER.debug("Updating Article{id={}} details", article.getId());
            return Optional.ofNullable(articleRepository.save(article));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update article", e);

            auditService.error("Failed to update article details");
            return Optional.empty();
        }
    }

    /**
     * Add specified article category to specified article.
     *
     * @param article  article.
     * @param category article category.
     * @return article optional with added category.
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
     * Set specified article categories to specified article.
     *
     * @param article    article.
     * @param categories list of article categories
     * @return article optional with added categories.
     */
    public Optional<Article> setCategories(Article article, List<ArticleCategory> categories) {
        article.setCategories(categories);

        try {
            LOGGER.debug("Setting ArticleCategories{size={}} to Article{id={}}",
                    categories.size(),
                    article.getId());

            return Optional.ofNullable(articleRepository.save(article));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not add article categories to article", e);

            auditService.error("Failed to add article categories to article");
            return Optional.empty();
        }
    }

    /**
     * Add specified article photo to specified article.
     *
     * @param article article.
     * @param photo   article photo.
     * @return article optional with added photo.
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

    /**
     * Get all articles.
     *
     * @return list of articles.
     */
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }
}
