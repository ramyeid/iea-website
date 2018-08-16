package com.iea.simulator;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverConfiguration;
import com.iea.circuit.receiver.config.ReceiverType;
import com.iea.utils.Tuple;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.iea.circuit.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.receiver.ReceiverFactory.createReceiver;
import static org.hibernate.validator.internal.util.CollectionHelper.asSet;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;
import static org.junit.Assert.assertEquals;

public class ValidatorTest {

    private static final ReceiverConfiguration LED_CONFIG = new ReceiverConfiguration(1, 1, 4, 1);
    private static final ReceiverConfiguration MOTOR_CONFIG = new ReceiverConfiguration(2, 2, 5, 2);
    private static final GeneratorConfiguration GENERATOR_CONFIG = new GeneratorConfiguration(10, 10);
    private static Receiver LED_01;
    private static Receiver LED_02;
    private static Receiver MOTOR_01;
    private static Receiver MOTOR_02;

    private static Generator GENERATOR;

    @Before
    public void init() {
        LED_01 = createReceiver("led01", ReceiverType.DIPOLE, LED_CONFIG);
        LED_02 = createReceiver("led02", ReceiverType.DIPOLE, LED_CONFIG);
        MOTOR_01 = createReceiver("mot01", ReceiverType.DIPOLE, MOTOR_CONFIG);
        MOTOR_02 = createReceiver("mot02", ReceiverType.DIPOLE, MOTOR_CONFIG);
        GENERATOR = createGenerator("gen01", GENERATOR_CONFIG);
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_complete_series_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .connectComponents(new Tuple<>(LED_02.getSecondPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01, LED_02, MOTOR_01), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_unconnected_receiver() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01, MOTOR_01), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_an_open_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_only_a_generator() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_normal_parallel_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01), new Tuple<>(GENERATOR.getFirstPin(), GENERATOR))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getFirstPin(), LED_02), new Tuple<>(GENERATOR.getFirstPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getSecondPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01, LED_02, MOTOR_01), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_series_circuit_with_one_parallel_receiver() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getSecondPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01, LED_02, MOTOR_01), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_with_reversed_pins_in_series_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getSecondPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getFirstPin(), LED_01), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getSecondPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_receiver_with_switched_pins() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getSecondPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getFirstPin(), LED_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_with_series_receiver_and_reversed_pins_in_parallel_receiver() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(LED_02.getSecondPin(), LED_02))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getFirstPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01, MOTOR_01), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_multiple_receivers_with_switched_pins() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .connectComponents(new Tuple<>(LED_02.getSecondPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_with_two_receivers_connected_to_each_other() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(LED_02.getSecondPin(), LED_02))
                .connectComponents(new Tuple<>(LED_02.getSecondPin(), LED_02), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_delta_connection_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getFirstPin(), LED_02), new Tuple<>(GENERATOR.getFirstPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getSecondPin(), LED_02), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01, LED_02, MOTOR_01), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_simple_circuit_without_generator() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .connectComponents(new Tuple<>(LED_01.getFirstPin(), LED_01), new Tuple<>(LED_02.getSecondPin(), LED_02))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_an_empty_circuit() {
        Circuit circuit = Circuit.Builder.newBuilder().build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(newHashSet(), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_complex_circuit_without_generator() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .addReceiver(MOTOR_02)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getFirstPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(GENERATOR.getSecondPin(), GENERATOR), new Tuple<>(MOTOR_02.getSecondPin(), MOTOR_02))
                .connectComponents(new Tuple<>(MOTOR_02.getFirstPin(), MOTOR_02), new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01), new Tuple<>(LED_02.getSecondPin(), LED_02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_complex_circuit_without_a_generator() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .addReceiver(MOTOR_02)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getFirstPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(GENERATOR.getSecondPin(), GENERATOR), new Tuple<>(MOTOR_02.getSecondPin(), MOTOR_02))
                .connectComponents(new Tuple<>(MOTOR_02.getFirstPin(), MOTOR_02), new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01), new Tuple<>(LED_02.getSecondPin(), LED_02))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(LED_02.getFirstPin(), LED_02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01, LED_02, MOTOR_01, MOTOR_02), newHashSet(result));
    }

    @Test
    public void should_ensure_validator_returns_components_in_closed_circuit_for_circuit_without_generator() {
        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(MOTOR_01)
                .addReceiver(MOTOR_02)
                .connectComponents(new Tuple<>(GENERATOR.getFirstPin(), GENERATOR), new Tuple<>(LED_01.getFirstPin(), LED_01))
                .connectComponents(new Tuple<>(LED_01.getSecondPin(), LED_01), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(LED_02.getFirstPin(), LED_02), new Tuple<>(GENERATOR.getSecondPin(), GENERATOR))
                .connectComponents(new Tuple<>(GENERATOR.getSecondPin(), GENERATOR), new Tuple<>(MOTOR_02.getSecondPin(), MOTOR_02))
                .connectComponents(new Tuple<>(MOTOR_02.getFirstPin(), MOTOR_02), new Tuple<>(MOTOR_01.getSecondPin(), MOTOR_01))
                .connectComponents(new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01), new Tuple<>(LED_02.getSecondPin(), LED_02))
                .connectComponents(new Tuple<>(MOTOR_01.getFirstPin(), MOTOR_01), new Tuple<>(LED_02.getSecondPin(), LED_02))
                .build();

        List<Receiver> result = Validator.validate(circuit);

        assertEquals(asSet(LED_01), newHashSet(result));
    }
}