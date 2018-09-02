package com.iea.circuit.creator;

import com.iea.circuit.Circuit;
import com.iea.circuit.RpiCircuit;
import com.iea.circuit.creator.exception.DuplicateReceiversInCircuitsException;
import com.iea.circuit.creator.exception.InvalidNumberOfGeneratorException;
import com.iea.circuit.model.Connection;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.receiver.config.Receiver;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class RpiCircuitCreator extends CircuitCreator {

    @Override
    protected Circuit doCreate(List<Generator> generators, List<Receiver> receivers, List<Connection> connections) throws InvalidNumberOfGeneratorException, DuplicateReceiversInCircuitsException {
        if (generators.isEmpty()) {
            throw new InvalidNumberOfGeneratorException(generators);
        }

        List<SimpleCircuit> simpleCircuits = generators.stream()
                .map(this::buildSimpleCircuitWithGenerator)
                .collect(toList());

        checkForCommonReceivers(simpleCircuits);
        return new RpiCircuit(simpleCircuits);
    }

    private void checkForCommonReceivers(List<SimpleCircuit> simpleCircuits) throws DuplicateReceiversInCircuitsException {
        Set<String> commonReceiversIds = findCommonReceiversInDistinctCircuits(simpleCircuits);
        if (!commonReceiversIds.isEmpty()) {
            throw new DuplicateReceiversInCircuitsException(commonReceiversIds.toString());
        }
    }

    private Set<String> findCommonReceiversInDistinctCircuits(List<SimpleCircuit> simpleCircuits) {
        List<String> receiversIds = simpleCircuits
                .stream()
                .map(circuit -> circuit.getReceivers()
                        .stream()
                        .map(Component::getId)
                        .collect(toList())
                )
                .flatMap(Collection::stream)
                .collect(toList());

        return receiversIds.stream().filter(id -> Collections.frequency(receiversIds, id) > 1).collect(Collectors.toSet());
    }
}
