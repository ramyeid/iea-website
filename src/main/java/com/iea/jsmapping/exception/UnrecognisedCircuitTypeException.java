package com.iea.jsmapping.exception;

public class UnrecognisedCircuitTypeException extends Exception {

    private static String ERROR_MESSAGE = "Unrecognised circuit type received from client: ";

    public UnrecognisedCircuitTypeException(String circuitTypeString) {
        super(ERROR_MESSAGE + circuitTypeString);
    }
}
