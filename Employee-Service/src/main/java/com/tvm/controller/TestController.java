package com.tvm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private static final Logger log =
            LoggerFactory.getLogger(TestController.class);

    @GetMapping("/welcome")
    public String welcome() {
		log.info("Employee service welcome endpoint called");
        return "Employee Service Working";
    }
}
