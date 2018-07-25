package com.iea.circuit.receiver;

import com.iea.circuit.receiver.exception.UnrecognisedReceiverTypeError;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReceiverFactoryTest {

    @Test
    public void should_create_dipole_receiver() {
        ReceiverConfiguration testConfig = new ReceiverConfiguration(5,5,5,5);
        String testId = "led-1";
        Receiver actualReceiver = ReceiverFactory.createReceiver(ReceiverType.DIPOLE,testId, testConfig);
        DipoleReceiver expectedReceiver = new DipoleReceiver(testId, testConfig);
        assertEquals(actualReceiver, expectedReceiver);
    }

    @Test
    public void should_create_resistor_receiver() {
        ReceiverConfiguration testConfig = new ReceiverConfiguration(5,5,5,5);
        String testId = "res-1";
        Receiver actualReceiver = ReceiverFactory.createReceiver(ReceiverType.RESISTOR,testId, testConfig);
        Resistor expectedReceiver = new Resistor(testId, testConfig);
        assertEquals(actualReceiver, expectedReceiver);
    }

}