package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.utils.CustomSseEmitter;
import com.iea.utils.EmitterException;

import java.io.IOException;

public interface ScreenListener {

    void onSubmit(Circuit circuit, CustomSseEmitter emitter) throws EmitterException;
}
