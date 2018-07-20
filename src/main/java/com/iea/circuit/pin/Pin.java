/**
<<<<<<< HEAD
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
=======
 * Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 * <p>
 * This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 * express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
>>>>>>> b44adb4cda296fe76b4c8a245b69d5bac9707fdc
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

    Pin(Type type) {
        connections = new ArrayList<>();
        this.type = type;
    }

    public List<Tuple<Pin, Component>> getConnections() {
        return connections;
    }

    public Type getType() {
        return type;
    }

    private void connect(Component component, Pin pin) {
        connections.add(new Tuple<>(pin, component));
    }

    public static void connectComponents(Tuple<Pin, Component> component, Tuple<Pin, Component> component2) {
        component.getFirst().connect(component2.getSecond(), component2.getFirst());
        component2.getFirst().connect(component.getSecond(), component.getFirst());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pin pin = (Pin) o;
        boolean connectionsEquals = true;
        if (connections.size() != pin.connections.size()) return false;
        int i=0;
        for (Tuple<Pin,Component> connection : connections){
            if (!(connection.getFirst().type.equals(pin.connections.get(i).getFirst().type) && connection.getSecond().getId().equals(pin.connections.get(i).getSecond().getId()))){
                connectionsEquals=false;
                break;}

            i++;
        }
                //this.connections.equals(pin.connections) &&
        return  connectionsEquals &&
                type == pin.type;
    }

    @Override
    public int hashCode() {
//        return Objects.hash(connections, type);
        /*if (this == null)
            return 0;*/

        int result = 1;

        for (Tuple<Pin,Component> connection : this.connections){
            result = 31 * result + (connection.getFirst().type == null ? 0 : connection.getFirst().type.hashCode());
            result = 31 * result + (connection.getSecond().getId() == null ? 0 : connection.getSecond().getId().hashCode());}
        result = 31 * result + (this.getType() == null ? 0 : this.getType().hashCode());

        return result;
    }
}
