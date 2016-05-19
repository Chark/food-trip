package io.chark.food.app.thread.categories;


import io.chark.food.app.thread.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/threads")
public class ThreadCategoryController {
    private final ThreadService threadService;
    private final ThreadCategoryService threadCategoryService;

    @Autowired
    public ThreadCategoryController(ThreadService threadService,ThreadCategoryService threadCategoryService) {
        this.threadService = threadService;
        this.threadCategoryService = threadCategoryService;
    }

    @RequestMapping(value = "/list")
    public String categories(Model model) {

        model.addAttribute("categories", threadCategoryService.getThreadCategories());
        return "thread/category_list";
    }

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public String category(@PathVariable long id, Model model) {

        model.addAttribute("category", threadCategoryService.getThreadCategory(id));
        return "thread/category_view";
    }

}
