package com.iea.circuit.receiver;

import com.iea.circuit.receiver.exception.UnrecognisedReceiverTypeError;

public class ReceiverFactory {

    public static Receiver createReceiver(ReceiverType receiverType, String id, ReceiverConfiguration receiverConfiguration) throws UnrecognisedReceiverTypeError{
        switch (receiverType)
        {
            case DIPOLE:
                return createDipoleReceiver(id, receiverConfiguration);
            case RESISTOR:
                return createResistor(id, receiverConfiguration);
            default:
                throw new UnrecognisedReceiverTypeError();
        }
    }

    private static DipoleReceiver createDipoleReceiver(String id, ReceiverConfiguration receiverConfiguration) {
        return new DipoleReceiver(id, receiverConfiguration);
    }

    private static Resistor createResistor(String id, ReceiverConfiguration receiverConfiguration) {
        return new Resistor(id, receiverConfiguration);
    }
}
