package com.iea.controller;

import com.google.common.util.concurrent.ListenableFuture;
import com.iea.listener.AsynchronousScreenListenersNotifier;
import com.iea.utils.CustomSseEmitter;
import com.iea.utils.EmitterException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/")
public class Index {

    static private CustomSseEmitter userSseEmitter = null;

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
        userSseEmitter = new CustomSseEmitter(600000L);
        return userSseEmitter;
    }

    @PostMapping("/canvas/submit")
    @ResponseBody
    public void onSubmit(@RequestParam("generators") String generators, @RequestParam("receivers") String receivers, @RequestParam("connections") String connections, Model model) {
        //todo:
        // SSE EMITTER CAN SEND ON ERRORS: IN FUNCTIONS
        // MODEL.ADDATTRIBUTE("ONERROR").
        // ADD A BANNER THAT SHOWS THESE ERRORS.
        CompletableFuture
                .runAsync(() -> {
                    try {
                        AsynchronousScreenListenersNotifier.onSubmit(generators, receivers, connections, userSseEmitter);
                    } catch (EmitterException emitterException) {
                        //TODO LOG HERE.
                        model.addAttribute("ERROR", emitterException.getMessage());
                    }
                });
    }
}