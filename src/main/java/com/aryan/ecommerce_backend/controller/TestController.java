package com.aryan.ecommerce_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/protected")
    public String protectedEndpoint() {
        System.out.println(">>> CONTROLLER METHOD REACHED <<<");
        String result = "You are authenticated!";
        System.out.println(">>> RETURNING: " + result + " <<<");
        return result;
    }
}