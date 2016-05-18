package io.chark.food.app.moderate.thread;

import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.comment.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/moderate")
public class ThreadAdministrationController {

    private final ThreadAdministrationService threadAdministrationService;

    @Autowired
    public ThreadAdministrationController(ThreadAdministrationService threadAdministrationService) {
        this.threadAdministrationService = threadAdministrationService;
    }

    @RequestMapping(value = "/threads", method = RequestMethod.GET)
    public String threads() {
        return "moderate/threads";
    }


    @RequestMapping(value = "/threads/edit/{id}", method = RequestMethod.GET)
    public String getThread(@PathVariable long id, Model model) {
        Thread thread;

        if (id <= 0) {
            // Id below or equals to zero means this is a new thread.
            thread = new Thread();
        } else {
            // Id is above zero, existing account.
            thread = threadAdministrationService.getThread(id);
        }
        model.addAttribute("thread", thread);
        return "moderate/thread";
    }

    @RequestMapping(value = "/threads/edit/{id}", method = RequestMethod.POST)
    public String saveThread(@PathVariable long id,
                               Thread thread,
                               Model model) {

        if (!threadAdministrationService.saveThread(id, thread).isPresent()) {
            model.addAttribute("error", "Failed to create account," +
                    " please double check the details you've entered. The email or username might already be taken");

            model.addAttribute("thread", thread);
            return "moderate/thread";
        }
        return "redirect:/moderate/threads";
    }


    @ResponseBody
    @RequestMapping(value = "/api/threads", method = RequestMethod.GET)
    public List<Thread> getThreads() {
        return threadAdministrationService.getThreads();
    }




}
