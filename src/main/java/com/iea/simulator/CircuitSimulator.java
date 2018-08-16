package com.iea.simulator;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverConfiguration;
import com.iea.circuit.receiver.config.ReceiverStatus;
import com.iea.simulator.exception.NoGeneratorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class CircuitSimulator {


    private static final Logger LOGGER = LogManager.getLogger(CircuitSimulator.class);

    public static Map<Receiver, ReceiverStatus> simulate(Circuit circuit) throws NoGeneratorException {
        LOGGER.info("------------ CircuitSimulator Started ------------ ");
        if (circuit.getGenerator() == null || circuit.getReceivers() == null || circuit.getReceivers().isEmpty()) {
            LOGGER.info("No Generator In Circuit Exception");
            throw new NoGeneratorException();
        }

        List<Receiver> receiversInClosedCircuit = Validator.validate(circuit);

        if (receiversInClosedCircuit.isEmpty()) {
            LOGGER.info("Circuit is empty, returning OFF status for all receivers");
            return circuit.getReceivers().stream()
                    .collect(toMap(receiver -> receiver, receiver -> ReceiverStatus.OFF));
        }

        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), receiversInClosedCircuit);

        Map<Receiver, ReceiverStatus> receiverStatusToReceiver = receiversInClosedCircuit
                .stream()
                .collect(toMap(r -> r,
                        receiver -> retrieveStatus(receiver, amp)));

        receiverStatusToReceiver.putAll(circuit.getReceivers().stream()
                .filter(receiver -> !receiversInClosedCircuit.contains(receiver))
                .collect(toMap(receiver -> receiver, receiver -> ReceiverStatus.OFF)));

        LOGGER.info("ReceiversStatuses: " + receiverStatusToReceiver);
        LOGGER.info("------------ CircuitSimulator Finished ------------ ");
        return receiverStatusToReceiver;
    }

    private static ReceiverStatus retrieveStatus(Receiver receiver, double amp) {
        ReceiverConfiguration receiverConfiguration = receiver.getConfiguration();
        double receiverVolt = amp * receiverConfiguration.getResistance();
        LOGGER.info("Receiver: " + receiver + " - Volt:" + receiverVolt);
        ReceiverStatus status = receiver.retrieveStatus(amp, receiverVolt);
        LOGGER.info("Receiver: " + receiver + " - Status: " + status);

        return status;
    }
}