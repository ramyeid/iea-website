package com.iea.circuit;

import com.iea.circuit.pin.Pin;

import java.util.List;
import java.util.Objects;


public abstract class Component {

    protected final String id;
    private final Pin firstPin;
    private final Pin secondPin;

    public Component(String id, Pin firstPin, Pin secondPin) {
        this.id = id;
        this.firstPin = firstPin;
        this.secondPin = secondPin;
    }

    public String getId() {
        return id;
    }

    public Pin getFirstPin() {
        return firstPin;
    }

    public abstract List<Pin> getPins();

    public Pin getSecondPin() {
        return secondPin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Component component = (Component) o;
        return Objects.equals(id, component.id) &&
                Objects.equals(firstPin, component.firstPin) &&
                Objects.equals(secondPin, component.secondPin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstPin, secondPin);
    }

    @Override
    public String toString() {
        return "Component{" +
                "id='" + id + '\'' +
                '}';
    }
}
