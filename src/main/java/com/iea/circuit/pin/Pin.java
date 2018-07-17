package com.iea.circuit.pin;

import com.iea.circuit.Component;
import com.iea.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pin {
    private final List<Tuple<Pin, Component>> connections;
    private final PinType pinType;

    Pin(PinType pinType){
        connections = new ArrayList<>();
        this.pinType = pinType;
    }

    public List<Tuple<Pin, Component>> getConnections() {
        return connections;
    }

    public PinType getPinType() {
        return pinType;
    }

    private void connect(Component component, Pin pin) {
        connections.add(new Tuple<>(pin, component));
    }


    public static void connectComponents(Tuple<Pin, Component> component, Tuple<Pin, Component> component2) {
        component.getFirst().connect(component2.getSecond(), component2.getFirst());
        component2.getFirst().connect(component.getSecond(), component.getFirst());

    }

    enum PinType {
        POSITIVE, NEGATIVE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pin pin = (Pin) o;
        return Objects.equals(connections, pin.connections) &&
                pinType == pin.pinType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(connections, pinType);
    }
}
