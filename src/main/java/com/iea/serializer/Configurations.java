package com.iea.serializer;

import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.ReceiverConfiguration;

import java.util.Map;

import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;

public class Configurations {

    private static final Map<String, ReceiverConfiguration> receiverIdToConfiguration = newHashMap();
    private static final Map<String, GeneratorConfiguration> generatorIdToConfiguration = newHashMap();

    static {
        receiverIdToConfiguration.put("led", new ReceiverConfiguration(20, 1.5, 2.5, 48));
        receiverIdToConfiguration.put("buz", new ReceiverConfiguration(30, 2, 2.5, 30));
        receiverIdToConfiguration.put("res", new ReceiverConfiguration(30, 2, 2.5, 30));

        generatorIdToConfiguration.put("bat", new GeneratorConfiguration(40, 5));
    }


    public static ReceiverConfiguration getReceiverConfiguration(String id) {
        return receiverIdToConfiguration.get(id);
    }

    public static GeneratorConfiguration getGeneratorConfiguration(String id) {
        return generatorIdToConfiguration.get(id);
    }

}
