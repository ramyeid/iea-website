package com.iea.utils.emitter;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class CustomSseEmitter extends SseEmitter {

    public CustomSseEmitter() {
        super(Long.MAX_VALUE);
    }

    public void send(String message) throws EmitterException {
        try {
            super.send(message);
        } catch (IOException ioException) {
            throw new EmitterException(ioException);
        }
    }

    public void send(Exception exception) throws EmitterException {
        try {
            super.send(onErrorMessage(exception));
            end();
        } catch (IOException ioException) {
            throw new EmitterException(ioException);
        }
    }

    public void end() throws EmitterException {
        try {
            super.send(onEndMessage());
        } catch (IOException ioException) {
            throw new EmitterException(ioException);
        }
    }

    private String onEndMessage() {
        return "end";
    }

    private static String onErrorMessage(Exception e) {
        return "Error: " + e.getMessage();
    }
}
