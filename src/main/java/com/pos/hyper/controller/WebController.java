package com.pos.hyper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WebController {

    @GetMapping("")
    String sayHello() {
        return "Hello team hyper, Let's Do this...";
    }
}
