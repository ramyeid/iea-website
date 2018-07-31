package com.iea;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;
import com.iea.orchestrator.exception.NoGeneratorException;

import java.util.Map;

public interface ScreenListener {

    Map<Receiver, ReceiverStatus> onStart(Circuit circuit) throws NoGeneratorException;

    void onStop();
}