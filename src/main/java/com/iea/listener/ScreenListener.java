package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.simulator.exception.NoGeneratorException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface ScreenListener {

    void onSubmit(Circuit circuit, SseEmitter emitter) throws Exception;
}
