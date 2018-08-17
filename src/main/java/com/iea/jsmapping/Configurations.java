package com.iea.jsmapping;


import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverType;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;


public class Configurations {

    private static final Map<String, ReceiverConfiguration> receiverIdToConfiguration = newHashMap();
    private static final Map<String, GeneratorConfiguration> generatorIdToConfiguration = newHashMap();
    private static final Map<String, ReceiverType> receiverIdToType = newHashMap();


    static {
        receiverIdToConfiguration.put("led", new ReceiverConfiguration(0.02, 1.3, 2.5, 100));
        receiverIdToConfiguration.put("buz", new ReceiverConfiguration(0.03, 2, 2.5, 120));
        receiverIdToConfiguration.put("res", new ReceiverConfiguration(10, 1.5, 2, 150));

        generatorIdToConfiguration.put("bat", new GeneratorConfiguration(1, 5));
        generatorIdToConfiguration.put("rpiV3", new GeneratorConfiguration(0.03, 3.3));
        generatorIdToConfiguration.put("rpiV5", new GeneratorConfiguration(0.5, 5));
        generatorIdToConfiguration.put("gpio", new GeneratorConfiguration(0.03, 3.3));

        receiverIdToType.put("led", ReceiverType.DIPOLE);
        receiverIdToType.put("buz", ReceiverType.DIPOLE);
        receiverIdToType.put("res", ReceiverType.RESISTOR);
    }

    static ReceiverConfiguration getReceiverConfiguration(String id) {
        return receiverIdToConfiguration.get(id);
    }

    static GeneratorConfiguration getGeneratorConfiguration(String id) {
        return generatorIdToConfiguration.get(id);
    }

    static ReceiverType getReceiverType(String id) {
        return receiverIdToType.get(id);
    }

}
