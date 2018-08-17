package com.iea.circuit.model.component.receiver;

import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReceiverFactoryTest {

    @Test
    public void should_create_dipole_receiver() {
        ReceiverConfiguration testConfig = new ReceiverConfiguration(5, 5, 5, 5);
        String testId = "led-1";
        DipoleReceiver expectedReceiver = new DipoleReceiver(testId, testConfig);

        Receiver actualReceiver = ReceiverFactory.createReceiver(testId, ReceiverType.DIPOLE, testConfig);

        assertEquals(expectedReceiver, actualReceiver);
    }

    @Test
    public void should_create_resistor_receiver() {
        ReceiverConfiguration testConfig = new ReceiverConfiguration(5, 5, 5, 5);
        String testId = "res-1";
        Resistor expectedReceiver = new Resistor(testId, testConfig);

        Receiver actualReceiver = ReceiverFactory.createReceiver(testId, ReceiverType.RESISTOR, testConfig);

        assertEquals(actualReceiver, expectedReceiver);
    }
}