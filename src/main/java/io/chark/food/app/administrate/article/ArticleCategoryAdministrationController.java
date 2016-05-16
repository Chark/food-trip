package io.chark.food.app.administrate.article;

import io.chark.food.domain.article.ArticleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/administrate")
public class ArticleCategoryAdministrationController {

    private final ArticleCategoryAdministrationService administrationService;

    @Autowired
    public ArticleCategoryAdministrationController(ArticleCategoryAdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    /**
     * Article category administration page.
     *
     * @return template for administrating article categories.
     */
    @RequestMapping(value = "/articles/categories", method = RequestMethod.GET)
    public String articleCategoryAdministration() {
        return "administrate/article_categories";
    }

    /**
     * Get list of article categories.
     *
     * @return list of article categories.
     */
    @ResponseBody
    @RequestMapping(value = "/api/articles/categories", method = RequestMethod.GET)
    public List<ArticleCategory> getCategories() {
        return administrationService.getCategories();
    }

    /**
     * Delete specified article category by id.
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/articles/categories/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        administrationService.delete(id);
    }
}
