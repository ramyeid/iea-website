package com.iea.circuit.creator.exception;

public class DuplicateReceiversInCircuitsException extends Exception {

    private static String ERROR_MESSAGE = "Some circuits share the same receivers: ";

    public DuplicateReceiversInCircuitsException(String commonReceiversIds) {
        super(ERROR_MESSAGE + commonReceiversIds);
    }
}