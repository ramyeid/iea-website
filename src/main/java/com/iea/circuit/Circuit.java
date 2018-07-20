package com.iea.circuit;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.Receiver;
import com.iea.utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

public class Circuit {

    private final Generator generator;
    private final List<Receiver> receivers;

    public Circuit(Generator generator, List<Receiver> receivers) {
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
        if(receivers.size() != circuit.getReceivers().size()) return false;
        int i=0;
        for (Receiver receiver : receivers){
            if (!(receiver.getId().equals(circuit.receivers.get(i).getId()) && receiver.getFirstPin().equals(circuit.receivers.get(i).getFirstPin()) && receiver.getSecondPin().equals(circuit.receivers.get(i).getSecondPin()))){
                areReceiversEqual = false;
                break;
            }
            i++;
        }
        return Objects.equals(generator, circuit.generator) && areReceiversEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(generator, receivers);
    }

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

        public void clearComponents()
        {
            builder.receivers = new ArrayList<>();
            builder.generator = null;
        }
        public void setReceivers(List<Receiver> receivers) {
            this.receivers = receivers;
        }

        public Circuit build() {
            return new Circuit(builder.generator, builder.receivers);
        }

        public Component getComponentById(final String componentId) {
            List<Component> components = newArrayList();
            components.addAll(receivers);
            components.add(generator);
            return components.stream()
                    .filter(component -> component.getId()
                    .equals(componentId))
                    .findFirst()
                    .orElse(null);
        }
    }
}
