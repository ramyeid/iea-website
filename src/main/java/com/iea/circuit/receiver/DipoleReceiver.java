package com.iea.circuit.receiver;

import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.status.ReceiverStatus;

import java.util.Objects;

public class DipoleReceiver extends Receiver {
    private final Pin positivePin;
    private final Pin negativePin;

    public DipoleReceiver(String id, Configuration configuration, Pin positivePin, Pin negativePin) {
        super(id, configuration);
        this.positivePin = positivePin;
        this.negativePin = negativePin;
    }

    public Pin getPositivePin() {
        return positivePin;
    }

    public Pin getNegativePin() {
        return negativePin;
    }

    @Override
    public ReceiverStatus retrieveStatus(double amper, double volt) {
        return ReceiverStatus.OPTIMAL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DipoleReceiver that = (DipoleReceiver) o;
        return Objects.equals(positivePin, that.positivePin) &&
                Objects.equals(negativePin, that.negativePin);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), positivePin, negativePin);
    }
}
