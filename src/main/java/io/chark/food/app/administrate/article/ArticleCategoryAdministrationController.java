package io.chark.food.app.administrate.article;

import io.chark.food.domain.article.ArticleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
     * Get a single article category administration page, if negative id is provided, a empty article category template is returned.
     *
     * @return single article category administration template.
     */
    @RequestMapping(value = "/articles/categories/{id}", method = RequestMethod.GET)
    public String getCategory(@PathVariable long id, Model model) {
        ArticleCategory category;

        if (id <= 0) {
            // Id below or equals to zero means this is a new article cagegory.
            category = new ArticleCategory();
        } else {
            // Id is above zero, existing account.
            category = administrationService.getCategory(id);
        }
        model.addAttribute("category", category);
        return "administrate/article_category";
    }

    /**
     * Create a new user article category or update an existing one based on provided id.
     *
     * @return article category administration page or the same page if an error occurred.
     */
    @RequestMapping(value = "/articles/categories/{id}", method = RequestMethod.POST)
    public String saveCategory(@PathVariable long id,
                              ArticleCategory category,
                              Model model) {

        if (!administrationService.saveCategory(id, category).isPresent()) {
            model.addAttribute("error", "Failed to create account," +
                    " please double check the details you've entered. The email or username might already be taken");

            model.addAttribute("category", category);
            return "administrate/article_category";
        }
        return "redirect:/administrate/article_categories";
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
