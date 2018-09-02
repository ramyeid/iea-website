package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.circuit.creator.CircuitCreator;
import com.iea.circuit.creator.exception.DuplicateReceiversInCircuitsException;
import com.iea.circuit.creator.exception.InvalidNumberOfGeneratorException;
import com.iea.circuit.creator.factory.CircuitCreatorFactory;
import com.iea.circuit.utils.exception.MatchingComponentNotFoundException;
import com.iea.jsmapping.exception.InvalidConnectionsStringException;
import com.iea.jsmapping.exception.NoMatchingPinFoundException;
import com.iea.jsmapping.exception.UnrecognisedCircuitTypeException;
import com.iea.utils.emitter.CustomSseEmitter;
import com.iea.utils.emitter.EmitterException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.iea.jsmapping.JavascriptMapping.stringToCircuitType;

public class AsynchronousScreenListenersNotifier {

    private final static ConcurrentLinkedQueue<ScreenListener> screenListeners = new ConcurrentLinkedQueue<>();

    public static void addListener(ScreenListener screenListener) {
        screenListeners.add(screenListener);
    }

    public static void onSubmit(String circuitType, String generators, String receivers, String connections, CustomSseEmitter emitter, String fileName) throws EmitterException {
        try {
            CircuitCreator circuitCreator = CircuitCreatorFactory.create(stringToCircuitType(circuitType));
            Circuit userCircuit = circuitCreator.create(generators, receivers, connections);

            List<CompletableFuture<Void>> threads = asyncRunInExceptionHandling((screenListener -> screenListener.onSubmit(userCircuit, fileName, emitter)));
            threads.forEach(CompletableFuture::join);

            emitter.end();
        } catch (DuplicateReceiversInCircuitsException | NoMatchingPinFoundException | InvalidNumberOfGeneratorException | InvalidConnectionsStringException | MatchingComponentNotFoundException | UnrecognisedCircuitTypeException e) {
            emitter.send(e);
        }
    }

    private static List<CompletableFuture<Void>> asyncRunInExceptionHandling(AsyncEmitterExceptionThrowingConsumer consumer) {
        List<CompletableFuture<Void>> threads = new ArrayList<>();
        screenListeners.forEach(t -> threads.add(consumer.asyncAccept(t)));
        return threads;
    }

    @FunctionalInterface
    private interface AsyncEmitterExceptionThrowingConsumer {

        default CompletableFuture<Void> asyncAccept(ScreenListener screenListener) {
            return CompletableFuture.runAsync(() -> {
                try {
                    doAccept(screenListener);
                } catch (final EmitterException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        void doAccept(ScreenListener screenListener) throws EmitterException;
    }
}