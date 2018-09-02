package com.iea.circuit.utils.exception;

public class MatchingComponentNotFoundException extends Exception {

    private static String ERROR_MESSAGE = "The following component (ID) could not be found in search:";

    public MatchingComponentNotFoundException(String componentId) {
        super(ERROR_MESSAGE + componentId);
    }
}
