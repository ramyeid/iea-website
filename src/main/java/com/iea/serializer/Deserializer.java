package com.iea.serializer;

import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;

import java.util.Map;
import java.util.StringJoiner;

public class Deserializer {

    /**
     * Function used to transform a receiver to receiverStatus map into a string
     * with the following format: receiver1Id,receiver1Status,receiver2Id,receiver2Status, ...
     * @param receiverStatusMap Map containing receiver to receiverStatus mappings
     */
    public static String deserialize(Map<Receiver,ReceiverStatus> receiverStatusMap){
        StringJoiner deserializedStringBuilder = new StringJoiner(",");
        receiverStatusMap.forEach((key, value) -> deserializedStringBuilder.add(key.getId() + ":" +  String.valueOf(value.getIntValue())));
        return deserializedStringBuilder.toString();
    }
}
