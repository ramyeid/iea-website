package com.iea.jsmapping.exception;

public class InvalidConnectionsStringException extends Exception {

    private static String ERROR_MESSAGE = "Invalid connections string received from client: ";

    public InvalidConnectionsStringException(String connections) {
        super(ERROR_MESSAGE + connections);
    }
}
