package com.iea;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.config.Receiver;

import java.util.List;

public class Utils {

    public static Receiver retrieveReceiverWithId(String id, Circuit circuit) {
        return circuit.getReceivers().stream().filter(t-> t.getId().equals(id)).findFirst().orElseGet(null);
    }
}
