/**
 * Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 * <p>
 * This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 * express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.pin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.iea.circuit.Component;
import com.iea.utils.Tuple;


public class Pin {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Enums
    //~ ----------------------------------------------------------------------------------------------------------------

    public enum Type {
        POSITIVE, NEGATIVE, NEUTRAL
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields
    //~ ----------------------------------------------------------------------------------------------------------------

    private final List<Tuple<Pin, Component>> connections;
    private final Type type;
    private boolean checked;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors
    //~ ----------------------------------------------------------------------------------------------------------------

    Pin(Type type) {
        connections = new ArrayList<>();
        this.type = type;
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

    public static void connectComponents(Tuple<Pin, Component> component, Tuple<Pin, Component> component2) {
        component.getFirst().connect(component2.getSecond(), component2.getFirst());
        component2.getFirst().connect(component.getSecond(), component.getFirst());

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<Tuple<Pin, Component>> getConnections() {
        return connections;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        Pin pin = (Pin) o;
        return Objects.equals(connections, pin.connections) && (type == pin.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connections, type);
    }

    private void connect(Component component, Pin pin) {
        connections.add(new Tuple<>(pin, component));
    }
}
