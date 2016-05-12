package io.chark.food.app.authentication;

import io.chark.food.app.account.AccountRegisterModel;
import io.chark.food.app.account.AccountService;
import io.chark.food.domain.authentication.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

    private final AccountService accountService;

    @Autowired
    public AuthenticationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "authentication/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("register", new AccountRegisterModel());
        return "authentication/register";
    }

    /**
     * Register using for validation and models.
     *
     * @param register registration model.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("register") @Valid AccountRegisterModel register,
                           RedirectAttributes attributes,
                           BindingResult result,
                           Model model) {

        // Basic input validation.
        register.validate(result);
        if (result.hasErrors()) {
            return "authentication/register";
        }

        // Perform the actual register.
        Account account = accountService.register(
                register.getUsername(),
                register.getEmail(),
                register.getPassword())
                .get();

        // Email might be taken and etc.
        if (account == null) {
            model.addAttribute("error", "Invalid credentials, please try again");
            return "authentication/register";
        }

        // Flash attributes are required during redirects!
        attributes.addFlashAttribute("success", "Account created, now you can log in");
        return "redirect:/login";
    }
}