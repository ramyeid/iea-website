package com.iea.circuit.model.component.generator;

import static com.google.common.base.Preconditions.checkNotNull;

public class GeneratorFactory {

    public static Generator createGenerator(String id, GeneratorConfiguration generatorConfiguration) {
        checkNotNull(id, "Generator id can not be null");
        checkNotNull(generatorConfiguration, "Generator Configuration can not be null");

        return new Generator(id, generatorConfiguration);
    }
}
