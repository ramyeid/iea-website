package com.iea.serializer;

import com.iea.circuit.receiver.*;
import org.junit.Test;

import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;
import static org.junit.Assert.*;

public class DeserializerTest {

    @Test
    public void should_return_correct_string()
    {
        Map<String,ReceiverStatus> givenMap = newHashMap();
        givenMap.put("res-2", ReceiverStatus.DAMAGED);
        givenMap.put ("buz-3", ReceiverStatus.OFF);
        givenMap.put("led-1", ReceiverStatus.OPTIMAL);
        String actualString = Deserializer.deserialize(givenMap);

        String expectedString = "res-2,3,buz-3,0,led-1,2";

        assertEquals(expectedString, actualString);
    }

    @Test
    public void should_return_empty_string()
    {
        Map<String,ReceiverStatus> givenMap = newHashMap();
        String actualString = Deserializer.deserialize(givenMap);

        String expectedString = "";

        assertEquals(expectedString, actualString);
    }

}