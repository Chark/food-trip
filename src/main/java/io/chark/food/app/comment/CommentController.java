package io.chark.food.app.comment;

import io.chark.food.app.thread.ThreadService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.comment.CommentRepository;
import io.chark.food.domain.comment.Rating;
import io.chark.food.domain.comment.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/threads")
public class CommentController {

    private final CommentRepository commentRepository;
    private final RatingService ratingService;

    @Autowired
    public CommentController(CommentRepository commentRepository, RatingService ratingService) {
        this.commentRepository = commentRepository;
        this.ratingService = ratingService;
    }

    @RequestMapping(value = "/list/{cid}/thread/{tid}/comment/{comid}/{voteResult}", method = RequestMethod.GET)
    public String thread(@PathVariable long cid, @PathVariable long tid, @PathVariable long voteResult, @PathVariable long comid, Model model, HttpServletRequest request) {
        Comment comment = commentRepository.findOne(comid);

        boolean vote = false;
        if (voteResult != 0) {
            vote = true;
        }
        Rating rating = new Rating(vote);
        comment.addRaiting(rating);
        ratingService.register(rating);

        return "redirect:/threads/list/" + cid + "/thread/" + tid;
    }
}
