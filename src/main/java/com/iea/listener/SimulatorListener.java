package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverStatus;
import com.iea.simulator.CircuitSimulator;
import com.iea.simulator.NoGeneratorException;
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
        } catch (NoGeneratorException exception) {
            emitter.send(exception);
        }
    }
}