package com.iea.circuit.simulator;

import com.iea.SimpleCircuitTemplates;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import org.junit.Test;

import java.util.Map;

import static com.iea.circuit.simulator.ReceiverStatusesGenerator.generateReceiverStatuses;
import static org.junit.Assert.assertEquals;

public class ReceiverStatusesGeneratorTest {

    @Test
    public void should_ensure_retrieveStatus_should_return_OPTIMAL_for_RedLED_LOW_For_Green_LED() {
        SimpleCircuit simpleCircuit = SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneGreenLED();

        Map<Receiver, ReceiverStatus> actual = generateReceiverStatuses(simpleCircuit);

        assertEquals(ReceiverStatus.OPTIMAL, geteReceiverStatusForReceiverId("led01", actual));
        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("led02", actual));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_DAMAGED_for_resistor_and_Optimal_for_RedLED() {
        SimpleCircuit simpleCircuit = SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneResistor();

        Map<Receiver, ReceiverStatus> actual = generateReceiverStatuses(simpleCircuit);

        assertEquals(ReceiverStatus.DAMAGED, geteReceiverStatusForReceiverId("res01", actual));
        assertEquals(ReceiverStatus.OPTIMAL, geteReceiverStatusForReceiverId("led01", actual));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_for_all_LEDs() {
        SimpleCircuit simpleCircuit = SimpleCircuitTemplates.createSeriesCircuitWithTwoRedLEDsAndOneGreenLED();

        Map<Receiver, ReceiverStatus> actual = generateReceiverStatuses(simpleCircuit);

        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("led01", actual));
        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("led02", actual));
        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("led03", actual));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_For_LED_And_Two_Resistors() {
        SimpleCircuit simpleCircuit = SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndTwoResistors();

        Map<Receiver, ReceiverStatus> actual = generateReceiverStatuses(simpleCircuit);

        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("led01", actual));
        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("res01", actual));
        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("res02", actual));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_LOW_For_Buzzer_OPTIMAL_For_LED() {
        SimpleCircuit simpleCircuit = SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneBuzzer();

        Map<Receiver, ReceiverStatus> actual = generateReceiverStatuses(simpleCircuit);

        assertEquals(ReceiverStatus.LOW, geteReceiverStatusForReceiverId("buz01", actual));
        assertEquals(ReceiverStatus.OPTIMAL, geteReceiverStatusForReceiverId("led01", actual));
    }

    @Test
    public void should_ensure_retrieveStatus_returns_OFF_For_Three_Buzzers() {
        SimpleCircuit simpleCircuit = SimpleCircuitTemplates.createSeriesCircuitWithThreeBuzzers();

        Map<Receiver, ReceiverStatus> actual = generateReceiverStatuses(simpleCircuit);

        assertEquals(ReceiverStatus.OFF, geteReceiverStatusForReceiverId("buz01", actual));
        assertEquals(ReceiverStatus.OFF, geteReceiverStatusForReceiverId("buz02", actual));
        assertEquals(ReceiverStatus.OFF, geteReceiverStatusForReceiverId("buz03", actual));
    }

    private ReceiverStatus geteReceiverStatusForReceiverId(String id, Map<Receiver, ReceiverStatus> receiverReceiverStatusMap) {
        Receiver receiver = receiverReceiverStatusMap.keySet().stream().filter(temp -> temp.getId().equals(id)).findFirst().get();
        return receiverReceiverStatusMap.get(receiver);
    }
}
