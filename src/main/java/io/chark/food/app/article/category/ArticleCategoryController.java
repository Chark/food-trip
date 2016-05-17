package io.chark.food.app.article.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/articles/categories")
public class ArticleCategoryController {

    private final ArticleCategoryService categoryService;

    @Autowired
    public ArticleCategoryController(ArticleCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * View article categories.
     */
    @RequestMapping(value = "")
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getCategories());
        return "article/category/list";
    }
}
