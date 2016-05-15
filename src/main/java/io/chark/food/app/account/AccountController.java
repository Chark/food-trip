package io.chark.food.app.account;

import io.chark.food.domain.authentication.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "account/login";
    }

    /**
     * Get register page and send an empty registration model to the register page.
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("register", new AccountRegisterModel());
        return "account/register";
    }

    /**
     * Register using for validation and models.
     *
     * @param register registration model.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute("register") @Valid AccountRegisterModel register,
                           BindingResult result,
                           RedirectAttributes attributes,
                           Model model) {

        // Basic input validation.
        register.validate(result);
        if (result.hasErrors()) {
            return "account/register";
        }

        // Perform the actual register.
        boolean created = accountService.register(
                register.getUsername(),
                register.getEmail(),
                register.getPassword())
                .isPresent();

        // Email might be taken and etc.
        if (!created) {
            model.addAttribute("error", "Invalid credentials, please try again");
            return "account/register";
        }

        // Flash attributes are required during redirects!
        attributes.addFlashAttribute("success", "Account created, now you can log in");
        return "redirect:/login";
    }

    /**
     * View profile details.
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model) {
        model.addAttribute("account", accountService.getAccount());
        return "account/profile";
    }

    /**
     * Update account profile details.
     *
     * @param account details used in updating.
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profile(Account account, RedirectAttributes attributes) {
        if (!accountService.update(account).isPresent()) {
            attributes.addFlashAttribute("error", "Could not update your profile details, the email might be" +
                    " taken or some error occurred.");
        }
        attributes.addFlashAttribute("profileTab", true);
        return "redirect:/profile";
    }

    /**
     * Accept or ignore invitation to join a restaurant.
     *
     * @param id     invitation id.
     * @param accept should the invitation be accepted or ignored.
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/profile/api/invitation/{id}", method = RequestMethod.POST)
    public void invitation(@PathVariable long id, @RequestParam boolean accept) {

        // Either accept or ignore the invitation.
        if (accept) {
            accountService.acceptInvitation(id);
        } else {
            accountService.ignoreInvitation(id);
        }
    }
}