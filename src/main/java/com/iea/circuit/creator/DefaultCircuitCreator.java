package com.iea.circuit.creator;

import com.iea.circuit.Circuit;
import com.iea.circuit.DefaultCircuit;
import com.iea.circuit.creator.exception.InvalidNumberOfGeneratorException;
import com.iea.circuit.model.Connection;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.receiver.config.Receiver;

import java.util.List;

public class DefaultCircuitCreator extends CircuitCreator {

    @Override
    protected Circuit doCreate(List<Generator> generators, List<Receiver> receivers, List<Connection> connections) throws InvalidNumberOfGeneratorException {
        if (generators.isEmpty() || generators.size() > 1) {
            throw new InvalidNumberOfGeneratorException(generators);
        }

        SimpleCircuit simpleCircuit = buildSimpleCircuitWithGenerator(generators.get(0));
        return new DefaultCircuit(simpleCircuit);
    }
}
