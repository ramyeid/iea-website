package com.iea.utils.emitter;

import java.io.IOException;

public class EmitterException extends Throwable {
    public EmitterException(IOException e) {
        super(e.getMessage());
    }
}
