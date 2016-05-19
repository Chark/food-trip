package io.chark.food.app.thread;

import io.chark.food.app.thread.categories.ThreadCategoryService;
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
    private final ThreadCategoryService threadCategoryService;

    @Autowired
    public ThreadController(ThreadService threadService,ThreadCategoryService threadCategoryService) {
        this.threadService = threadService;
        this.threadCategoryService = threadCategoryService;
    }


}
