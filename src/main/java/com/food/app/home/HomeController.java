package com.food.app.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        List<String> variables = new ArrayList<>();
        variables.add("One");
        variables.add("Two");
        variables.add("Three!");
        model.addAttribute("variables", variables);
        return "home/home";
    }
}