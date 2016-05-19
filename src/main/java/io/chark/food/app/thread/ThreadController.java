package io.chark.food.app.thread;

import io.chark.food.app.thread.categories.ThreadCategoryService;
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

    @Autowired
    public ThreadController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @RequestMapping(value = "/list/{cid}/thread/{tid}", method = RequestMethod.GET)
    public String thread(@PathVariable long cid, @PathVariable long tid, Model model) {
        Thread t = threadService.getThread(tid);
        model.addAttribute("thread", threadService.getThread(tid));
        return "thread/thread_view";
    }



}
