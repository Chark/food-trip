package io.chark.food.app.administrate;

import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.permission.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/administrate")
public class AccountAdministrationController {

    private final AccountAdministrationService administrationService;

    @Autowired
    public AccountAdministrationController(AccountAdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    /**
     * Account administration page.
     *
     * @return template for administrating accounts.
     */
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String accountAdministration() {
        return "administrate/accounts";
    }

    /**
     * Get a single account administration page, if negative id is provided, a empty account template is returned.
     *
     * @return single account administration template.
     */
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public String getAccount(@PathVariable long id, Model model) {
        Account account;

        if (id <= 0) {
            // Id below or equals to zero means this is a new account.
            account = new Account();
        } else {
            // Id is above zero, existing account.
            account = administrationService.getAccount(id);
        }
        model.addAttribute("account", account);
        return "administrate/account";
    }

    /**
     * Create a new user account or update an existing one based on provided id.
     *
     * @return account administration page or the same page if an error occurred.
     */
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.POST)
    public String saveAccount(@PathVariable long id,
                              Account account,
                              Permission.Authority[] authorities,
                              Model model) {

        if (!administrationService.saveAccount(id, account, authorities).isPresent()) {
            model.addAttribute("error", "Failed to create account," +
                    " please double check the details you've entered");

            model.addAttribute("account", account);
            return "administrate/account";
        }
        return "redirect:/administrate/accounts";
    }


    /**
     * Get list of users, by also excluding or including currently authenticated account.
     *
     * @return list of accounts.
     */
    @ResponseBody
    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    public List<Account> getAccounts(@RequestParam(defaultValue = "false") boolean includeSelf) {
        return administrationService.getAccounts(includeSelf);
    }

    /**
     * Delete specified user account by id.
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        administrationService.delete(id);
    }
}