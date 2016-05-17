package io.chark.food.app.article;

import io.chark.food.domain.article.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
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

    @RequestMapping("/{id}/images/{imageId}")
    public ResponseEntity<byte[]> photo(@PathVariable long id, @PathVariable int imageId) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        System.out.println(articleService.getArticle(id).getPhotos().size());
        return new ResponseEntity<byte[]>(new byte[5], headers, HttpStatus.CREATED);
    }


}
