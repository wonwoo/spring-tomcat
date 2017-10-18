package me.wonwoo.springtomcat.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.wonwoo.springtomcat.domain.Person;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @GetMapping
    public List<Person> persons() {
        return Arrays.asList(new Person("wonwoo", "wonwoo@test.com"), new Person("kevin","kevin@test.com"));
    }

}
