package com.iea.listener;

import com.iea.circuit.Circuit;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import com.iea.jsmapping.Serializer;
import com.iea.utils.emitter.CustomSseEmitter;
import com.iea.utils.emitter.EmitterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


public class SimulatorListener implements ScreenListener {

    private static final Logger LOGGER = LogManager.getLogger(SimulatorListener.class);

    @Override
    public void onSubmit(Circuit circuit, String fileName, CustomSseEmitter emitter) throws EmitterException {
        LOGGER.info("------------ CircuitSimulator Started ------------ ");

        if (circuit.getReceivers().isEmpty()) {
            LOGGER.info("Circuit is empty, returning OFF status for all receivers");
            return;
        }

        Map<Receiver, ReceiverStatus> receiverStatusToReceiver = circuit.generateReceiverStatuses();

        LOGGER.info("ReceiversStatuses: " + receiverStatusToReceiver);
        LOGGER.info("------------ CircuitSimulator Finished ------------ ");

        String serializedMap = Serializer.serialize(receiverStatusToReceiver);

        emitter.send(serializedMap);
    }
}