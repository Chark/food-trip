package io.chark.food.app.administrate;

import io.chark.food.domain.authentication.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @RequestMapping(value = "/api/accounts", method = RequestMethod.GET)
    public List<Account> getAccounts() {
        return administrationService.getAccounts();
    }
}