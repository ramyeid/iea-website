package com.iea.simulator;

import com.iea.CircuitTemplates;
import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverConfiguration;
import com.iea.circuit.receiver.ReceiverStatus;
import com.iea.simulator.exception.NoGeneratorException;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;
import static org.junit.Assert.assertEquals;

public class CircuitSimulatorTest {
    @Test
    public void should_ensure_retrieveStatus_returns_OPTIMAL_for_RedLED_LOW_For_Green_LED() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneGreenLED();

        List<Receiver> validatedComponents = Validator.validate(circuit);

        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.OPTIMAL);
        expected.put(validatedComponents.get(1), ReceiverStatus.LOW);

        assertEquals(expected, result);
    }

    @Test
    public void should_ensure_retrieveStatus_returns_DAMAGED_for_resistor_and_Optimal_for_RedLED() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneResistor();
        List<Receiver> validatedComponents = Validator.validate(circuit);
        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.OPTIMAL);
        expected.put(validatedComponents.get(1), ReceiverStatus.DAMAGED);
        assertEquals(expected, result);

    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_for_all_LEDs() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithTwoRedLEDsAndOneGreenLED();
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
        assertEquals(expected, result);

    }

    @Test
    public void should_ensure_retrieveStatus_returns_DAMAGED_For_GreenLED() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneGreenLED();

        List<Receiver> validatedComponents = Validator.validate(circuit);
        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.DAMAGED);

        assertEquals(expected, result);

    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_For_LED_And_Two_Resistors() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndTwoResistors();
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

        assertEquals(expected, result);
    }


    @Test
    public void should_ensure_retrieveStatus_returns_LOW_For_Buzzer_OPTIMAL_For_LED() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneBuzzer();
        List<Receiver> validatedComponents = Validator.validate(circuit);

        double amp = AmpCalculator.calculateAmp(circuit.getGenerator(), validatedComponents);
        Map<Receiver, ReceiverStatus> result = newHashMap();
        for (Receiver receiver : validatedComponents) {
            result.put(receiver, CircuitSimulator.retrieveStatus(receiver, amp));
        }
        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(validatedComponents.get(0), ReceiverStatus.LOW);
        expected.put(validatedComponents.get(1), ReceiverStatus.OPTIMAL);

        assertEquals(expected, result);
    }

    @Test
    public void should_ensure_retrieveStatus_returns_OFF_For_Three_Buzzers() {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithThreeBuzzers();
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

        assertEquals(expected, result);
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



