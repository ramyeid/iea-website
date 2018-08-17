/**
 * Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 * <p>
 * This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 * express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.simulator;

import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static java.util.stream.Collectors.toMap;


public class ReceiverStatusesGenerator {

    private static final Logger LOGGER = LogManager.getLogger(ReceiverStatusesGenerator.class);

    public static Map<Receiver, ReceiverStatus> generateReceiverStatuses(SimpleCircuit circuit) {
        double amp = AmpCalculator.calculateAmp(circuit);;

        return circuit.getReceivers().stream().collect(toMap(r -> r, receiver -> retrieveStatus(receiver, amp)));
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
