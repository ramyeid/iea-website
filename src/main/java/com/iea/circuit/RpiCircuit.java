package com.iea.circuit;

import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import com.iea.circuit.simulator.ReceiverStatusesGenerator;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;

public class RpiCircuit implements Circuit {

    private final List<SimpleCircuit> circuits;

    public RpiCircuit(List<SimpleCircuit> circuits) {
        this.circuits = checkNotNull(circuits, "circuits can't be null");
    }

    @Override
    public List<Receiver> getReceivers() {
        return circuits.stream().map(SimpleCircuit::getReceivers).flatMap(Collection::stream).collect(Collectors.toList());
    }

    //TODO REFACTOR FROM LIST OF MAP TO MAP.
    @Override
    public Map<Receiver, ReceiverStatus> generateReceiverStatuses() {
        List<Map<Receiver, ReceiverStatus>> collect = circuits.stream().map(ReceiverStatusesGenerator::generateReceiverStatuses).collect(Collectors.toList());
        Map<Receiver, ReceiverStatus> receiverReceiverStatusMap = newHashMap();
        collect.forEach(receiverReceiverStatusMap::putAll);
        return receiverReceiverStatusMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpiCircuit that = (RpiCircuit) o;

        if (that.circuits.size() != circuits.size()) {
            return false;
        }
        for(SimpleCircuit circuit: circuits) {
            boolean didFindCircuit = false;
            for(SimpleCircuit thatCircuit: that.circuits) {
                if (circuit.equals(thatCircuit)) {
                    didFindCircuit = true;
                }
            }
            if (!didFindCircuit) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(circuits);
    }

    @Override
    public String toString() {
        return "RpiCircuit{" +
                "circuits=" + circuits +
                '}';
    }
}