package com.pos.hyper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WebController {


        @GetMapping("docs")
    String getDocs() {
        return "Hello, we are team hyper... <a href=\"https://documenter.getpostman.com/view/40718634/2sB2j4gX6J\">API Documentation</a>";
    }
    @GetMapping("")
    String sayHello() {
        return "Hello, we are team hyper... <a href=\"https://documenter.getpostman.com/view/40718634/2sB2j4gX6J\">API Documentation</a>";
    }



}

