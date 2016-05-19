package io.chark.food.app.thread;

import io.chark.food.app.account.AccountService;
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


@Controller
@RequestMapping(value = "/threads")
public class ThreadController {

    private final ThreadService threadService;
    private final AccountService accountService;

    @Autowired
    public ThreadController(ThreadService threadService, AccountService accountService) {
        this.threadService = threadService;
        this.accountService = accountService;
    }

    @RequestMapping(value = "/list/{cid}/thread/{tid}", method = RequestMethod.GET)
    public String thread(@PathVariable long cid, @PathVariable long tid, Model model) {
        Thread t = threadService.getThread(tid);
        boolean canEdit = false;
        try {
            Account currAccount = accountService.getAccount();
            if (t.getAccount().getId() == currAccount.getId() ||
                    currAccount.hasPermission(Permission.Authority.ROLE_MODERATOR) ||
                    currAccount.hasPermission(Permission.Authority.ROLE_ADMIN)) {
                canEdit = true;
            }
        } catch (Exception e) {
            System.out.println(e);
            if (t.isRegistrationRequired()) {
                return "redirect:/threads/list";
            }
        }

        threadService.incrementView(t);
        model.addAttribute("canEdit", canEdit);
        model.addAttribute("thread", threadService.getThread(tid));
        return "thread/thread_view";
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


}
