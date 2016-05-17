package io.chark.food.app.article.photo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/articles/photos")
public class ArticlePhotoController {

    private final ArticlePhotoService photoService;

    @Autowired
    public ArticlePhotoController(ArticlePhotoService photoService) {
        this.photoService = photoService;
    }

    /**
     * View article photo.
     */
    @RequestMapping("/{id}")
    public ResponseEntity<byte[]> photo(@PathVariable long id) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        byte[] photo = photoService.getPhoto(id).getPhoto();
        return new ResponseEntity<byte[]>(photo, headers, HttpStatus.CREATED);
    }
}
