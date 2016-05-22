package io.chark.food.app.moderate.comment;

import io.chark.food.domain.comment.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/moderate")
public class CommentModerationController {

    private final CommentModerationService commentModerationService;

    @Autowired
    public CommentModerationController(CommentModerationService commentModerationService) {
        this.commentModerationService = commentModerationService;
    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET)
    public String threads() {
        return "moderate/comments";
    }

    @ResponseBody
    @RequestMapping(value = "/api/comments", method = RequestMethod.GET)
    public List<Comment> getComments() {
        return commentModerationService.getComments();
    }



    @RequestMapping(value = "/comments/edit/{id}", method = RequestMethod.GET)
    public String getComment(@PathVariable long id, Model model) {
        Comment comment;

        if (id <= 0) {
            // Id below or equals to zero means this is a new thread.
            comment = new Comment();
        } else {
            // Id is above zero, existing account.
            comment = commentModerationService.getComment(id);
        }
        model.addAttribute("comment", comment);
        return "moderate/comment";
    }

    @RequestMapping(value = "/comments/edit/{id}", method = RequestMethod.POST)
    public String saveComment(@PathVariable long id,
                             Comment comment,
                             Model model) {

        if (!commentModerationService.saveComment(id, comment).isPresent()) {
            model.addAttribute("error", "Failed to create comment," +
                    " please double check the details you've entered.");

            model.addAttribute("comment", comment);
            return "moderate/comment";
        }
        return "redirect:/moderate/comments";
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/comments/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        commentModerationService.delete(id);
    }

}
