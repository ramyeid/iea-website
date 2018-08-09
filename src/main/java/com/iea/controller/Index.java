package com.iea.controller;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.*;
import com.iea.listener.AsynchronousScreenListenersNotifier;
import com.iea.listener.ScreenListener;
import com.iea.serializer.exception.NoMatchingPinFoundException;
import com.iea.simulator.CircuitSimulator;
import com.iea.simulator.exception.NoGeneratorException;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.iea.serializer.Serializer.deSerialize;
import static com.iea.serializer.Serializer.serialize;

@Controller
@RequestMapping("/")
public class Index {

    static private SseEmitter userSseEmitter = null;

    @RequestMapping
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/canvas")
    public String canvas(Model model) {
        return "canvas";
    }

    @GetMapping("/canvas/notifications")
    public SseEmitter establishSseConnection() {
        userSseEmitter = new SseEmitter(600000L);
        return userSseEmitter;
    }

    @PostMapping("/canvas/submit")
    @ResponseBody
    public void onSubmit(@RequestParam("generators") String generators, @RequestParam("receivers") String receivers, @RequestParam("connections") String connections) {
        try {
            Circuit userCircuit = deSerialize(generators, receivers, connections);
            AsynchronousScreenListenersNotifier.onSubmit(userCircuit, userSseEmitter);
        } catch (NoMatchingPinFoundException e) {
            //TODO LOG ERROR HERE
            e.printStackTrace();
        }
    }
}