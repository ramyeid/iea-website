package com.iea.circuit.receiver;

public class ReceiverFactory {

    public static Receiver createReceiver(ReceiverType receiverType, String id, ReceiverConfiguration receiverConfiguration){
        switch (receiverType)
        {
            case DIPOLE:
                return createDipoleReceiver(id, receiverConfiguration);
            case RESISTOR:
                return createResistor(id, receiverConfiguration);
            default:
                throw new RuntimeException();
        }
    }

    public static DipoleReceiver createDipoleReceiver(String id, ReceiverConfiguration receiverConfiguration) {
        return new DipoleReceiver(id, receiverConfiguration);
    }

    public static Resistor createResistor(String id, ReceiverConfiguration receiverConfiguration) {
        return new Resistor(id, receiverConfiguration);
    }
}
