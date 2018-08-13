package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;
import com.iea.simulator.CircuitSimulator;
import com.iea.simulator.exception.NoGeneratorException;
import com.iea.utils.CustomSseEmitter;
import com.iea.utils.EmitterException;

import java.util.Map;

import static com.iea.serializer.Serializer.serialize;

public class SimulatorListener implements ScreenListener {

    @Override
    public void onSubmit(Circuit circuit, CustomSseEmitter emitter) throws EmitterException {
        try {
            Map<Receiver, ReceiverStatus> simulatedResult = CircuitSimulator.simulate(circuit);
            String serializedMap = serialize(simulatedResult);
            emitter.send(serializedMap);
        } catch (NoGeneratorException e) {
            emitter.send(e);
        }
    }
}