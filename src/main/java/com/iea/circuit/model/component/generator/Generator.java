package com.iea.circuit.model.component.generator;

import com.iea.circuit.model.component.Component;
import com.iea.circuit.model.pin.Pin;
import com.iea.circuit.model.pin.PinFactory;

import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;


public class Generator extends Component {

    private final GeneratorConfiguration generatorConfiguration;

    Generator(String id, GeneratorConfiguration generatorConfiguration) {
        super(id, PinFactory.createPositivePin(), PinFactory.createNegativePin());
        this.generatorConfiguration = generatorConfiguration;
    }

    public Pin getPositivePin() {
        return firstPin;
    }

    public Pin getNegativePin() {
        return secondPin;
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
    public String toString() {
        return "Generator{" +
                "generatorConfiguration=" + generatorConfiguration +
                ", id='" + id + '\'' +
                ", firstPin=" + firstPin +
                ", secondPin=" + secondPin +
                '}';
    }
}
