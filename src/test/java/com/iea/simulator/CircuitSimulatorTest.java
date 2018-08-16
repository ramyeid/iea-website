package com.iea.simulator;

import com.iea.CircuitTemplates;
import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverConfiguration;
import com.iea.circuit.receiver.config.ReceiverStatus;
import com.iea.simulator.exception.NoGeneratorException;
import org.junit.Test;

import java.util.Map;

import static com.iea.Utils.retrieveReceiverWithId;
import static com.iea.circuit.receiver.ReceiverFactory.createReceiver;
import static com.iea.circuit.receiver.config.ReceiverType.DIPOLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;

public class CircuitSimulatorTest {

    @Test
    public void should_ensure_retrieve_status_returns_OPTIMAL_for_RedLED_LOW_For_Green_LED() throws NoGeneratorException {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneGreenLED();

        Map<Receiver, ReceiverStatus> actualResult = CircuitSimulator.simulate(circuit);

        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(retrieveReceiverWithId("led01", circuit), ReceiverStatus.OPTIMAL);
        expected.put(retrieveReceiverWithId("led02", circuit), ReceiverStatus.LOW);
        assertThat(expected, equalTo(actualResult));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_DAMAGED_for_resistor_and_Optimal_for_RedLED() throws NoGeneratorException {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneResistor();

        Map<Receiver, ReceiverStatus> actualResult = CircuitSimulator.simulate(circuit);

        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(retrieveReceiverWithId("res01", circuit), ReceiverStatus.DAMAGED);
        expected.put(retrieveReceiverWithId("led01", circuit), ReceiverStatus.OPTIMAL);
        assertThat(expected, equalTo(actualResult));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_for_all_LEDs() throws NoGeneratorException {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithTwoRedLEDsAndOneGreenLED();

        Map<Receiver, ReceiverStatus> actualResult = CircuitSimulator.simulate(circuit);

        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(retrieveReceiverWithId("led01", circuit), ReceiverStatus.LOW);
        expected.put(retrieveReceiverWithId("led02", circuit), ReceiverStatus.LOW);
        expected.put(retrieveReceiverWithId("led03", circuit), ReceiverStatus.LOW);
        assertThat(expected, equalTo(actualResult));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_DAMAGED_For_GreenLED() throws NoGeneratorException {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneGreenLED();

        Map<Receiver, ReceiverStatus> actualResult = CircuitSimulator.simulate(circuit);

        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(retrieveReceiverWithId("led01", circuit), ReceiverStatus.DAMAGED);
        assertThat(expected, equalTo(actualResult));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_For_LED_And_Two_Resistors() throws NoGeneratorException {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndTwoResistors();

        Map<Receiver, ReceiverStatus> actualResult = CircuitSimulator.simulate(circuit);

        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(retrieveReceiverWithId("led01", circuit), ReceiverStatus.LOW);
        expected.put(retrieveReceiverWithId("res01", circuit), ReceiverStatus.LOW);
        expected.put(retrieveReceiverWithId("res02", circuit), ReceiverStatus.LOW);
        assertThat(expected, equalTo(actualResult));
    }


    @Test
    public void should_ensure_retrieveStatus_returns_LOW_For_Buzzer_OPTIMAL_For_LED() throws NoGeneratorException {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneBuzzer();

        Map<Receiver, ReceiverStatus> actualResult = CircuitSimulator.simulate(circuit);

        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(retrieveReceiverWithId("led01", circuit), ReceiverStatus.OPTIMAL);
        expected.put(retrieveReceiverWithId("buz01", circuit), ReceiverStatus.LOW);
        assertThat(expected, equalTo(actualResult));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_OFF_For_Three_Buzzers() throws NoGeneratorException {
        Circuit circuit = CircuitTemplates.createSeriesCircuitWithThreeBuzzers();

        Map<Receiver, ReceiverStatus> actualResult = CircuitSimulator.simulate(circuit);

        Map<Receiver, ReceiverStatus> expected = newHashMap();
        expected.put(retrieveReceiverWithId("buz01", circuit), ReceiverStatus.OFF);
        expected.put(retrieveReceiverWithId("buz02", circuit), ReceiverStatus.OFF);
        expected.put(retrieveReceiverWithId("buz03", circuit), ReceiverStatus.OFF);
        assertThat(expected, equalTo(actualResult));
    }

    @Test(expected = NoGeneratorException.class)
    public void NoGeneratorFoundExceptionTest() throws NoGeneratorException {
        Receiver led01 = createReceiver("led01", DIPOLE, new ReceiverConfiguration(1.2, 1, 4, 1));

        Circuit circuit = Circuit.Builder.newBuilder().addReceiver(led01).build();
        CircuitSimulator.simulate(circuit);
    }
}



