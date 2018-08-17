package com.iea.jsmapping;

import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.StringJoiner;

public class Serializer {

    private static final Logger LOGGER = LogManager.getLogger(Serializer.class);

    /**
     * Function used to transform a receiver to receiverStatus map into a string
     * with the following format: receiver1Id,receiver1Status,receiver2Id,receiver2Status, ...
     *
     * @param receiverStatusMap Map containing receiver to receiverStatus mappings
     */
    public static String serialize(Map<Receiver, ReceiverStatus> receiverStatusMap) {
        LOGGER.info("------------ Serializer Started ------------ ");
        StringJoiner deserializedStringBuilder = new StringJoiner(",");
        receiverStatusMap.forEach((key, value) -> deserializedStringBuilder.add(key.getId() + ":" + String.valueOf(value.getIntValue())));
        String deserialzedStringBuilderValue = deserializedStringBuilder.toString();
        LOGGER.info("Serialized string: " + deserialzedStringBuilderValue);
        LOGGER.info("------------ Serializer Finished ------------ ");
        return deserialzedStringBuilderValue;
    }
}