package com.iea.serializer;

import com.iea.circuit.receiver.ReceiverStatus;

import java.util.Map;
import java.util.StringJoiner;

public class Deserializer {

    public static String deserialize(Map<String,ReceiverStatus> receiverStatusMap){
        StringJoiner deserializedStringBuilder = new StringJoiner(",");
        for (Map.Entry<String, ReceiverStatus> entry : receiverStatusMap.entrySet()) {
            deserializedStringBuilder.add(entry.getKey())
                                        .add(String.valueOf(entry.getValue().getIntValue()));
        }
        return deserializedStringBuilder.toString();
    }
}
