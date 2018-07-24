package com.iea.circuit.receiver;

public class ReceiverFactory {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

    public static DipoleReceiver createDipoleReceiver(String id, ReceiverConfiguration receiverConfiguration) {
        return new DipoleReceiver(id, receiverConfiguration);
    }
}
