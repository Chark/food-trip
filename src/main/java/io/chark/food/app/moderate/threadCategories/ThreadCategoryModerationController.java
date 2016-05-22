package io.chark.food.app.moderate.threadCategories;

import io.chark.food.domain.thread.Thread;
import io.chark.food.domain.thread.category.ThreadCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/moderate")
public class ThreadCategoryModerationController {

    private final ThreadCategoryModerationService threadCategoryModerationService;

    @Autowired
    public ThreadCategoryModerationController(ThreadCategoryModerationService threadCategoryModerationService) {
        this.threadCategoryModerationService = threadCategoryModerationService;
    }

    @RequestMapping(value = "/threads/categories", method = RequestMethod.GET)
    public String threads() {
        return "moderate/threadCategories";
    }


    @ResponseBody
    @RequestMapping(value = "/api/threads/categories", method = RequestMethod.GET)
    public List<ThreadCategory> getThreads() {
        return threadCategoryModerationService.getThreadCategories();
    }



    @RequestMapping(value = "/threads/categories/edit/{id}", method = RequestMethod.GET)
    public String getThread(@PathVariable long id, Model model) {
        ThreadCategory threadCategory;

        if (id <= 0) {
            // Id below or equals to zero means this is a new thread.
            threadCategory = new ThreadCategory();
        } else {
            // Id is above zero, existing account.
            threadCategory = threadCategoryModerationService.getThreadCategory(id);
        }
        model.addAttribute("threadCategory", threadCategory);
        return "moderate/threadCategory";
    }

    @RequestMapping(value = "/threads/categories/edit/{id}", method = RequestMethod.POST)
    public String saveThread(@PathVariable long id,
                             ThreadCategory threadCategory,
                             Model model) {

        if (!threadCategoryModerationService.saveThreadCategory(id, threadCategory).isPresent()) {
            model.addAttribute("error", "Failed to create account," +
                    " please double check the details you've entered. The email or username might already be taken");

            model.addAttribute("threadCategory", threadCategory);
            return "moderate/threadCategory";
        }
        return "redirect:/moderate/threads/categories";
    }



    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/threads/categories/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        threadCategoryModerationService.deleteCategory(id);
    }
}
