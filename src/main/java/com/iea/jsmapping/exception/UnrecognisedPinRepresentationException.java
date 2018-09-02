package com.iea.jsmapping.exception;

public class UnrecognisedPinRepresentationException extends Exception {

    private static String ERROR_MESSAGE = "Unrecognised pin representation: ";

    public UnrecognisedPinRepresentationException(String pinRepresentation) { super(ERROR_MESSAGE + pinRepresentation);}

}
