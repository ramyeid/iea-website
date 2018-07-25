package com.iea.circuit.generator;

import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.pin.PinFactory;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;


public class Generator extends Component {

    private final GeneratorConfiguration generatorConfiguration;

    public Generator(String id, GeneratorConfiguration generatorConfiguration) {
        super(id, PinFactory.createPositivePin(), PinFactory.createNegativePin());
        this.generatorConfiguration = generatorConfiguration;
    }

    public Pin getPositivePin() {
        return getFirstPin();
    }

    public Pin getNegativePin() {
        return getSecondPin();
    }

    public GeneratorConfiguration getGeneratorConfiguration() {
        return generatorConfiguration;
    }

    @Override
    public List<Pin> getPins() {
        return asList(getFirstPin(), getSecondPin());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Generator generator = (Generator) o;
        return Objects.equals(generatorConfiguration, generator.generatorConfiguration);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), generatorConfiguration);
    }
}
