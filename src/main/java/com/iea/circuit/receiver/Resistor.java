package com.iea.circuit.receiver;

import com.iea.circuit.pin.PinFactory;

public class Resistor extends Receiver{

    public Resistor(String id, ReceiverConfiguration configuration) {
        super(id, configuration, PinFactory.createNeutralPin(), PinFactory.createNeutralPin());
    }

    @Override
    public ReceiverStatus retrieveStatus(double amper, double volt) {
        return ReceiverStatus.OPTIMAL;
    }

}
