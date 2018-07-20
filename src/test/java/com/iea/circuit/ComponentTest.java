package com.iea.circuit;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.ReceiverConfiguration;
import com.iea.utils.Tuple;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComponentTest {


    @Test
    public void should_build_two_identical_simple_generators_correctly_and_compare_them_true() {

        GeneratorConfiguration generatorConfig0 = new GeneratorConfiguration(40,6);
        Component component0 = new Generator("gen0",generatorConfig0);
        Component component1 = new Generator("gen0",generatorConfig0);

        assertEquals (component0,component1);
    }

    @Test
    public void should_build_two_identical_complex_generators_correctly_and_compare_them_true() {

        GeneratorConfiguration generatorConfig0 = new GeneratorConfiguration(40,6);
        ReceiverConfiguration receiverConfig0 = new ReceiverConfiguration(40,1,2,100);
        Component component0 = new Generator("gen0",generatorConfig0);
        Component component1 = new Generator("gen0",generatorConfig0);
        Component component2 = new DipoleReceiver("rec0",receiverConfig0);
        Pin.connectComponents(new Tuple<>(component0.getFirstPin(),component0),new Tuple<>(component2.getSecondPin(),component2));
        Pin.connectComponents(new Tuple<>(component1.getFirstPin(),component1),new Tuple<>(component2.getSecondPin(),component2));
        Pin.connectComponents(new Tuple<>(component0.getSecondPin(),component0),new Tuple<>(component2.getSecondPin(),component2));
        Pin.connectComponents(new Tuple<>(component1.getSecondPin(),component1),new Tuple<>(component2.getSecondPin(),component2));

        assertEquals (component0,component1);
    }

    @Test
    public void should_build_two_different_id_wise_generators_correctly_and_compare_them_false() {

        GeneratorConfiguration generatorConfig0 = new GeneratorConfiguration(40,6);
        Component component0 = new Generator("gen0",generatorConfig0);
        Component component1 = new Generator("gen1",generatorConfig0);

        assertNotEquals(component0,component1);
    }

    @Test
    public void should_build_two_different_config_wise_generators_correctly_and_compare_them_false() {

        GeneratorConfiguration generatorConfig0 = new GeneratorConfiguration(40,6);
        GeneratorConfiguration generatorConfig1 = new GeneratorConfiguration(20,3);
        Component component0 = new Generator("gen0",generatorConfig0);
        Component component1 = new Generator("gen0",generatorConfig1);

        assertNotEquals(component0,component1);
    }

    @Test
    public void should_build_two_different_wiring_wise_generators_correctly_and_compare_them_false() {

        GeneratorConfiguration generatorConfig0 = new GeneratorConfiguration(40,6);
        ReceiverConfiguration receiverConfig0 = new ReceiverConfiguration(40,1,2,100);
        Component component0 = new Generator("gen0",generatorConfig0);
        Component component1 = new Generator("gen0",generatorConfig0);
        Component component2 = new DipoleReceiver("rec0",receiverConfig0);
        Pin.connectComponents(new Tuple<>(component0.getFirstPin(),component0),new Tuple<>(component2.getSecondPin(),component2));

        assertNotEquals(component0,component1);
    }

    @Test
    public void should_build_two_identical_dipoleReceivers_correctly_and_compare_them_true() {

        ReceiverConfiguration receiverConfig0 = new ReceiverConfiguration(40,1,2,100);
        Component component0 = new DipoleReceiver("rec0",receiverConfig0);
        Component component1 = new DipoleReceiver("rec0",receiverConfig0);

        assertEquals(component0,component1);
    }

    @Test
    public void should_build_two_different_components_and_compare_them_false() {

        ReceiverConfiguration receiverConfig0 = new ReceiverConfiguration(40,1,2,100);
        GeneratorConfiguration generatorConfig0 = new GeneratorConfiguration(40,6);
        Component component0 = new DipoleReceiver("rec0",receiverConfig0);
        Component component1 = new Generator("gen0",generatorConfig0);

        assertNotEquals(component0,component1);
    }

}