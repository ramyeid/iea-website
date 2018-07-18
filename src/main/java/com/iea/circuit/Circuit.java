/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.Receiver;
import com.iea.utils.Tuple;


public class Circuit {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields
    //~ ----------------------------------------------------------------------------------------------------------------

    private final Generator generator;
    private final List<Receiver> receivers;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors
    //~ ----------------------------------------------------------------------------------------------------------------

    public Circuit(Generator generator, List<Receiver> receivers) {
        this.generator = generator;
        this.receivers = receivers;
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        Circuit circuit = (Circuit) o;
        return Objects.equals(generator, circuit.generator) && Objects.equals(receivers, circuit.receivers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generator, receivers);
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Nested Classes
    //~ ----------------------------------------------------------------------------------------------------------------

    public static class Builder {
        private static Builder builder;
        private Generator generator;
        private List<Receiver> receivers;

        public static Builder newBuilder() {
            if (builder == null) {
                builder = new Builder();
            }
            return builder;
        }

        public static Builder setGenerator(Generator generator) {
            builder.generator = generator;
            return builder;
        }

        public static Builder addReceiver(Receiver receiver) {
            if (builder.receivers == null) {
                builder.receivers = new ArrayList<>();
            }
            builder.receivers.add(receiver);
            return builder;
        }

        public static Builder connectComponents(Tuple<Pin, Component> pinComponentTuple, Tuple<Pin, Component> pinComponentTuple2) {
            Pin.connectComponents(pinComponentTuple, pinComponentTuple2);
            return builder;
        }

        public Circuit build() {
            return new Circuit(builder.generator, builder.receivers);
        }
    }
}
