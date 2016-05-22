package io.chark.food.app.comment;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.thread.ThreadService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.comment.CommentRepository;
import io.chark.food.domain.comment.Rating;
import io.chark.food.domain.thread.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/threads")
public class CommentController {

    private final CommentService commentService;
    private final RatingService ratingService;
    private final AccountService accountService;
    private final ThreadService threadService;

    @Autowired
    public CommentController(RatingService ratingService, CommentService commentService, AccountService accountService, ThreadService threadService) {
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.accountService = accountService;
        this.threadService = threadService;
    }

    @RequestMapping(value = "/comment/{comid}/{voteResult}", method = RequestMethod.GET)
    public String vote(@PathVariable long voteResult, @PathVariable long comid, Model model, HttpServletRequest request) {
        Comment comment = commentService.getComment(comid);
        Account account;
        boolean vote = false;
        if (voteResult != 0) {
            vote = true;
        }
        try {
            account = accountService.getAccount();

            List<Rating> ratings = comment.getRatings();
            for (Rating r : ratings) {
                if (r.getAccount().getId() == account.getId()) {
                    return "redirect:" + request.getHeader("Referer");
                }
            }


            if(account.getId() != comment.getAccount().getId()){
                comment.getAccount().increaseKarmaPoints(vote);
//                accountService.save(comment.getAccount());
            }

        } catch (DataIntegrityViolationException e) {
            return "redirect:" + request.getHeader("Referer");
        }


        Rating rating = new Rating(vote, account);
        comment.addRaiting(rating);
        ratingService.register(rating);

        return "redirect:" + request.getHeader("Referer");
    }






    @RequestMapping(value = "/list/{cid}/thread/control/{tid}/comment/{comid}", method = RequestMethod.GET)
    public String controlComment(@PathVariable long cid, @PathVariable long tid, @PathVariable long comid, Model model) {
        Comment comment;
        boolean canEdit = false;
        Account currAccount;
        try {
            currAccount = accountService.getAccount();
        } catch (DataIntegrityViolationException e) {
            return "redirect:/threads/list/" + cid + "thread/" + tid;
        }

        if (comid <= 0) {
            comment = new Comment();


        } else {
            comment = commentService.getComment(comid);

            if (comment.getAccount().getId() == currAccount.getId() ||
                    currAccount.hasPermission(Permission.Authority.ROLE_MODERATOR) ||
                    currAccount.hasPermission(Permission.Authority.ROLE_ADMIN)) {
                canEdit = true;
            } else {
                return "redirect:/threads/list/" + cid + "thread/" + tid;
            }
        }
        Thread t = threadService.getThread(tid);

        model.addAttribute("comment", comment);
        model.addAttribute("category", t.getThreadCategory().getId());
        model.addAttribute("thread", tid);
        model.addAttribute("canEdit", canEdit);
        return "comment/thread_create";
    }




    @RequestMapping(value = "/list/{cid}/thread/control/{tid}/comment/{comid}", method = RequestMethod.POST)
    public String saveComment(@PathVariable long cid, @PathVariable long tid, @PathVariable long comid, Comment comment, Model model) {

        if (!commentService.saveComment(tid, comid, comment).isPresent()) {
            model.addAttribute("error", "Failed to create comment," +
                    " please double check the details you've entered.");

            Thread thread = threadService.getThread(tid);
            model.addAttribute("thread", thread);

            return "thread/thread_create";
        }

        return "redirect:/threads/list/" + cid + "/thread/" + tid;
    }
}
