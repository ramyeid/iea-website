package com.iea.simulator;

import com.iea.CircuitTemplates;
import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverConfiguration;
import com.iea.circuit.receiver.ReceiverStatus;
import com.iea.simulator.exception.NoGeneratorException;
import com.iea.utils.Tuple;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;
import static org.junit.Assert.assertEquals;

public class CircuitSimulatorTest {


    @Test
    public void should_ensure_retrieveStatus_returns_OPTIMAL_for_all_receivers() {
        Circuit circuit = CircuitTemplates.create_circuit_with_three_receivers_series();

        List<Receiver> validatedComponents = Validator.validate(circuit);

        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.OPTIMAL);
        expected.put(validatedComponents.get(1), ReceiverStatus.OPTIMAL);
        expected.put(validatedComponents.get(2), ReceiverStatus.OPTIMAL);

        assertEquals(result, expected);
    }

    @Test
    public void should_ensure_retrieveStatus_returns_OFF_for_all_receivers() {
        Circuit circuit = CircuitTemplates.create_series_circuit_with_three_receivers_series_high_resistance_and_low_generator_volt();
        List<Receiver> validatedComponents = Validator.validate(circuit);
        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.OFF);
        expected.put(validatedComponents.get(1), ReceiverStatus.OFF);
        expected.put(validatedComponents.get(2), ReceiverStatus.OFF);

        assertEquals(result, expected);
    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_for_all_receivers() {
        Circuit circuit = CircuitTemplates.create_series_circuit_with_three_receivers_series_receivers_high_amper();
        List<Receiver> validatedComponents = Validator.validate(circuit);
        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.LOW);
        expected.put(validatedComponents.get(1), ReceiverStatus.LOW);
        expected.put(validatedComponents.get(2), ReceiverStatus.LOW);

        assertEquals(result, expected);
    }

    @Test
    public void should_ensure_retreiveStatus_returns_DAMAGED_due_to_high_voltage_for_receivers() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithThreeReceiversWithLowMaxvolt();

        List<Receiver> validatedComponents = Validator.validate(circuit);
        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.DAMAGED);
        expected.put(validatedComponents.get(1), ReceiverStatus.DAMAGED);
        expected.put(validatedComponents.get(2), ReceiverStatus.DAMAGED);

        assertEquals(result, expected);

    }

    @Test
    public void should_ensure_retrieveStatus_returns_every_status_for_every_receiver_respectively() {

        ReceiverConfiguration led01Config = new ReceiverConfiguration(3, 1, 4, 2);
        ReceiverConfiguration led02Config = new ReceiverConfiguration(5, 1, 4, 2);
        ReceiverConfiguration motor01Config = new ReceiverConfiguration(5, 1, 4, 5);
        ReceiverConfiguration motor02Config = new ReceiverConfiguration(2, 6, 8, 3);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 20);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", led01Config);
        DipoleReceiver led02 = new DipoleReceiver("led02", led02Config);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motor01Config);
        DipoleReceiver motor02 = new DipoleReceiver("mot02", motor02Config);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .addReceiver(motor02)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(motor02.getPositivePin(), motor02))
                .connectComponents(new Tuple<>(motor02.getNegativePin(), motor02), new Tuple<>(generator.getNegativePin(), generator))
                .build();
        List<Receiver> validatedComponents = Validator.validate(circuit);
        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();

        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(led01, ReceiverStatus.LOW);
        expected.put(led02, ReceiverStatus.LOW);
        expected.put(motor01, ReceiverStatus.DAMAGED);
        expected.put(motor02, ReceiverStatus.OFF);


        assertEquals(expected, result);
    }

    @Test
    public void should_ensure_retrieveStatus_returns_OPRIMAL_for_LEDs_DAMAGED_for_motor() {
        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1.2, 1, 4, 1);
        ReceiverConfiguration motorConfig = new ReceiverConfiguration(1, 2, 6, 10);
        GeneratorConfiguration generatorConfig = new GeneratorConfiguration(10, 20);

        Generator generator = new Generator("gen01", generatorConfig);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);
        DipoleReceiver led02 = new DipoleReceiver("led02", ledConfig);
        DipoleReceiver motor01 = new DipoleReceiver("mot01", motorConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(motor01)
                .connectComponents(new Tuple<>(generator.getPositivePin(), generator), new Tuple<>(led01.getPositivePin(), led01))
                .connectComponents(new Tuple<>(led01.getNegativePin(), led01), new Tuple<>(motor01.getPositivePin(), motor01))
                .connectComponents(new Tuple<>(motor01.getNegativePin(), motor01), new Tuple<>(led02.getPositivePin(), led02))
                .connectComponents(new Tuple<>(led02.getNegativePin(), led02), new Tuple<>(generator.getNegativePin(), generator))
                .build();

        List<Receiver> validatedComponents = Validator.validate(circuit);

        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(led01, ReceiverStatus.OPTIMAL);
        expected.put(led02, ReceiverStatus.OPTIMAL);
        expected.put(motor01, ReceiverStatus.DAMAGED);

        assertEquals(result, expected);
    }

    @Test(expected = NoGeneratorException.class)
    public void NoGeneratorFoundExceptionTest() throws NoGeneratorException {
        ReceiverConfiguration ledConfig = new ReceiverConfiguration(1.2, 1, 4, 1);
        DipoleReceiver led01 = new DipoleReceiver("led01", ledConfig);

        Circuit.Builder.newBuilder();
        Circuit circuit = Circuit.Builder.newBuilder().addReceiver(led01).build();
        CircuitSimulator.simulate(circuit);
    }
}



