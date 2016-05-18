package io.chark.food.app.thread;

import io.chark.food.app.thread.categories.ThreadCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/threads")
public class ThreadController {

    private final ThreadService threadService;
    private final ThreadCategoryService threadCategoryService;

    @Autowired
    public ThreadController(ThreadService threadService,ThreadCategoryService threadCategoryService) {
        this.threadService = threadService;
        this.threadCategoryService = threadCategoryService;
    }

    @RequestMapping(value = "/list")
    public String list(Model model) {

        model.addAttribute("categories", threadCategoryService.getThreadCategories());
        return "thread/thread_list";
    }
}
