package com.iea.circuit.model.pin;

import com.iea.circuit.model.Connection;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.utils.Tuple;
import org.junit.Test;

import static com.iea.circuit.model.component.generator.GeneratorFactory.createGenerator;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PinTest {

    private static final GeneratorConfiguration GENERATOR_CONFIGURATION = new GeneratorConfiguration(40, 6);

    @Test
    public void should_equals_between_two_pins_with_same_connections_and_type_be_equal() {
        Generator generator0 = createGenerator("gen0", GENERATOR_CONFIGURATION);
        Generator generator1 = createGenerator("gen1", GENERATOR_CONFIGURATION);
        Generator generator2 = createGenerator("gen2", GENERATOR_CONFIGURATION);

        Pin.connectComponents(new Connection(generator0, generator0.getNegativePin(), generator1, generator1.getNegativePin()));
        Pin.connectComponents(new Connection(generator2, generator2.getNegativePin(), generator1, generator1.getNegativePin()));

        assertTrue(generator0.getNegativePin().equals(generator2.getNegativePin()));
    }

    @Test
    public void should_equals_between_two_pins_with_different_connections_and_same_type_return_false() {
        Generator generator0 = createGenerator("gen0", GENERATOR_CONFIGURATION);
        Generator generator1 = createGenerator("gen1", GENERATOR_CONFIGURATION);
        Generator generator2 = createGenerator("gen2", GENERATOR_CONFIGURATION);

        Pin.connectComponents(new Connection(generator0, generator0.getNegativePin(), generator1, generator1.getNegativePin()));
        Pin.connectComponents(new Connection(generator2, generator2.getNegativePin(), generator1, generator1.getPositivePin()));

        assertFalse(generator0.getNegativePin().equals(generator2.getNegativePin()));
    }

    @Test
    public void should_equals_between_two_pins_with_different_type_return_false() {
        Generator generator0 = createGenerator("gen0", GENERATOR_CONFIGURATION);

        assertFalse(generator0.getNegativePin().equals(generator0.getPositivePin()));
    }

    @Test
    public void should_equals_between_two_pins_with_same_connections_and_different_type_return_false() {
        Generator generator0 = createGenerator("gen0", GENERATOR_CONFIGURATION);
        Generator generator1 = createGenerator("gen1", GENERATOR_CONFIGURATION);
        Generator generator2 = createGenerator("gen2", GENERATOR_CONFIGURATION);

        Pin.connectComponents(new Connection(generator0, generator0.getNegativePin(), generator1, generator1.getNegativePin()));
        Pin.connectComponents(new Connection(generator2, generator2.getPositivePin(), generator1, generator1.getNegativePin()));

        assertFalse(generator0.getNegativePin().equals(generator2.getPositivePin()));
    }

}