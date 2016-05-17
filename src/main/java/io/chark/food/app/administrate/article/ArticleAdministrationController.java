package io.chark.food.app.administrate.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/administrate")
public class ArticleAdministrationController {

    private final ArticleAdministrationService articleAdministrationService;

    @Autowired
    public ArticleAdministrationController(ArticleAdministrationService articleAdministrationService) {
        this.articleAdministrationService = articleAdministrationService;
    }

    /**
     * Article administration page.
     *
     * @return template for administrating articles.
     */
    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public String articleCategoryAdministration() {
        return "administrate/articles";
    }
}
