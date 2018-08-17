package com.iea.circuit.model.component.receiver;

import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import com.iea.circuit.model.pin.Pin;
import com.iea.circuit.model.pin.PinFactory;

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
