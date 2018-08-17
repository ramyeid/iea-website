package com.iea.circuit.model;

import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.pin.Pin;

import java.util.Objects;

public class Connection {

    private final Component sourceComponent;
    private final Component destinationComponent;
    private final Pin sourcePin;
    private final Pin destinationPin;

    public Connection(Component sourceComponent, Pin sourcePin, Component destinationComponent, Pin destinationPin) {
        this.sourceComponent = sourceComponent;
        this.destinationComponent = destinationComponent;
        this.sourcePin = sourcePin;
        this.destinationPin = destinationPin;
    }


    public Component getSourceComponent() {
        return sourceComponent;
    }

    public Component getDestinationComponent() {
        return destinationComponent;
    }

    public Pin getSourcePin() {
        return sourcePin;
    }

    public Pin getDestinationPin() {
        return destinationPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(sourceComponent, that.sourceComponent) &&
                Objects.equals(destinationComponent, that.destinationComponent) &&
                Objects.equals(sourcePin, that.sourcePin) &&
                Objects.equals(destinationPin, that.destinationPin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceComponent, destinationComponent, sourcePin, destinationPin);
    }
}
