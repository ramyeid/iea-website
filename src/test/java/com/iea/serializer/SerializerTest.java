package com.iea.serializer;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.utils.Tuple;
import org.junit.Test;
import sun.security.jgss.wrapper.GSSNameElement;

import java.util.ArrayList;

import static com.iea.serializer.Configurations.getGeneratorConfiguration;
import static com.iea.serializer.Configurations.getReceiverConfiguration;
import static org.junit.Assert.*;

public class SerializerTest {

    private final Serializer serializer = new Serializer();
    private static final String ID_TOKEN = "-";

    @Test
    public void should_ensure_that_empty_strings_return_empty_circuit() {
        String generator = "";
        String receivers = "";
        String connections = "";
        Circuit testCircuit = Serializer.serialize(generator,receivers,connections);
        assertNull(testCircuit.getGenerator());
        assertNull(testCircuit.getReceivers());
    }

    @Test
    public void should_ensure_that_circuit_connects_correctly() {
        String generator = "bat-0";
        String receivers = "res-1,led-2";
        String connections = "bat-0,+,res-1,+,res-1,-,led-2,+,led-2,-,bat-0,-";
        Circuit testCircuit = Serializer.serialize(generator,receivers,connections);
        ReceiverFactory receiverFactory = new ReceiverFactory();
        Circuit.Builder circuitBuilder = Circuit.Builder.newBuilder();

        DipoleReceiver resistor1 = receiverFactory.createDipoleReceiver("res-1",getReceiverConfiguration("res-1".split(ID_TOKEN)[0]));
        DipoleReceiver led2 = receiverFactory.createDipoleReceiver("led-2",getReceiverConfiguration("led-2".split(ID_TOKEN)[0]));
        Generator bat0 = new Generator("bat-0",getGeneratorConfiguration("bat-0".split(ID_TOKEN)[0]));

        Circuit comparisonCircuit = circuitBuilder
                .setGenerator((Generator)bat0)
                .addReceiver((Receiver)resistor1)
                .addReceiver((Receiver)led2)
                .connectComponents(new Tuple<>(bat0.getPositivePin(), bat0), new Tuple<>(resistor1.getNegativePin(), resistor1))
                .connectComponents(new Tuple<>(resistor1.getNegativePin(), resistor1), new Tuple<>(led2.getPositivePin(), led2))
                .connectComponents(new Tuple<>(led2.getNegativePin(), led2), new Tuple<>(bat0.getNegativePin(), bat0))
                .build();

        assertEquals(testCircuit, comparisonCircuit);
    }
}