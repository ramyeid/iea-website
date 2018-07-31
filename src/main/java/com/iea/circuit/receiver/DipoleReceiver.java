package com.iea.circuit.receiver;

import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;

import java.util.List;

import static com.iea.circuit.receiver.ReceiverStatus.*;
import static java.util.Arrays.asList;


public class DipoleReceiver extends Receiver {

    private static final double MIN_VOLT_TOLERANCE_RATE = 0.9;
    private static final double MAX_VOLT_TOLERANCE_RATE = 1.2;
    private static final double MIN_AMPER_TOLERANCE_RATE = 0.9;
    private static final double MAX_AMPER_TOLERANCE_RATE = 1.5;

    public DipoleReceiver(String id, ReceiverConfiguration configuration) {
        super(id, configuration, PinFactory.createPositivePin(), PinFactory.createNegativePin());
    }

    @Override
    public List<Pin> getPins() {
        return asList(getFirstPin(), getSecondPin());
    }

    public Pin getPositivePin() {
        return getFirstPin();
    }

    public Pin getNegativePin() {
        return getSecondPin();
    }

    @Override
    public ReceiverStatus retrieveStatus(double amp, double volt) {
        if (volt/configuration.getMinVolt() < MIN_VOLT_TOLERANCE_RATE ) {
            return OFF;
        } else if (volt/configuration.getMaxVolt() > MAX_VOLT_TOLERANCE_RATE) {
            return DAMAGED;
        } else {
            if (amp/configuration.getOptimalAmper() < MIN_AMPER_TOLERANCE_RATE) {
                return LOW;
            } else if (amp/configuration.getOptimalAmper() > MAX_AMPER_TOLERANCE_RATE) {
                return DAMAGED;
            } else {
                return OPTIMAL;
            }
        }
    }

}
