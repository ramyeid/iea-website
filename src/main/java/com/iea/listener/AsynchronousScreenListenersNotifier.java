package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.serializer.NoMatchingPinFoundException;
import com.iea.utils.CustomSseEmitter;
import com.iea.utils.EmitterException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.iea.serializer.Serializer.deSerialize;


public class AsynchronousScreenListenersNotifier {

    private final static ConcurrentLinkedQueue<ScreenListener> screenListeners = new ConcurrentLinkedQueue<>();

    public static void addListener(ScreenListener screenListener) {
        screenListeners.add(screenListener);
    }

    public static void onSubmit(String generators, String receivers, String connections, CustomSseEmitter emitter, String fileName) throws EmitterException {
        try {
            Circuit userCircuit = deSerialize(generators, receivers, connections);

            List<CompletableFuture<Void>> threads = asyncRunInExceptionHandling((screenListener -> screenListener.onSubmit(userCircuit, fileName, emitter)));
            threads.forEach(CompletableFuture::join);

            emitter.end();
        } catch (NoMatchingPinFoundException e) {
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