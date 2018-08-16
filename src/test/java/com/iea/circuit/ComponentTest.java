package com.iea.circuit;

import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.config.ReceiverConfiguration;
import com.iea.utils.Tuple;
import org.junit.Test;

import static com.iea.circuit.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.receiver.ReceiverFactory.createReceiver;
import static com.iea.circuit.receiver.config.ReceiverType.DIPOLE;
import static org.junit.Assert.*;

public class ComponentTest {

    @Test
    public void should_equals_between_two_identical_complex_components_return_true() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Tuple<>(component0.getFirstPin(), component0), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component1.getFirstPin(), component1), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component0.getSecondPin(), component0), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component1.getSecondPin(), component1), new Tuple<>(component2.getSecondPin(), component2));

        assertTrue(component0.equals(component1));
    }

    @Test
    public void should_equals_between_two_complex_components_with_different_id_return_false() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen1", new GeneratorConfiguration(40, 6));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Tuple<>(component0.getFirstPin(), component0), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component1.getFirstPin(), component1), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component0.getSecondPin(), component0), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component1.getSecondPin(), component1), new Tuple<>(component2.getSecondPin(), component2));

        assertFalse(component0.equals(component1));
    }

    @Test
    public void should_equals_between_two_complex_components_with_different_configuration_return_false() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen1", new GeneratorConfiguration(43, 7));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Tuple<>(component0.getFirstPin(), component0), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component1.getFirstPin(), component1), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component0.getSecondPin(), component0), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component1.getSecondPin(), component1), new Tuple<>(component2.getSecondPin(), component2));

        assertFalse(component0.equals(component1));
    }

    @Test
    public void should_equals_between_two_complex_components_with_different_connections_return_false() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen1", new GeneratorConfiguration(43, 7));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Tuple<>(component0.getFirstPin(), component0), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component1.getFirstPin(), component1), new Tuple<>(component2.getSecondPin(), component2));
        Pin.connectComponents(new Tuple<>(component0.getSecondPin(), component0), new Tuple<>(component2.getSecondPin(), component2));

        assertFalse(component0.equals(component1));
    }

    @Test
    public void should_equals_between_two_different__complex_components_with_same_connections_return_false() {
        Component component0 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 1, 2, 100));
        Component component1 = createGenerator("gen0", new GeneratorConfiguration(40, 6));

        Pin.connectComponents(new Tuple<>(component0.getFirstPin(), component0), new Tuple<>(component1.getSecondPin(), component1));
        Pin.connectComponents(new Tuple<>(component1.getFirstPin(), component1), new Tuple<>(component0.getSecondPin(), component0));

        assertNotEquals(component0, component1);
    }
}