package io.chark.food.domain.moderation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/moderate")
public class ModerationController {

    @RequestMapping(method = RequestMethod.GET)
    public String moderator() {
        return "moderate/dashboard";
    }
}