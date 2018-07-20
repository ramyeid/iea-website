package com.iea.circuit.pin;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.utils.Tuple;
import org.junit.Test;

public class PinTest {

    GeneratorConfiguration generatorConfig = new GeneratorConfiguration(40,6);

    @Test
    public void should_create_two_identical_pins_correctly_and_compare_them_true() {

        Pin pin0 = PinFactory.createPositivePin();
        Pin pin1 = PinFactory.createPositivePin();
        assert (pin0.equals(pin1));

    }

    @Test
    public void should_create_two_identical_pins_form_different_generators_and_compare_them_true() {

        Generator generator0 = new Generator("gen0", generatorConfig);
        Generator generator1 = new Generator("gen1", generatorConfig);
        Generator generator2 = new Generator("gen2", generatorConfig);
        Pin.connectComponents(new Tuple<>(generator0.getNegativePin(),generator0), new Tuple<>(generator1.getNegativePin(),generator1));
        Pin.connectComponents(new Tuple<>(generator2.getNegativePin(),generator2), new Tuple<>(generator1.getNegativePin(),generator1));
        assert (generator0.getNegativePin().equals(generator2.getNegativePin()));
    }

    @Test
    public void should_create_two_different_pins_form_different_generators_and_compare_them_false() {

        Generator generator0 = new Generator("gen0", generatorConfig);
        Generator generator1 = new Generator("gen1", generatorConfig);
        Generator generator2 = new Generator("gen2", generatorConfig);
        Pin.connectComponents(new Tuple<>(generator0.getNegativePin(),generator0), new Tuple<>(generator1.getNegativePin(),generator1));
        Pin.connectComponents(new Tuple<>(generator2.getNegativePin(),generator2), new Tuple<>(generator1.getPositivePin(),generator1));
        assert !(generator0.getNegativePin().equals(generator2.getNegativePin()));
    }

    @Test
    public void should_create_two_different_pins_form_same_generator_with_only_different_type_and_compare_them_false() {

        Generator generator0 = new Generator("gen0", generatorConfig);
        assert !(generator0.getNegativePin().equals(generator0.getPositivePin()));
    }

    @Test
    public void should_create_two_different_pins_form_same_generator_and_compare_them_false() {
        Generator generator0 = new Generator("gen0", generatorConfig);
        Generator generator1 = new Generator("gen1", generatorConfig);

        Pin.connectComponents(new Tuple<>(generator0.getNegativePin(),generator0), new Tuple<>(generator1.getNegativePin(),generator1));
        assert !(generator0.getNegativePin().equals(generator0.getPositivePin()));
    }

}