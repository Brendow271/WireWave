package com.wirewave.wirewave.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRestController {

    @GetMapping("/")
    public String helloWorld(){
        return "Hello World!";
    }

    @GetMapping("/test")
    public String testHelloWorld(){
        return "Test Hello World!";
    }
}