package io.chark.food.app.article.photo;

import io.chark.food.app.administrate.audit.AuditService;
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
public class ArticlePhotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticlePhotoService.class);

    private final ArticlePhotoRepository photoRepository;
    private final AuditService auditService;

    @Autowired
    public ArticlePhotoService(ArticlePhotoRepository photoRepository,
                               AuditService auditService) {

        this.photoRepository = photoRepository;
        this.auditService = auditService;
    }

    /**
     *  Register a new Article Photo.
     *
     * @param photo         photo bytes array of the article photo.
     * @param description   description of the article photo.
     * @param alt           alternate text of the article photo.
     * @return              article photo or empty optional.
     */
    public Optional<ArticlePhoto> register(byte[] photo, String description, String alt) {
        ArticlePhoto articlePhoto = new ArticlePhoto(
                photo,
                description,
                alt);

        try {
            articlePhoto = photoRepository.save(articlePhoto);
            LOGGER.debug("Created new user ArticlePhoto{description='{}'}", description);

            auditService.info("Created a new ArticlePhoto using description: %s", description);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new user ArticlePhoto{description='{}'}", description, e);

            auditService.error("Failed to create a new ArticlePhoto using description: %s", description);
            return Optional.empty();
        }
        return Optional.of(articlePhoto);
    }

    /**
     * Get an article photo by id.
     *
     * @return article photo.
     * @throws NotFoundException if the article photo is not found.
     */
    public ArticlePhoto getPhoto(long id) {
        ArticlePhoto photo = photoRepository.findOne(id);

        if (photo == null) {
            auditService.warn("Attempted to query non-existing ArticlePhoto with id: %d", id);
            throw new NotFoundException(ArticlePhoto.class, id);
        }
        return photo;
    }
}
