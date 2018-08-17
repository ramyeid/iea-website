package com.iea.circuit.model;

import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.component.receiver.DipoleReceiver;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import org.junit.Before;
import org.junit.Test;

import static com.iea.circuit.model.component.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.model.component.receiver.ReceiverFactory.createReceiver;
import static com.iea.circuit.model.component.receiver.config.ReceiverType.DIPOLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SimpleCircuitTest {
    private static final GeneratorConfiguration GENERATOR_CONFIGURATION_0 = new GeneratorConfiguration(40, 6);
    private static final GeneratorConfiguration GENERATOR_CONFIGURATION_1 = new GeneratorConfiguration(20, 3);
    private static final ReceiverConfiguration RECEIVER_CONFIGURATION = new ReceiverConfiguration(10, 1, 2, 0.2);
    private static Generator GENERATOR_0;
    private static Generator GENERATOR_1;
    private static Receiver LED_01;
    private static Receiver LED_11;
    private static Receiver LED_02;
    private static Receiver LED_12;
    private static Receiver MOT_01;
    private static Receiver MOT_11;

    @Before
    public void init() {
        GENERATOR_0 = createGenerator("gen01", GENERATOR_CONFIGURATION_0);
        GENERATOR_1 = createGenerator("gen01", GENERATOR_CONFIGURATION_0);
        LED_01 = createReceiver("led01", DIPOLE, RECEIVER_CONFIGURATION);
        LED_11 = createReceiver("led01", DIPOLE, RECEIVER_CONFIGURATION);
        LED_02 = createReceiver("led02", DIPOLE, RECEIVER_CONFIGURATION);
        LED_12 = createReceiver("led02", DIPOLE, RECEIVER_CONFIGURATION);
        MOT_01 = createReceiver("mot01", DIPOLE, RECEIVER_CONFIGURATION);
        MOT_11 = createReceiver("mot01", DIPOLE, RECEIVER_CONFIGURATION);
    }

    @Test
    public void should_build_two_identical_simple_circuits_correctly_and_compare_them_true() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_0)
                .addReceiver(LED_01)
                .connectComponents(new Connection(GENERATOR_0, GENERATOR_0.getPositivePin(), LED_01, ((DipoleReceiver) LED_01).getPositivePin()))
                .connectComponents(new Connection(LED_01, ((DipoleReceiver) LED_01).getNegativePin(), GENERATOR_0, GENERATOR_0.getNegativePin()))
                .build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1)
                .addReceiver(LED_11)
                .connectComponents(new Connection(GENERATOR_1, GENERATOR_1.getPositivePin(), LED_11, ((DipoleReceiver) LED_11).getPositivePin()))
                .connectComponents(new Connection(LED_11, ((DipoleReceiver) LED_11).getNegativePin(), GENERATOR_1, GENERATOR_1.getNegativePin()))
                .build();


        assertEquals(circuit1, circuit2);
    }

    @Test
    public void should_build_two_identical_circuits_correctly_and_compare_them_true() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_0)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .connectComponents(new Connection(GENERATOR_0, GENERATOR_0.getPositivePin(), LED_01, ((DipoleReceiver) LED_01).getPositivePin()))
                .connectComponents(new Connection(LED_01, ((DipoleReceiver) LED_01).getNegativePin(), LED_02, ((DipoleReceiver) LED_02).getPositivePin()))
                .connectComponents(new Connection(LED_02, ((DipoleReceiver) LED_02).getNegativePin(), GENERATOR_0, GENERATOR_0.getNegativePin()))
                .build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1)
                .addReceiver(LED_11)
                .addReceiver(LED_12)
                .connectComponents(new Connection(GENERATOR_1, GENERATOR_1.getPositivePin(), LED_11, ((DipoleReceiver) LED_11).getPositivePin()))
                .connectComponents(new Connection(LED_11, ((DipoleReceiver) LED_11).getNegativePin(), LED_12, ((DipoleReceiver) LED_12).getPositivePin()))
                .connectComponents(new Connection(LED_12, ((DipoleReceiver) LED_12).getNegativePin(), GENERATOR_1, GENERATOR_1.getNegativePin()))
                .build();

        assertEquals(circuit1, circuit2);
        /*List<Component> result = ValidatorTest.validate(circuit1);
        List<Component> expected = newArrayList();
        expected.add(LED_01); expected.add(LED_02);
        assert (new HashSet<>(result).equals(new HashSet<>(expected)));*/
    }

    @Test
    public void should_build_two_identical_complex_circuits_correctly_and_compare_them_true() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_0)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOT_01)
                .connectComponents(new Connection(GENERATOR_0, GENERATOR_0.getPositivePin(), LED_01, ((DipoleReceiver) LED_01).getPositivePin()))
                .connectComponents(new Connection(LED_01, ((DipoleReceiver) LED_01).getNegativePin(), LED_02, ((DipoleReceiver) LED_02).getPositivePin()))
                .connectComponents(new Connection(LED_02, ((DipoleReceiver) LED_02).getNegativePin(), GENERATOR_0, GENERATOR_0.getNegativePin()))
                .connectComponents(new Connection(MOT_01, MOT_01.getSecondPin(), GENERATOR_0, GENERATOR_0.getNegativePin()))
                .build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1)
                .addReceiver(LED_11)
                .addReceiver(LED_12)
                .addReceiver(MOT_11)
                .connectComponents(new Connection(GENERATOR_1, GENERATOR_1.getPositivePin(), LED_11, ((DipoleReceiver) LED_11).getPositivePin()))
                .connectComponents(new Connection(LED_11, ((DipoleReceiver) LED_11).getNegativePin(), LED_12, ((DipoleReceiver) LED_12).getPositivePin()))
                .connectComponents(new Connection(LED_12, ((DipoleReceiver) LED_12).getNegativePin(), GENERATOR_1, GENERATOR_1.getNegativePin()))
                .connectComponents(new Connection(MOT_11, MOT_11.getSecondPin(), GENERATOR_1, GENERATOR_1.getNegativePin()))
                .build();

        assertEquals(circuit1, circuit2);
    }

    @Test
    public void should_build_two_different_generator_circuits_correctly_and_compare_them_false() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(createGenerator("gen01", GENERATOR_CONFIGURATION_1))
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .connectComponents(new Connection(GENERATOR_0, GENERATOR_0.getPositivePin(), LED_01, ((DipoleReceiver) LED_01).getPositivePin()))
                .connectComponents(new Connection(LED_01, ((DipoleReceiver) LED_01).getNegativePin(), LED_02, ((DipoleReceiver) LED_02).getPositivePin()))
                .connectComponents(new Connection(LED_02, ((DipoleReceiver) LED_02).getNegativePin(), GENERATOR_0, GENERATOR_0.getNegativePin()))
                .build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1)
                .addReceiver(LED_11)
                .addReceiver(LED_12)
                .connectComponents(new Connection(GENERATOR_1, GENERATOR_1.getPositivePin(), LED_11, ((DipoleReceiver) LED_11).getPositivePin()))
                .connectComponents(new Connection(LED_11, ((DipoleReceiver) LED_11).getNegativePin(), LED_12, ((DipoleReceiver) LED_12).getPositivePin()))
                .connectComponents(new Connection(LED_12, ((DipoleReceiver) LED_12).getNegativePin(), GENERATOR_1, GENERATOR_1.getNegativePin()))
                .build();

        assertNotEquals(circuit1, circuit2);
    }

    @Test
    public void should_build_two_different_receivers_circuits_correctly_and_compare_them_false() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_0)
                .addReceiver(LED_01)
                .connectComponents(new Connection(GENERATOR_0, GENERATOR_0.getPositivePin(), LED_01, ((DipoleReceiver) LED_01).getPositivePin()))
                .connectComponents(new Connection(LED_01, ((DipoleReceiver) LED_01).getNegativePin(), LED_02, ((DipoleReceiver) LED_02).getPositivePin()))
                .connectComponents(new Connection(LED_02, ((DipoleReceiver) LED_02).getNegativePin(), GENERATOR_0, GENERATOR_0.getNegativePin()))
                .build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1)
                .addReceiver(LED_11)
                .addReceiver(LED_12)
                .connectComponents(new Connection(GENERATOR_1, GENERATOR_1.getPositivePin(), LED_11, ((DipoleReceiver) LED_11).getPositivePin()))
                .connectComponents(new Connection(LED_11, ((DipoleReceiver) LED_11).getNegativePin(), LED_12, ((DipoleReceiver) LED_12).getPositivePin()))
                .connectComponents(new Connection(LED_12, ((DipoleReceiver) LED_12).getNegativePin(), GENERATOR_1, GENERATOR_1.getNegativePin()))
                .build();

        assertNotEquals(circuit1, circuit2);
    }

    @Test
    public void should_build_two_different_wiring_circuits_correctly_and_compare_them_false() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_0)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .connectComponents(new Connection(GENERATOR_0, GENERATOR_0.getPositivePin(), LED_01, ((DipoleReceiver) LED_01).getPositivePin()))
                .connectComponents(new Connection(LED_01, ((DipoleReceiver) LED_01).getNegativePin(), LED_02, ((DipoleReceiver) LED_02).getPositivePin()))
                .build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1)
                .addReceiver(LED_11)
                .addReceiver(LED_12)
                .connectComponents(new Connection(GENERATOR_1, GENERATOR_1.getPositivePin(), LED_11, ((DipoleReceiver) LED_11).getPositivePin()))
                .connectComponents(new Connection(LED_11, ((DipoleReceiver) LED_11).getNegativePin(), LED_12, ((DipoleReceiver) LED_12).getPositivePin()))
                .connectComponents(new Connection(LED_12, ((DipoleReceiver) LED_12).getNegativePin(), GENERATOR_1, GENERATOR_1.getNegativePin()))
                .build();

        assertNotEquals(circuit1, circuit2);
    }


    @Test
    public void should_equate_two_circuits_with_no_receivers() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_0).build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1).build();
        assertEquals(circuit1, circuit2);
    }

    @Test
    public void should_not_equate_empty_circuit_with_circuit_containing_receivers() {
        SimpleCircuit circuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_0)
                .addReceiver(LED_01)
                .build();
        SimpleCircuit circuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(GENERATOR_1).build();
        assertNotEquals(circuit1, circuit2);
    }

    @Test(expected = NullPointerException.class)
    public void should_ensure_that_simple_circuit_can_not_be_build_with_generator() {
        SimpleCircuit.Builder.newBuilder().addReceiver(LED_01).build();
    }
}