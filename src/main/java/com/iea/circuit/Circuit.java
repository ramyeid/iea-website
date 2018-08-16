package com.iea.circuit;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

//TODO LIST<RECEIVER> TO SET<RECEIVER>
public class Circuit {

    private final Generator generator;
    private final List<Receiver> receivers;

    private Circuit(Generator generator, List<Receiver> receivers) {
        this.generator = generator;
        this.receivers = receivers;
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
        Circuit circuit = (Circuit) o;
        boolean areReceiversEqual = true;

        if ((receivers == null && circuit.getReceivers() != null) || (receivers != null && circuit.getReceivers() == null)) {
            return false;
        } else if (receivers != null && circuit.getReceivers() != null) {
            if (receivers.size() != circuit.getReceivers().size()) return false;
            int i = 0;
            for (Receiver receiver : receivers) {
                if (!(receiver.getId().equals(circuit.receivers.get(i).getId()) && receiver.getFirstPin().equals(circuit.receivers.get(i).getFirstPin()) && receiver.getSecondPin().equals(circuit.receivers.get(i).getSecondPin()))) {
                    areReceiversEqual = false;
                    break;
                }
                i++;
            }
        }

        return Objects.equals(generator, circuit.generator) && areReceiversEqual;
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
        private List<Receiver> receivers;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public final Builder setGenerator(Generator generator) {
            this.generator = generator;
            return this;
        }


        public final Builder addReceiver(Receiver receiver) {
            if (this.receivers == null) {
                this.receivers = new ArrayList<>();
            }
            this.receivers.add(receiver);
            return this;
        }

        public Builder connectComponents(Tuple<Pin, Component> pinComponentTuple, Tuple<Pin, Component> pinComponentTuple2) {
            Pin.connectComponents(pinComponentTuple, pinComponentTuple2);
            return this;
        }


        public Circuit build() {
            return new Circuit(generator, receivers);
        }

        public Component getComponentById(String id) {
            List<Component> components = newArrayList();
            components.addAll(receivers);
            components.add(generator);
            return components.stream().filter(receiver -> receiver.getId().equals(id)).findFirst().orElse(null);
        }
    }
}