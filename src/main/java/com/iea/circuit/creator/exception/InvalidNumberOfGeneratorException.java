package com.iea.circuit.creator.exception;

import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.component.generator.Generator;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class InvalidNumberOfGeneratorException extends Exception {

    private static String ERROR_MESSAGE = "Invalid number of generators found in circuit; Generators = ";

    public InvalidNumberOfGeneratorException(List<Generator> generators) {
        super(ERROR_MESSAGE + generators.stream().map(Component::getId).collect(toList()));
    }
}
