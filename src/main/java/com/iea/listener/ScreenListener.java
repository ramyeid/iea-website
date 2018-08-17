package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.utils.emitter.CustomSseEmitter;
import com.iea.utils.emitter.EmitterException;

public interface ScreenListener {

    void onSubmit(Circuit circuit, String fileName, CustomSseEmitter emitter) throws EmitterException;
}
