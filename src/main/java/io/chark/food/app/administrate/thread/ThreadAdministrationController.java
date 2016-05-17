package io.chark.food.app.administrate.thread;

import io.chark.food.domain.comment.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/administrate")
public class ThreadAdministrationController {

    private final ThreadAdministrationService threadAdministrationService;

    @Autowired
    public ThreadAdministrationController(ThreadAdministrationService threadAdministrationService) {
        this.threadAdministrationService = threadAdministrationService;
    }

    @RequestMapping(value = "/thread", method = RequestMethod.GET)
    public String thread() {
        return "administrate/thread";
    }

    @ResponseBody
    @RequestMapping(value = "/api/threads", method = RequestMethod.GET)
    public List<Thread> getThreads() {
        return threadAdministrationService.getThreads();
    }
}
