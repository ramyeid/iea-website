package com.iea.circuit.model;

import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.pin.Pin;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

//TODO from List<Receiver> to Set<Receiver>
public class SimpleCircuit {

    private final Generator generator;
    private final List<Receiver> receivers;

    private SimpleCircuit(Generator generator, List<Receiver> receivers) {
        this.generator = checkNotNull(generator, "Generator can't be null");
        this.receivers = checkNotNull(receivers, "receivers can't be null");
    }

    public Generator getGenerator() {
        return generator;
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        SimpleCircuit circuit = (SimpleCircuit) o;

        List<Receiver> thatReceivers = circuit.getReceivers();
        if (thatReceivers == null && receivers != null || thatReceivers != null && receivers == null) {
            return false;
        } else if (thatReceivers != null) {
            if (thatReceivers.size() != receivers.size()) {
                return false;
            }
            for (Receiver thatReceiver : thatReceivers) {
                boolean didFindReceiver = false;
                for (Receiver receiver : receivers) {
                    if (thatReceiver.equals(receiver)) {
                        didFindReceiver = true;
                    }
                }
                if (!didFindReceiver) {
                    return false;
                }
            }
        }

        return Objects.equals(generator, circuit.generator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(generator, receivers);
    }

    @Override
    public String toString() {
        return "Circuit{" +
                "generator=" + generator +
                ", receivers=" + receivers +
                '}';
    }

    public static class Builder {
        private Generator generator;
        private List<Receiver> receivers = newArrayList();

        private Builder() {
        }

        public static SimpleCircuit.Builder newBuilder() {
            return new SimpleCircuit.Builder();
        }

        public final SimpleCircuit.Builder setGenerator(Generator generator) {
            this.generator = generator;
            return this;
        }


        public final SimpleCircuit.Builder addReceiver(Receiver receiver) {
            this.receivers.add(receiver);
            return this;
        }

        public SimpleCircuit.Builder connectComponents(Connection connect) {
            Pin.connectComponents(connect);
            return this;
        }

        public SimpleCircuit.Builder addAllReceivers(List<Receiver> receivers) {
            this.receivers.addAll(receivers);
            return this;
        }

        public SimpleCircuit build() {
            return new SimpleCircuit(generator, receivers);
        }
    }
}