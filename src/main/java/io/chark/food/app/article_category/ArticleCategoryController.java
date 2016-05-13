package io.chark.food.app.article_category;

import javafx.scene.shape.Arc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ArticleCategoryController {

    private final ArticleCategoryService categoryService;

    @Autowired
    public ArticleCategoryController(ArticleCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * View article categories.
     */
    @RequestMapping(value = "/list")
    public String list() {
        return "articleCategory/list";
    }
}
