package com.iea.serializer;

import com.google.common.collect.Sets;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.circuit.receiver.ReceiverStatus;
import com.iea.circuit.receiver.ReceiverType;
import org.junit.Test;

import java.util.Map;

import static com.iea.serializer.Configurations.getReceiverConfiguration;
import static com.iea.serializer.Configurations.getReceiverType;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;
import static org.junit.Assert.assertEquals;

public class DeserializerTest {

    @Test
    public void should_return_correct_string()
    {
        Map<Receiver,ReceiverStatus> givenMap = newHashMap();
        givenMap.put(ReceiverFactory.createReceiver(getReceiverType("res"),"res-2",getReceiverConfiguration("res")), ReceiverStatus.DAMAGED);
        givenMap.put(ReceiverFactory.createReceiver(ReceiverType.DIPOLE,"buz-3",getReceiverConfiguration("buz")), ReceiverStatus.OFF);
        givenMap.put(ReceiverFactory.createReceiver(ReceiverType.DIPOLE,"led-1",getReceiverConfiguration("led")), ReceiverStatus.OPTIMAL);
        String actualString = Deserializer.deserialize(givenMap);

        assertEquals(Sets.newHashSet("buz-3:0", "led-1:2" ,"res-2:3"), Sets.newHashSet(actualString.split(",")));
    }

    @Test
    public void should_return_empty_string()
    {
        Map<Receiver,ReceiverStatus> givenMap = newHashMap();
        String actualString = Deserializer.deserialize(givenMap);

        String expectedString = "";

        assertEquals(expectedString, actualString);
    }

    @Test
    public void should_return_one_component_in_string()
    {
        Map<Receiver,ReceiverStatus> givenMap = newHashMap();
        givenMap.put(ReceiverFactory.createReceiver(ReceiverType.DIPOLE,"buz-3",getReceiverConfiguration("buz")), ReceiverStatus.LOW);
        String actualString = Deserializer.deserialize(givenMap);

        String expectedString = "buz-3:1";

        assertEquals(expectedString, actualString);
    }

}