package com.iea.circuit.model.component;

import com.iea.circuit.model.Connection;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.pin.Pin;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.utils.Tuple;
import org.junit.Test;

import static com.iea.circuit.model.component.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.model.component.receiver.ReceiverFactory.createReceiver;
import static com.iea.circuit.model.component.receiver.config.ReceiverType.DIPOLE;
import static org.junit.Assert.*;

public class ComponentTest {

    @Test
    public void should_equals_between_two_identical_complex_components_return_true() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Connection(component0, component0.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component0, component0.getSecondPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getSecondPin(), component2, component2.getSecondPin()));

        assertEquals(component0, component1);
    }

    @Test
    public void should_equals_between_two_complex_components_with_different_id_return_false() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen1", new GeneratorConfiguration(40, 6));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Connection(component0, component0.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component0, component0.getSecondPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getSecondPin(), component2, component2.getSecondPin()));

        assertNotEquals(component0, component1);
    }

    @Test
    public void should_equals_between_two_complex_components_with_different_configuration_return_false() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen1", new GeneratorConfiguration(43, 7));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Connection(component0, component0.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component0, component0.getSecondPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getSecondPin(), component2, component2.getSecondPin()));

        assertNotEquals(component0, component1);
    }

    @Test
    public void should_equals_between_two_complex_components_with_different_connections_return_false() {
        Component component0 = createGenerator("gen0", new GeneratorConfiguration(40, 6));
        Component component1 = createGenerator("gen1", new GeneratorConfiguration(43, 7));
        Component component2 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 3, 10, 100));

        Pin.connectComponents(new Connection(component0, component0.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getFirstPin(), component2, component2.getSecondPin()));
        Pin.connectComponents(new Connection(component0, component0.getSecondPin(), component2, component2.getSecondPin()));

        assertNotEquals(component0, component1);
    }

    @Test
    public void should_equals_between_two_different__complex_components_with_same_connections_return_false() {
        Component component0 = createReceiver("rec0", DIPOLE, new ReceiverConfiguration(40, 1, 2, 100));
        Component component1 = createGenerator("gen0", new GeneratorConfiguration(40, 6));

        Pin.connectComponents(new Connection(component0, component0.getFirstPin(), component1, component1.getSecondPin()));
        Pin.connectComponents(new Connection(component1, component1.getFirstPin(), component0, component0.getSecondPin()));

        assertNotEquals(component0, component1);
    }
}