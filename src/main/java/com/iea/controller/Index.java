package com.iea.controller;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;
import com.iea.serializer.Serializer;
import com.iea.simulator.CircuitSimulator;
import com.iea.simulator.exception.NoGeneratorException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import static com.iea.serializer.Serializer.deserialize;
import static com.iea.serializer.Serializer.serialize;

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
        try {
            Circuit circuit = serialize(generators, receivers, connections);
            Map<Receiver, ReceiverStatus> simulatedResult = CircuitSimulator.simulate(circuit);
            return deserialize(simulatedResult);
        } catch(NoGeneratorException e) {
            return "ERROR: " + e.getMessage();
        }
    }
}