package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.utils.CustomSseEmitter;
import com.iea.utils.EmitterException;

public interface ScreenListener {

    void onSubmit(Circuit circuit, String fileName, CustomSseEmitter emitter) throws EmitterException;
}
