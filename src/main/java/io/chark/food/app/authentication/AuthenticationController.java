package io.chark.food.app.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AuthenticationController {

    @RequestMapping(value = "/login")
    public String login() {
        return "authentication/login";
    }
}