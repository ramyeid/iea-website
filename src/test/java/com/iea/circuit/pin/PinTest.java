package com.iea.circuit.pin;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.utils.Tuple;
import org.junit.Test;

import static com.iea.circuit.generator.GeneratorFactory.createGenerator;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class PinTest {

    private static final GeneratorConfiguration GENERATOR_CONFIGURATION = new GeneratorConfiguration(40, 6);

    @Test
    public void should_equals_between_two_pins_with_same_connections_and_type_be_equal() {
        Generator generator0 = createGenerator("gen0", GENERATOR_CONFIGURATION);
        Generator generator1 = createGenerator("gen1", GENERATOR_CONFIGURATION);
        Generator generator2 = createGenerator("gen2", GENERATOR_CONFIGURATION);

        Pin.connectComponents(new Tuple<>(generator0.getNegativePin(), generator0), new Tuple<>(generator1.getNegativePin(), generator1));
        Pin.connectComponents(new Tuple<>(generator2.getNegativePin(), generator2), new Tuple<>(generator1.getNegativePin(), generator1));

        assertTrue(generator0.getNegativePin().equals(generator2.getNegativePin()));
    }

    @Test
    public void should_equals_between_two_pins_with_different_connections_and_same_type_return_false() {
        Generator generator0 = createGenerator("gen0", GENERATOR_CONFIGURATION);
        Generator generator1 = createGenerator("gen1", GENERATOR_CONFIGURATION);
        Generator generator2 = createGenerator("gen2", GENERATOR_CONFIGURATION);

        Pin.connectComponents(new Tuple<>(generator0.getNegativePin(), generator0), new Tuple<>(generator1.getNegativePin(), generator1));
        Pin.connectComponents(new Tuple<>(generator2.getNegativePin(), generator2), new Tuple<>(generator1.getPositivePin(), generator1));

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

        Pin.connectComponents(new Tuple<>(generator0.getNegativePin(), generator0), new Tuple<>(generator1.getNegativePin(), generator1));
        Pin.connectComponents(new Tuple<>(generator2.getPositivePin(), generator2), new Tuple<>(generator1.getNegativePin(), generator1));

        assertFalse(generator0.getNegativePin().equals(generator2.getPositivePin()));
    }

}