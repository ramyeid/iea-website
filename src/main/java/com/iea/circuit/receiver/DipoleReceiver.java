package com.iea.circuit.receiver;

import com.iea.circuit.pin.PinFactory;


public class DipoleReceiver extends Receiver {

    public DipoleReceiver(String id, Configuration configuration) {
        super(id, configuration, PinFactory.createPositivePin(), PinFactory.createNegativePin());
    }

    @Override
    public ReceiverStatus retrieveStatus(double amper, double volt) {
        return ReceiverStatus.OPTIMAL;
    }

}
