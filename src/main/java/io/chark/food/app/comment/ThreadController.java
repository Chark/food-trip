package io.chark.food.app.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/threads")
public class ThreadController {

    private final ThreadService threadService;

    @Autowired
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @RequestMapping(value = "/list")
    public String list(Model model) {
        model.addAttribute("threads", threadService.getThreads());
        return "thread/thread_list";
    }
}
