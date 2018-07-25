package com.iea.controller;

import com.iea.circuit.Circuit;
import com.iea.serializer.Serializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public String canvasUpdate(@RequestParam("generators") String generators, @RequestParam("receivers") String receivers, @RequestParam("connections") String connections) {
        Circuit userCircuit = Serializer.serialize(generators, receivers, connections);
        return "SUCCESS!";
    }
}