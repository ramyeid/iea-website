package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.simulator.exception.NoGeneratorException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AsynchronousScreenListenersNotifier {

    private static ConcurrentLinkedQueue<ScreenListener> screenListeners = new ConcurrentLinkedQueue();


    public static void addListener(ScreenListener screenListener) {
        screenListeners.add(screenListener);
    }

    //TODO REFACTOR.
    public static void onSubmit(Circuit circuit, SseEmitter emitter) {
        screenListeners.parallelStream().forEach(t-> {
            try {
                t.onSubmit(circuit, emitter);
            } catch (NoGeneratorException | IOException e) {
                //TODO LOG ERROR
                e.printStackTrace();
            }
        });
    }
}
