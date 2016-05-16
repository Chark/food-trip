package io.chark.food.app.administrate.article;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/administrate")
public class ArticleCategoryAdministrationController {

    @RequestMapping(value = "/articles/categories", method = RequestMethod.GET)
    public String articleCategoryAdministration() {
        return "administrate/article_categories";
    }
}
