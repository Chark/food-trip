package io.chark.food.app.thread;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.comment.RatingService;
import io.chark.food.app.moderate.comment.CommentModerationService;
import io.chark.food.app.thread.categories.ThreadCategoryService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.thread.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping(value = "/threads")
public class ThreadController {

    private final ThreadService threadService;
    private final AccountService accountService;
    private final CommentModerationService commentModerationService;

    @Autowired
    public ThreadController(ThreadService threadService, AccountService accountService, CommentModerationService commentModerationService) {
        this.threadService = threadService;
        this.accountService = accountService;
        this.commentModerationService = commentModerationService;
    }

    @RequestMapping(value = "/list/{cid}/thread/{tid}", method = RequestMethod.GET)
    public String thread(@PathVariable long cid, @PathVariable long tid, Model model) {
        Thread t = threadService.getThread(tid);
        boolean canEdit = false;
        boolean canComment = false;
        boolean canEditComment = false;
        try {
            Account currAccount = accountService.getAccount();
            if (t.getAccount().getId() == currAccount.getId() ||
                    currAccount.hasPermission(Permission.Authority.ROLE_MODERATOR) ||
                    currAccount.hasPermission(Permission.Authority.ROLE_ADMIN)) {
                canEdit = true;
                model.addAttribute("account", currAccount);
            }

            canComment = true;
        } catch (Exception e) {
            if (t.isRegistrationRequired()) {
                return "redirect:/threads/list";
            }
        }

        threadService.incrementView(t);
        model.addAttribute("canEdit", canEdit);
        model.addAttribute("canComment", canComment);
        model.addAttribute("thread", threadService.getThread(tid));
        return "thread/thread_view";
    }

    @RequestMapping(value = "/list/{cid}/thread/{tid}/delete", method = RequestMethod.GET)
    public String deleteThread(@PathVariable long cid, @PathVariable long tid, Model model) {
        threadService.delete(tid);
        return "redirect:/threads/list/" + cid;
    }

    @RequestMapping(value = "/list/{cid}/thread/control/{tid}", method = RequestMethod.GET)
    public String controlThread(@PathVariable long cid, @PathVariable long tid, Model model) {
        Thread t;
        boolean canEdit = false;
        if (tid <= 0) {
            t = new Thread();
        } else {
            t = threadService.getThread(tid);
            Account currAccount = accountService.getAccount();
            if (t.getAccount().getId() == currAccount.getId() ||
                    currAccount.hasPermission(Permission.Authority.ROLE_MODERATOR) ||
                    currAccount.hasPermission(Permission.Authority.ROLE_ADMIN)) {
                canEdit = true;
            } else {
                return "thread/thread_view";
            }
        }


        model.addAttribute("thread", t);
        model.addAttribute("category", cid);
        model.addAttribute("canEdit", canEdit);
        return "thread/thread_create";
    }

    @RequestMapping(value = "/list/{cid}/thread/control/{tid}", method = RequestMethod.POST)
    public String saveThread(@PathVariable long cid, @PathVariable long tid, Thread thread, Model model) {

        if (!threadService.saveThread(tid, cid, thread).isPresent()) {
            model.addAttribute("error", "Failed to create thread," +
                    " please double check the details you've entered.");

            model.addAttribute("thread", thread);

            return "thread/thread_create";
        }
        return "redirect:/threads/list/" + cid;
    }


    @RequestMapping(value = "/{cid}/{tid}/comments/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable long id, @PathVariable long cid, @PathVariable long tid) {
        commentModerationService.delete(id);
        return "redirect:/threads/list/" + cid + "/thread/" + tid;
    }


}
