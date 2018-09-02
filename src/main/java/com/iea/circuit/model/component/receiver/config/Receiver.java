package com.iea.circuit.model.component.receiver.config;

import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.pin.Pin;

import java.util.Objects;

public abstract class Receiver extends Component {

    protected final ReceiverConfiguration configuration;

    protected Receiver(String id, ReceiverConfiguration receiverConfiguration, Pin firstPin, Pin secondPin) {
        super(id, firstPin, secondPin);
        this.configuration = receiverConfiguration;
    }

    public ReceiverConfiguration getConfiguration() {
        return configuration;
    }

    public abstract ReceiverStatus retrieveStatus(double amp, double volt);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Receiver receiver = (Receiver) o;
        return Objects.equals(configuration, receiver.configuration);
    }
}
