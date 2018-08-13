package com.iea.simulator.exception;

public class NoGeneratorException extends Exception {

    private static String ERROR_MESSAGE = "No Generator found in circuit";

    public NoGeneratorException() {
        super(ERROR_MESSAGE);
    }
}
