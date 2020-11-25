package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private SectionConfigurationProperties sectionConfigurationProperties;

    @RequestMapping(path = "/hello")
    public String helloWorld() {
        return "hello world";
    }

    @RequestMapping(path = "/props")
    public SectionConfigurationProperties props() {
        return sectionConfigurationProperties;
    }
}
