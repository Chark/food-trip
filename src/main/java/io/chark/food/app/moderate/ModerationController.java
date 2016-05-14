package io.chark.food.app.moderate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/moderate")
public class ModerationController {

    /**
     * Main dashboard for moderation and system administration.
     *
     * @return dashboard page template.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String dashboard() {
        return "moderate/dashboard";
    }
}