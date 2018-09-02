package com.iea.circuit;

import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import com.iea.circuit.simulator.ReceiverStatusesGenerator;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultCircuit implements Circuit {

    private final SimpleCircuit simpleCircuit;

    public DefaultCircuit(SimpleCircuit simpleCircuit) {
        this.simpleCircuit = simpleCircuit;
    }

    @Override
    public List<Receiver> getReceivers() {
        return simpleCircuit.getReceivers();
    }

    public Generator getGenerator() {
        return simpleCircuit.getGenerator();
    }

    @Override
    public Map<Receiver, ReceiverStatus> generateReceiverStatuses() {
        return new ReceiverStatusesGenerator().generateReceiverStatuses(simpleCircuit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        DefaultCircuit that = (DefaultCircuit) o;
        return Objects.equals(simpleCircuit, that.simpleCircuit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleCircuit);
    }

    @Override
    public String toString() {
        return "DefaultCircuit{" +
                "simpleCircuit=" + simpleCircuit + '}';
    }
}
