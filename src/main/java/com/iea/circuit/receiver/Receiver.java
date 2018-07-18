package com.iea.circuit.receiver;

import com.iea.circuit.Component;
import com.iea.circuit.receiver.status.ReceiverStatus;

import java.util.Objects;

public abstract class Receiver extends Component {
    private final Configuration configuration;

    public Receiver(String id, Configuration configuration) {
        super(id);
        this.configuration = configuration;
    }

    public abstract ReceiverStatus retrieveStatus(double amper, double volt);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Receiver receiver = (Receiver) o;
        return Objects.equals(configuration, receiver.configuration);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), configuration);
    }
}
