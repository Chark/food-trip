package io.chark.food.app.article;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.comment.CommentService;
import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.ArticleRepository;
import io.chark.food.domain.article.photo.ArticlePhoto;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.util.exception.NotFoundException;
import io.chark.food.util.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;
    private final RestaurantService restaurantService;
    private final CommentService commentService;
    private final AccountService accountService;
    private final AuditService auditService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository,
                          RestaurantService restaurantService,
                          AuditService auditService,
                          CommentService commentService,
                          AccountService accountService) {

        this.articleRepository = articleRepository;
        this.restaurantService = restaurantService;
        this.auditService = auditService;
        this.commentService = commentService;
        this.accountService = accountService;
    }

    /**
     * Register a new Article.
     *
     * @param article details to save.
     * @return created article or empty optional.
     */
    public Optional<Article> register(Article article) {
        return register(article, restaurantService.getRestaurant());
    }

    /**
     * Register a new Article and assign it to a restaurant.
     *
     * @param article details to save.
     * @return created article or empty optional.
     */
    public Optional<Article> register(Article article, Restaurant restaurant) {
        Assert.notNull(article);
        article.setRestaurant(restaurant);

        String title = article.getTitle();
        try {
            Account account = accountService.loadUserByUsername("admin");
            for (int j = 0; j < 5; j++) {

                article.addComment(commentService.register(account, "Text text text" + j, false).get());

            }


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

    public void addComment(Article a, Comment c) {
        Article article = articleRepository.findOne(a.getId());
        article.addComment(c);
        articleRepository.save(a);

    }

    /**
     * Update specified article details.
     *
     * @param articleDetails details which are used in updating.
     * @return updated article optional.
     */
    public Optional<Article> update(long id, Article articleDetails) {

        // You can only edit article if you own it.
        Restaurant restaurant = restaurantService.getRestaurant();
        long restaurantId = restaurant.getId();

        // Update other stuff.
        Article article = articleRepository.findByIdAndRestaurantId(id, restaurantId);

        if (article == null) {
            throw new UnauthorizedException("Rejected");
        }

        article.setTitle(articleDetails.getTitle());
        article.setDescription(articleDetails.getDescription());
        article.setShortDescription(articleDetails.getShortDescription());
        article.setMetaKeywords(articleDetails.getMetaKeywords());
        article.setMetaDescription(articleDetails.getMetaDescription());
        article.setCategories(articleDetails.getCategories());

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
