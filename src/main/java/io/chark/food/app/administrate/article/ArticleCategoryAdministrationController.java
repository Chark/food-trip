package io.chark.food.app.administrate.article;

import io.chark.food.domain.article.ArticleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/administrate")
public class ArticleCategoryAdministrationController {

    private final ArticleCategoryAdministrationService administrationService;

    @Autowired
    public ArticleCategoryAdministrationController(ArticleCategoryAdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    @RequestMapping(value = "/articles/categories", method = RequestMethod.GET)
    public String articleCategoryAdministration() {
        return "administrate/article_categories";
    }

    @ResponseBody
    @RequestMapping(value = "/api/articles/categories", method = RequestMethod.GET)
    public List<ArticleCategory> getCategories() {
        return administrationService.getCategories();
    }
}
