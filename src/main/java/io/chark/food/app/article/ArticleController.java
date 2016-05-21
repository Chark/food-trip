package io.chark.food.app.article;

import io.chark.food.app.article.category.ArticleCategoryService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.category.ArticleCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleCategoryService categoryService;

    @Autowired
    public ArticleController(ArticleService articleService,
                             ArticleCategoryService categoryService) {

        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    /**
     * View article.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String list(@PathVariable long id, Model model) {

        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "article/article";
    }

    /**
     * Get a new article view.
     *
     * @return new article template.
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String getCreateArticle(Model model) {

        Article article = new Article();
        model.addAttribute("article", article);

        List<ArticleCategory> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);

        return "article/new_article";
    }

    /**
     * Create a new article.
     *
     * @return article page or the same page if an error occurred.
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String createArticle(Article article,
                                Model model) {

        // Perform the actual register.
        Optional<Article> optional = articleService.register(
                article.getRestaurant(),
                article.getTitle(),
                article.getDescription(),
                article.getShortDescription(),
                article.getMetaKeywords(),
                article.getMetaDescription());

        Article createdArticle = optional.get();
        articleService.setCategories(article, article.getCategories());

        if (!optional.isPresent()) {
            model.addAttribute("error", "Failed to create article," +
                    " please double check the details you've entered.");

            model.addAttribute("article", article);

            List<ArticleCategory> categories = categoryService.getCategories();
            model.addAttribute("categories", categories);

            return "article/new_article";
        }

        return "redirect:/articles/" + createdArticle.getId();
    }

    /**
     * Edit article view.
     *
     * @return edit article template.
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String getEditArticle(@PathVariable long id,
                                 Model model) {

        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);

        List<ArticleCategory> categories = categoryService.getCategories();
        model.addAttribute("categories", categories);

        return "article/edit_article";
    }

    /**
     * Edit article.
     *
     * @return article page or the same page if an error occurred.
     */
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String editArticle(@PathVariable long id,
                              Article article,
                              Model model) {

        // Perform the actual register.
        Optional<Article> optional = articleService.update(articleService.getArticle(id), article);

        if (!optional.isPresent()) {
            model.addAttribute("error", "Failed to update article," +
                    " please double check the details you've entered.");

            model.addAttribute("article", article);

            List<ArticleCategory> categories = categoryService.getCategories();
            model.addAttribute("categories", categories);

            return "article/edit_article";
        }

        return "redirect:/articles/" + id;
    }

}
