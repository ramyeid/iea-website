/**
 * Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 * <p>
 * This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 * express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.listener;

import com.iea.SimpleCircuitTemplates;
import com.iea.circuit.DefaultCircuit;
import com.iea.listener.SimulatorListener;
import com.iea.utils.emitter.CustomSseEmitter;
import com.iea.utils.emitter.EmitterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;


// todo parametrized test for different default circuits and rpi circuits.
@RunWith(MockitoJUnitRunner.class)
public class SimulatorListenerTest {

    private static SimulatorListener testSimulator = new SimulatorListener();

    @Mock
    private CustomSseEmitter mockSseEmitter;

    private final ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);

    @Test
    public void should_simulate_default_circuit_optimal_for_red_led_low_for_green_led() throws EmitterException {
        DefaultCircuit circuit = new DefaultCircuit(SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneGreenLED());

        testSimulator.onSubmit(circuit, "0", mockSseEmitter);

        Mockito.verify(mockSseEmitter, times(1)).send(argCaptor.capture());
        String expectedSerializedString = "led01:2,led02:1";
        assertEquals(expectedSerializedString, argCaptor.getValue());
    }

    @Test
    public void should_simulate_default_circuit_damaged_for_resistor_and_optimal_for_red_led() throws EmitterException {
        DefaultCircuit circuit = new DefaultCircuit(SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneResistor());

        testSimulator.onSubmit(circuit, "0", mockSseEmitter);

        Mockito.verify(mockSseEmitter, times(1)).send(argCaptor.capture());
        String expectedSerializedString = "res01:3,led01:2";
        assertEquals(expectedSerializedString, argCaptor.getValue());
    }

    @Test
    public void should_simulate_default_circuit_low_for_all_leds() throws EmitterException {
        DefaultCircuit circuit = new DefaultCircuit(SimpleCircuitTemplates.createSeriesCircuitWithTwoRedLEDsAndOneGreenLED());

        testSimulator.onSubmit(circuit, "0", mockSseEmitter);

        Mockito.verify(mockSseEmitter, times(1)).send(argCaptor.capture());
        String expectedSerializedString = "led01:1,led03:1,led02:1";
        assertEquals(expectedSerializedString, argCaptor.getValue());
    }

    @Test
    public void should_simulate_default_circuit_damaged_For_green_led() throws EmitterException {
        DefaultCircuit circuit = new DefaultCircuit(SimpleCircuitTemplates.createSeriesCircuitWithOneGreenLED());

        testSimulator.onSubmit(circuit, "0", mockSseEmitter);

        Mockito.verify(mockSseEmitter, times(1)).send(argCaptor.capture());
        String expectedSerializedString = "led01:3";
        assertEquals(expectedSerializedString, argCaptor.getValue());
    }

    @Test
    public void should_simulate_default_circuit_low_for_led_and_two_resistors() throws EmitterException {
        DefaultCircuit circuit = new DefaultCircuit(SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndTwoResistors());

        testSimulator.onSubmit(circuit, "0", mockSseEmitter);

        Mockito.verify(mockSseEmitter, times(1)).send(argCaptor.capture());
        String expectedSerializedString = "res01:1,res02:1,led01:1";
        assertEquals(expectedSerializedString, argCaptor.getValue());
    }

    @Test
    public void should_simulate_default_circuit_low_for_buzzer_optimal_for_led() throws EmitterException {
        DefaultCircuit circuit = new DefaultCircuit(SimpleCircuitTemplates.createSeriesCircuitWithOneRedLEDAndOneBuzzer());

        testSimulator.onSubmit(circuit, "0", mockSseEmitter);

        Mockito.verify(mockSseEmitter, times(1)).send(argCaptor.capture());
        String expectedSerializedString = "led01:2,buz01:1";
        assertEquals(expectedSerializedString, argCaptor.getValue());
    }

    @Test
    public void should_simulate_default_circuit_off_for_three_buzzers() throws EmitterException {
        DefaultCircuit circuit = new DefaultCircuit(SimpleCircuitTemplates.createSeriesCircuitWithThreeBuzzers());

        testSimulator.onSubmit(circuit, "0", mockSseEmitter);

        Mockito.verify(mockSseEmitter, times(1)).send(argCaptor.capture());
        String expectedSerializedString = "buz03:0,buz02:0,buz01:0";
        assertEquals(expectedSerializedString, argCaptor.getValue());
    }
}
