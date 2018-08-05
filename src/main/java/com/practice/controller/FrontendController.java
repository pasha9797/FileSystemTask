package com.practice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FrontendController {
    @RequestMapping(value = {"","/browser","/sign-in","/sign-up"}, method = RequestMethod.GET)
    public String index() {
        return "forward:index.html";
    }
}
