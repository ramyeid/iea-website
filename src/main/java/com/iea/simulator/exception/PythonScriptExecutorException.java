package com.iea.simulator.exception;

public class PythonScriptExecutorException extends Exception {

    private static String ERROR_MESSAGE = "Error while executing python script";

    public PythonScriptExecutorException(Exception exception) {
        super(ERROR_MESSAGE, exception);
    }
}
