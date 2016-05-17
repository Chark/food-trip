package io.chark.food.app.article.photo;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.article.photo.ArticlePhoto;
import io.chark.food.domain.article.photo.ArticlePhotoRepository;
import io.chark.food.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticlePhotoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final ArticlePhotoRepository photoRepository;
    private final AuditService auditService;

    @Autowired
    public ArticlePhotoService(ArticlePhotoRepository photoRepository,
                               AuditService auditService) {

        this.photoRepository = photoRepository;
        this.auditService = auditService;
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
