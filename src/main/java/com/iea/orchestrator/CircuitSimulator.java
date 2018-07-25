package com.iea.orchestrator;

import com.iea.ScreenListener;
import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverConfiguration;
import com.iea.circuit.receiver.ReceiverStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class CircuitSimulator implements ScreenListener {

    @Override
    public  Map<Receiver, ReceiverStatus> onStart(Circuit circuit) {
        if (circuit.getGenerator() == null || circuit.getReceivers() == null || circuit.getReceivers().isEmpty()) {
            return null;
        }

        List<Receiver> receiversInClosedCircuit = Validator.validate(circuit);

        if (receiversInClosedCircuit.isEmpty()) {
            return circuit.getReceivers().stream()
                    .collect(toMap(receiver->receiver, receiver->ReceiverStatus.OFF));
        }

        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), receiversInClosedCircuit);

        Map<Receiver, ReceiverStatus> receiverStatusToReceiver  = receiversInClosedCircuit
                .stream()
                .collect(toMap(r->r,
                            receiver -> retrieveStatus(receiver, amp)));

        receiverStatusToReceiver.putAll(circuit.getReceivers().stream()
                .filter(receiver -> !receiversInClosedCircuit.contains(receiver))
                .collect(toMap(receiver -> receiver, receiver -> ReceiverStatus.OFF)));

        return receiverStatusToReceiver;
    }

    protected static ReceiverStatus retrieveStatus(Receiver receiver, double amp) {
        ReceiverConfiguration receiverConfiguration = receiver.getConfiguration();
        double receiverVolt = amp * receiverConfiguration.getResistance();

        return receiver.retrieveStatus(amp, receiverVolt);
    }
    @Override
    public void onStop() {

    }
}