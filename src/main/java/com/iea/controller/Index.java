package com.iea.controller;

import com.iea.listener.AsynchronousScreenListenersNotifier;
import com.iea.utils.emitter.CustomSseEmitter;
import com.iea.utils.emitter.EmitterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;


//TODO ADD A BANNER WHEN ERROR ATTRIBUTE IS FILLED.
//todo:
// SSE EMITTER CAN SEND ON ERRORS: IN FUNCTIONS
// MODEL.ADDATTRIBUTE("ONERROR").
// ADD A BANNER THAT SHOWS THESE ERRORS.
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
    public SseEmitter onSubmit(@RequestParam("type") String circuitType, @RequestParam("generators") String generators, @RequestParam("receivers") String receivers, @RequestParam("connections") String connections, @RequestParam("fileName") String fileName, Model model) {
        CustomSseEmitter userSseEmitter = new CustomSseEmitter();
        runAsync(() -> AsynchronousScreenListenersNotifier.onSubmit(circuitType, generators, receivers, connections, userSseEmitter, fileName), model);
        return userSseEmitter;
    }

    @RequestMapping("/canvas/savePythonFile")
    public void onSavePython(@RequestParam("pythonCode") String pythonCode, @RequestParam("pythonName") String fileName, Model model) {
        CompletableFuture.runAsync(() -> {
            try {
                String pathToNewPythonFile = new StringJoiner(File.separator).add("src").add("main").add("resources").add("python-lib").add(fileName + ".py").toString();
                Files.write(Paths.get(pathToNewPythonFile), pythonCode.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                LOGGER.error("Error while saving the python file");
                model.addAttribute("ERROR", e.getMessage());
            }
        });
    }

    private void runAsync(SupplierWithEmitterException supplier, Model model) {
        CompletableFuture.runAsync(() -> {
            try {
                supplier.get();
            } catch (EmitterException emitterException) {
                LOGGER.error("Emitter Exception: ", emitterException);
                model.addAttribute("ERROR", emitterException.getMessage());
            }
        });
    }

    @FunctionalInterface
    private interface SupplierWithEmitterException {
        void get() throws EmitterException;
    }
}