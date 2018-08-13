package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.serializer.exception.NoMatchingPinFoundException;
import com.iea.utils.CustomSseEmitter;
import com.iea.utils.EmitterException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

import static com.iea.serializer.Serializer.deSerialize;


public class AsynchronousScreenListenersNotifier {

    private final static ConcurrentLinkedQueue<ScreenListener> screenListeners = new ConcurrentLinkedQueue<>();

    public static void addListener(ScreenListener screenListener) {
        screenListeners.add(screenListener);
    }

    public static void onSubmit(String generators, String receivers, String connections, CustomSseEmitter emitter) throws EmitterException {
        try {
            List<CompletableFuture<Void>> listenersThreads = new ArrayList<>();

            Circuit userCircuit = deSerialize(generators, receivers, connections);
            screenListeners.forEach(t -> listenersThreads.add(asyncRunInExceptionHandling(() -> t.onSubmit(userCircuit, emitter))));

            listenersThreads.forEach(CompletableFuture::join);
            emitter.end();
        } catch (NoMatchingPinFoundException e) {
            emitter.send(e);
        }
    }

    private static CompletableFuture<Void> asyncRunInExceptionHandling(EmitterExceptionThrowingSupplier supplier) {
        return CompletableFuture.runAsync(supplier::get);
    }

    @FunctionalInterface
    public interface EmitterExceptionThrowingSupplier extends Supplier<Void> {

        @Override
        default Void get() {
            try {
                getThrows();
                return null;
            } catch (final EmitterException e) {
                throw new RuntimeException(e);
            }
        }

        void getThrows() throws EmitterException;
    }

}
