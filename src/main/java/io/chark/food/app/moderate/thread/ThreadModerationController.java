package io.chark.food.app.moderate.thread;

import io.chark.food.domain.thread.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/moderate")
public class ThreadModerationController {

    private final ThreadModerationService threadModerationService;

    @Autowired
    public ThreadModerationController(ThreadModerationService threadModerationService) {
        this.threadModerationService = threadModerationService;
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
            thread = threadModerationService.getThread(id);
        }
        model.addAttribute("thread", thread);
        return "moderate/thread";
    }

    @RequestMapping(value = "/threads/edit/{id}", method = RequestMethod.POST)
    public String saveThread(@PathVariable long id,
                               Thread thread,
                               Model model) {

        if (!threadModerationService.saveThread(id, thread).isPresent()) {
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
        return threadModerationService.getThreads();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/threads/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        threadModerationService.delete(id);
    }


}
