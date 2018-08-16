package com.iea.circuit.receiver;

import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverConfiguration;
import com.iea.circuit.receiver.config.ReceiverStatus;

import java.util.List;

import static java.util.Arrays.asList;

public class Resistor extends Receiver {

    Resistor(String id, ReceiverConfiguration configuration) {
        super(id, configuration, PinFactory.createNeutralPin(), PinFactory.createNeutralPin());
    }

    @Override
    public List<Pin> getPins() {
        return asList(getFirstPin(), getSecondPin());
    }

    @Override
    public ReceiverStatus retrieveStatus(double amper, double volt) {
        return ReceiverStatus.OPTIMAL;
    }

    @Override
    public String toString() {
        return "Resistor{" +
                "configuration=" + configuration +
                ", id='" + id + '\'' +
                ", firstPin=" + firstPin +
                ", secondPin=" + secondPin +
                '}';
    }
}
