package com.iea.circuit;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.Receiver;
import com.iea.utils.Tuple;
import org.eclipse.jdt.internal.compiler.parser.RecoveredImport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Circuit {
    private final Generator generator;
    private final List<Receiver> receivers;

    public Circuit(Generator generator, List<Receiver> receivers) {
        this.generator = generator;
        this.receivers = receivers;
    }

    public static class Builder {
        private Generator generator;
        private List<Receiver> receivers;
        private static Builder builder;

        public static Builder newBuilder() {
            if (builder == null) {
                builder = new Builder();
            }
            return builder;
        }

        public static Builder setGenerator(Generator generator){
            builder.generator = generator;
            return builder;
        }

        public static Builder addReceiver (Receiver receiver) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circuit circuit = (Circuit) o;
        return Objects.equals(generator, circuit.generator) &&
                Objects.equals(receivers, circuit.receivers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(generator, receivers);
    }
}




