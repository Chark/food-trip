package io.chark.food.app.thread.categories;


import io.chark.food.app.account.AccountService;
import io.chark.food.app.thread.ThreadService;
import io.chark.food.domain.authentication.account.Account;
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
    private final AccountService accountService;

    @Autowired
    public ThreadCategoryController(ThreadService threadService,ThreadCategoryService threadCategoryService,AccountService accountService) {
        this.threadService = threadService;
        this.threadCategoryService = threadCategoryService;
        this.accountService = accountService;
    }

    @RequestMapping(value = "/list")
    public String categories(Model model) {
        model.addAttribute("categories", threadCategoryService.getThreadCategories());
        return "thread/category/category_list";
    }

    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    public String category(@PathVariable long id, Model model) {
        boolean canEdit = false;
        try{
            Account currAccount = accountService.getAccount();
            if (currAccount != null) {
                canEdit = true;
            }
        }catch (Exception e){
            System.out.println(e);
        }


        model.addAttribute("category", threadCategoryService.getThreadCategory(id));
        model.addAttribute("canEdit", canEdit);
        return "thread/category/category_view";
    }
}