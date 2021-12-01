package com.turing.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author Tianyi Yan
 * @Date 2021/12/01 14:35
 * Redirecting to index.html
 **/
@Controller
public class IndexController {
    @RequestMapping("/api/**") // All/api/** requests return index.html page
    public ModelAndView ui() {
        return new ModelAndView("/index.html");
    }
    @RequestMapping("/") // Visit root directory: redirect to the request above
    public ModelAndView index() {
        return new ModelAndView("forward:ui");
    }
}
