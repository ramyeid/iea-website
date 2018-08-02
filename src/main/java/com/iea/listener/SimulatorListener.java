package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;
import com.iea.simulator.CircuitSimulator;
import com.iea.simulator.exception.NoGeneratorException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

import static com.iea.serializer.Serializer.serialize;

public class SimulatorListener implements ScreenListener {

    @Override
    public void onSubmit(Circuit circuit, SseEmitter emitter) throws NoGeneratorException, IOException {
        Map<Receiver, ReceiverStatus> simulatedResult = CircuitSimulator.simulate(circuit);
        String serializedMap = serialize(simulatedResult);
        emitter.send(serializedMap);
    }
}