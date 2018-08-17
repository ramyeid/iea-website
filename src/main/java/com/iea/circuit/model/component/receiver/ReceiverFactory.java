package com.iea.circuit.model.component.receiver;

import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverType;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReceiverFactory {

    public static Receiver createReceiver(String id, ReceiverType receiverType, ReceiverConfiguration receiverConfiguration) {
        checkNotNull(receiverType, "ReceiverType can not be null");
        checkNotNull(id, "Receiver id can not be null");
        checkNotNull(receiverConfiguration, "Receiver Configuration can not be null");
        switch (receiverType) {
            case DIPOLE:
                return createDipoleReceiver(id, receiverConfiguration);
            case RESISTOR:
                return createResistor(id, receiverConfiguration);
            default:
                return createDipoleReceiver(id, receiverConfiguration);
        }
    }

    private static DipoleReceiver createDipoleReceiver(String id, ReceiverConfiguration receiverConfiguration) {
        return new DipoleReceiver(id, receiverConfiguration);
    }

    private static Resistor createResistor(String id, ReceiverConfiguration receiverConfiguration) {
        return new Resistor(id, receiverConfiguration);
    }
}
