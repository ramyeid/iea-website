package com.iea.circuit.model.pin;

import com.iea.circuit.model.Connection;
import com.iea.circuit.model.component.Component;
import com.iea.utils.Tuple;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

//TODO: List<Tuple<>> => Set<Tuple<>>
public class Pin {

    private final List<Tuple<Pin, Component>> connections;
    private final Type type;

    Pin(Type type) {
        connections = newArrayList();
        this.type = type;
    }

    public List<Tuple<Pin, Component>> getConnections() {
        return connections;
    }

    public Type getType() {
        return type;
    }

    private void connect(Tuple<Pin, Component> connection) {
        if (!connections.contains(connection)) {
            connections.add(connection);
        }
    }

    public static void connectComponents(Connection connection) {
        connection.getDestinationPin().connect(new Tuple<>(connection.getSourcePin(), connection.getSourceComponent()));
        connection.getSourcePin().connect(new Tuple<>(connection.getDestinationPin(), connection.getDestinationComponent()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pin pin = (Pin) o;
        ArrayList<Tuple<Pin, Component>> connectionsAsList = new ArrayList<>(pin.connections);
        boolean connectionsEquals = true;
        if (connections.size() != pin.connections.size()) {
            return false;
        }
        int i = 0;
        for (Tuple<Pin, Component> connection : connections) {
            if (!(connection.getFirst().type.equals(connectionsAsList.get(i).getFirst().type) && connection.getSecond().getId().equals(connectionsAsList.get(i).getSecond().getId()))) {
                connectionsEquals = false;
                break;
            }
            i++;
        }
        return connectionsEquals &&
                type == pin.type;
    }

    @Override
    public int hashCode() {
        int result = 1;

        for (Tuple<Pin, Component> connection : this.connections) {
            result = 31 * result + (connection.getFirst().type == null ? 0 : connection.getFirst().type.hashCode());
            result = 31 * result + (connection.getSecond().getId() == null ? 0 : connection.getSecond().getId().hashCode());
        }
        result = 31 * result + (this.getType() == null ? 0 : this.getType().hashCode());
        return result;
    }

    //TODO pin to string => connections.
    @Override
    public String toString() {
        return "Pin{" +
//                "connections=" + connections +
                "type=" + type +
                '}';
    }

    public enum Type {
        POSITIVE, NEGATIVE, NEUTRAL;
    }

}
