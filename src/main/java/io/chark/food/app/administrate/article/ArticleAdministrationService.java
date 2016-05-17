package io.chark.food.app.administrate.article;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.article.ArticleService;
import io.chark.food.domain.article.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleAdministrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleAdministrationService.class);

    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final AuditService auditService;

    @Autowired
    public ArticleAdministrationService(ArticleRepository articleRepository,
                                        ArticleService articleService,
                                        AuditService auditService) {

        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.auditService = auditService;
    }
}
