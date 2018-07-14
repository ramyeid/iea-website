package com.iea.circuit.receiver;

import java.util.Objects;

import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;


public abstract class Receiver extends Component {

    private final ReceiverConfiguration receiverConfiguration;

    Receiver(String id, ReceiverConfiguration receiverConfiguration, Pin firstPin, Pin secondPin) {
        super(id, firstPin, secondPin);
        this.receiverConfiguration = receiverConfiguration;

    }

    public abstract ReceiverStatus retrieveStatus(double amper, double volt);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Receiver receiver = (Receiver) o;
        return Objects.equals(receiverConfiguration, receiver.receiverConfiguration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), receiverConfiguration);
    }
}
