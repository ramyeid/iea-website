package com.iea.jsmapping;

import com.google.common.collect.Sets;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import com.iea.circuit.model.component.receiver.config.ReceiverType;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static com.iea.circuit.model.component.receiver.ReceiverFactory.createReceiver;
import static com.iea.jsmapping.Serializer.serialize;
import static org.junit.Assert.assertEquals;

public class SerializerTest {

    private static final ReceiverConfiguration RECEIVER_CONFIGURATION = new ReceiverConfiguration(0.02, 1.3, 2.5, 100);

    @Test
    public void should_return_correct_string() {
        Map<Receiver, ReceiverStatus> givenMap = newHashMap();
        givenMap.put(createReceiver("res-2", ReceiverType.RESISTOR, RECEIVER_CONFIGURATION), ReceiverStatus.DAMAGED);
        givenMap.put(createReceiver("buz-3", ReceiverType.DIPOLE, RECEIVER_CONFIGURATION), ReceiverStatus.OFF);
        givenMap.put(createReceiver("led-1", ReceiverType.DIPOLE, RECEIVER_CONFIGURATION), ReceiverStatus.OPTIMAL);

        String actualString = serialize(givenMap);

        assertEquals(Sets.newHashSet("buz-3:0", "led-1:2", "res-2:3"), Sets.newHashSet(actualString.split(",")));
    }

    @Test
    public void should_return_empty_string() {
        Map<Receiver, ReceiverStatus> givenMap = newHashMap();

        String actualString = serialize(givenMap);

        String expectedString = "";
        assertEquals(expectedString, actualString);
    }

    @Test
    public void should_return_one_component_in_string() {
        Map<Receiver, ReceiverStatus> givenMap = newHashMap();
        givenMap.put(createReceiver("buz-3", ReceiverType.DIPOLE, RECEIVER_CONFIGURATION), ReceiverStatus.LOW);

        String actualString = serialize(givenMap);

        String expectedString = "buz-3:1";
        assertEquals(expectedString, actualString);
    }
}

