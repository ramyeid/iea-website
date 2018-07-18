package com.iea.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class Index {
    @RequestMapping
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/canvas")
    public String canvas(Model model) {
        return "canvas";
    }

    @PostMapping("/canvas/update")
    public void canvasUpdate(@RequestParam("components") String components, @RequestParam("connections") String connections) {
        System.out.println(components);
        System.out.println(connections);
    }
}