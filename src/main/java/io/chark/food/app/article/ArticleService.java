package io.chark.food.app.article;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.category.ArticleCategoryRepository;
import io.chark.food.domain.article.ArticleRepository;
import io.chark.food.domain.article.photo.ArticlePhoto;
import io.chark.food.domain.article.photo.ArticlePhotoRepository;
import io.chark.food.util.exception.NotFoundException;
import io.chark.food.util.photo.PhotoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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
     * Initialize articles used by the system.
     */
//    @PostConstruct todo disabled for now
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

        // Adds photo of the article
        byte[] image = PhotoUtils.getImageBytes("static/images/default_avatar.JPG");
        ArticlePhoto photo = new ArticlePhoto(image, "Edvinas has the best dog.", "Good looking cool dog");
        article.addPhoto(photoRepository.save(photo));
        photo = new ArticlePhoto(image, "Edvinas has the best dog.", "Good looking cool dog");
        article.addPhoto(photoRepository.save(photo));

        //ArticlePhoto photo
        articleRepository.save(article);
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
