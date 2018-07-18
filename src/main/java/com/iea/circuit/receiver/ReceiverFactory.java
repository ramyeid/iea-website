package com.iea.circuit.receiver;

public class ReceiverFactory {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

    public DipoleReceiver createDipoleReceiver(String id, Configuration configuration) {
        return new DipoleReceiver(id, configuration);
    }
}
