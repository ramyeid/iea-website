package com.iea.serializer;

import com.iea.circuit.Circuit;
import com.iea.circuit.receiver.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Serializer {
    private static final Map<String, Configuration> idToConfig = Collections.unmodifiableMap(
            new HashMap<String, Configuration>() {{
                put("led", new Configuration(20,1.5,2.5,48));
                put("buz", new Configuration(30,2,2.5,30));
                put("res", new Configuration(30,2,2.5,30));
            }});

    public static Circuit serialize(String id, String components, String connections)
    {
        Circuit.Builder circuitBuilder = new Circuit.Builder();

        return circuitBuilder.build();
    }
}
