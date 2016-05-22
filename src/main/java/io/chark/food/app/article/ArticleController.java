package io.chark.food.app.article;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.article.category.ArticleCategoryService;
import io.chark.food.app.comment.CommentService;
import io.chark.food.app.moderate.comment.CommentModerationService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.thread.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final AccountService accountService;
    private final ArticleCategoryService categoryService;
    private final CommentService commentService;
    private final CommentModerationService commentModerationService;

    @Autowired
    public ArticleController(ArticleService articleService,
                             ArticleCategoryService categoryService,
                             AccountService accountService,
                             CommentService commentService,
                             CommentModerationService commentModerationService) {

        this.articleService = articleService;
        this.categoryService = categoryService;
        this.accountService = accountService;
        this.commentService = commentService;
        this.commentModerationService = commentModerationService;
    }

    /**
     * View article.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String list(@PathVariable long id, Model model, HttpServletRequest request) {

        boolean canVote = false;
        Account currAccount;
        try {
            currAccount = accountService.getAccount();
            model.addAttribute("account",currAccount);
            canVote = true;
        } catch (Exception e) {

        }

        model.addAttribute("canVote", canVote);


        Article article = articleService.getArticle(id);
        model.addAttribute("article", article);
        return "article/article";
    }

    @RequestMapping(value = "/{aid}/comment", method = RequestMethod.GET)
    public String controlComment(@PathVariable long aid, Model model, HttpServletRequest request) {
        Comment comment;

        Account currAccount;
        try {
            currAccount = accountService.getAccount();
        } catch (DataIntegrityViolationException e) {
            return "redirect:" + request.getHeader("Referer");
        }

        comment = new Comment();
        model.addAttribute("article", aid);
        model.addAttribute("comment", comment);
        return "comment/article_create";
    }


    @RequestMapping(value = "/{aid}/comment", method = RequestMethod.POST)
    public String saveComment(@PathVariable long aid, Comment c, Model model, HttpServletRequest request) {

        if (!commentService.saveCommentArticle(aid, c).isPresent()) {
            model.addAttribute("error", "Failed to create thread," +
                    " please double check the details you've entered.");

            model.addAttribute("comment", c);

            return "thread/thread_create";
        }
        return "redirect:/articles/" + aid;
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
        Optional<Article> optional = articleService.register(article);

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
        Optional<Article> optional = articleService.update(id, article);

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

    @RequestMapping(value = "/{aid}/comments/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id,@PathVariable long aid,HttpServletRequest request) {
        commentModerationService.delete(id);
        return "redirect:/articles/" + aid;
    }

}
