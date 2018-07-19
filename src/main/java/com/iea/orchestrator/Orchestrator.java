package com.iea.orchestrator;

import com.iea.PresenterNotifier;
import com.iea.ScreenListener;
import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;

import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;

public class Orchestrator implements ScreenListener {

    PresenterNotifier presenterNotifier;

    public Orchestrator(PresenterNotifier presenterNotifier) {
        this.presenterNotifier = presenterNotifier;
    }

    @Override
    public boolean onStart(Circuit circuit) {
        Map<Receiver, ReceiverStatus> receiverStatusToReceiver = newHashMap();

        List<Component> componentsInClosedCircuit = Validator.validate(circuit);

        if (componentsInClosedCircuit.isEmpty()) {
            presenterNotifier.onError();
            return false;
        }

        circuit.getReceivers().stream()
                .filter(receiver -> !componentsInClosedCircuit.contains(receiver))
                .forEach(t -> receiverStatusToReceiver.put(t, ReceiverStatus.OFF));






        presenterNotifier.onResults(receiverStatusToReceiver);
        return true;
    }

    @Override
    public void onStop() {

    }
}
