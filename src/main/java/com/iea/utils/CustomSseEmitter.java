package com.iea.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class CustomSseEmitter extends SseEmitter {

    public CustomSseEmitter(long timeout) {
        super(timeout);
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
        } catch (IOException ioException) {
            throw new EmitterException(ioException);
        }
    }

    private static String onErrorMessage(Exception e) {
        return "Error: " + e.getMessage();
    }
}
