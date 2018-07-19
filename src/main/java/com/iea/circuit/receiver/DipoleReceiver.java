package com.iea.circuit.receiver;

import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;


public class DipoleReceiver extends Receiver {

    public DipoleReceiver(String id, Configuration configuration) {
        super(id, configuration, PinFactory.createPositivePin(), PinFactory.createNegativePin());
    }

    public Pin getPositionPin() {
        return getFirstPin();
    }

    public Pin getNegativePin() {
        return getSecondPin();
    }

    @Override
    public ReceiverStatus retrieveStatus(double amper, double volt) {
        return ReceiverStatus.OPTIMAL;
    }

}
