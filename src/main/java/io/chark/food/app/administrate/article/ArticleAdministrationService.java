package io.chark.food.app.administrate.article;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.article.ArticleService;
import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.ArticleRepository;
import io.chark.food.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleAdministrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleAdministrationService.class);

    private final ArticleRepository articleRepository;
    private final RestaurantService restaurantService;
    private final ArticleService articleService;
    private final AuditService auditService;

    @Autowired
    public ArticleAdministrationService(ArticleRepository articleRepository,
                                        RestaurantService restaurantService,
                                        ArticleService articleService,
                                        AuditService auditService) {

        this.articleRepository = articleRepository;
        this.restaurantService = restaurantService;
        this.articleService = articleService;
        this.auditService = auditService;
    }

    /**
     * Create a new article or update an existing one based on id.
     *
     * @param id             article id.
     * @param articleDetails details used in creation or updating.
     * @return article optional.
     */
    public Optional<Article> saveArticle(long id,
                                         Article articleDetails) {

        Optional<Article> optional = Optional.of(articleRepository.findOne(id));

        // No account found, error.
        if (!optional.isPresent()) {
            return Optional.empty();
        }

        // Update account details.
        optional = articleService.update(optional.get(), articleDetails);

        // Update other details editable only by admins.
        Article article = optional.get();
        article.setTitle(articleDetails.getTitle());
        article.setDescription(articleDetails.getDescription());
        article.setShortDescription(articleDetails.getShortDescription());
        article.setMetaKeywords(articleDetails.getMetaKeywords());
        article.setMetaDescription(articleDetails.getMetaDescription());
        article.setCategories(articleDetails.getCategories());

        try {
            article = articleRepository.save(article);
            LOGGER.debug("Saved Article{id={}}", article.getId());

            auditService.debug("Updated Article+ with id: %d via admin panel", article.getId());

            return Optional.of(article);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save article", e);

            auditService.error("Failed to save Article");
            return Optional.empty();
        }
    }

    /**
     * Get an article by id.
     *
     * @return article.
     * @throws NotFoundException if the article is not found.
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
    List<Article> getArticles() {
        return articleRepository.findAll();
    }

    /**
     * Delete specified article.
     *
     * @param id user account id.
     */
    public void delete(long id) {
        Article article = articleRepository.findOne(id);
        articleRepository.delete(article);
        auditService.info("Deleted Article with id: %d", id);
    }
}
