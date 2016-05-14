package io.chark.food.app.administrate;

import io.chark.food.domain.authentication.account.Account;
import io.chark.food.util.exception.NotFoundException;
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
     * Get a single account administration page.
     *
     * @param id account id.
     * @return single account administration template.
     */
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public String getAccount(@PathVariable long id, Model model) {
        Account account = administrationService.getAccount(id)
                .orElseThrow(() -> new NotFoundException(Account.class, id));

        model.addAttribute("account", account);
        return "administrate/account";
    }

    /**
     * Get list of users, by also excluding or including currently authenticated account.
     *
     * @param includeSelf should currently authenticated account be included.
     * @return list of accounts.
     */
    @ResponseBody
    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    public List<Account> getAccounts(@RequestParam(defaultValue = "false") boolean includeSelf) {
        return administrationService.getAccounts(includeSelf);
    }

    /**
     * Delete specified user account by id.
     *
     * @param id account id which is to be deleted.
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/accounts/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        administrationService.delete(id);
    }
}