package com.iea.serializer;

public class NoMatchingPinFoundException extends Exception {

    private static String ERROR_MESSAGE = "No Matching pin found for: ";

    public NoMatchingPinFoundException(String pin) {
        super(ERROR_MESSAGE + pin);
    }
}
