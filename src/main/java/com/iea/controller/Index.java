package com.iea.controller;

import com.iea.Application;
import com.iea.listener.AsynchronousScreenListenersNotifier;
import com.iea.utils.CustomSseEmitter;
import com.iea.utils.EmitterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/")
public class Index {

    private static final Logger LOGGER = LogManager.getLogger(Index.class);

    @RequestMapping
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/canvas")
    public String canvas(Model model) {
        return "canvas";
    }

    @RequestMapping("/canvas/submit")
    public SseEmitter onSubmit(@RequestParam("generators") String generators, @RequestParam("receivers") String receivers, @RequestParam("connections") String connections, Model model) {
        //todo:
        // SSE EMITTER CAN SEND ON ERRORS: IN FUNCTIONS
        // MODEL.ADDATTRIBUTE("ONERROR").
        // ADD A BANNER THAT SHOWS THESE ERRORS.
        CustomSseEmitter userSseEmitter = new CustomSseEmitter(Long.MAX_VALUE);
        CompletableFuture
                .runAsync(() -> {
                    try {
                        AsynchronousScreenListenersNotifier.onSubmit(generators, receivers, connections, userSseEmitter);
                    } catch (EmitterException emitterException) {
                        LOGGER.error("Emitter Exception: ", emitterException);
                        model.addAttribute("ERROR", emitterException.getMessage());
                    }
                });
        return userSseEmitter;
    }
}