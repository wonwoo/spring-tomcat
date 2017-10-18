package me.wonwoo.springtomcat.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/{nm}")
    public String hello(@PathVariable String nm) {
        return "hello " + nm + "!";
    }

}
