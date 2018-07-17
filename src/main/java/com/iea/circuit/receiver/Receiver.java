package com.iea.circuit.receiver;

import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.status.ReceiverStatus;

import java.util.Objects;

public abstract class Receiver extends Component {
    private final Configuration configuration;

    public Receiver(String id, double optimalAmper, double minVolt, double maxVolt, double resistance) {
        super(id);
        this.optimalAmper = optimalAmper;
        this.minVolt = minVolt;
        this.maxVolt = maxVolt;
        this.resistance = resistance;
    }

    public abstract ReceiverStatus retrieveStatus(double amper, double volt);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Receiver receiver = (Receiver) o;
        return Double.compare(receiver.optimalAmper, optimalAmper) == 0 &&
                Double.compare(receiver.minVolt, minVolt) == 0 &&
                Double.compare(receiver.maxVolt, maxVolt) == 0 &&
                Double.compare(receiver.resistance, resistance) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), optimalAmper, minVolt, maxVolt, resistance);
    }
}
