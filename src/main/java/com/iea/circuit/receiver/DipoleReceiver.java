package com.iea.circuit.receiver;

import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;


public class DipoleReceiver extends Receiver {

    public DipoleReceiver(String id, ReceiverConfiguration configuration) {
        super(id, configuration, PinFactory.createPositivePin(), PinFactory.createNegativePin());
    }

    public Pin getPositivePin() {
        return getFirstPin();
    }

    public Pin getNegativePin() {
        return getSecondPin();
    }

    @Override
    public ReceiverStatus retrieveStatus(double amp, double volt) {
        double toleranceRate = 0.5;
        if (volt < configuration.getMinVolt()) {
            return ReceiverStatus.OFF;
        }
        if (volt <= configuration.getMaxVolt() && volt >= configuration.getMinVolt()) {
            if (amp < configuration.getOptimalAmper() - configuration.getOptimalAmper() * toleranceRate) {
                return ReceiverStatus.LOW;
            }
            if (amp <= configuration.getOptimalAmper() + configuration.getOptimalAmper() * toleranceRate)
                return ReceiverStatus.OPTIMAL;
            return ReceiverStatus.DAMAGED;
        }
        if (volt > configuration.getMaxVolt())
            return ReceiverStatus.DAMAGED;

        return ReceiverStatus.OFF;
    }

}
