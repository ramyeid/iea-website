package com.iea.serializer;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.utils.Tuple;
import org.junit.Test;

import static com.iea.serializer.Configurations.getGeneratorConfiguration;
import static com.iea.serializer.Configurations.getReceiverConfiguration;
import static org.junit.Assert.assertEquals;

public class SerializerTest {

    private static final String ID_TOKEN = "-";

    @Test
    public void should_ensure_that_empty_strings_return_empty_circuit() {
        String generator = "";
        String receivers = "";
        String connections = "";

        Circuit testCircuit = Serializer.serialize(generator,receivers,connections);

        Circuit expectedCircuit = Circuit.Builder.newBuilder().build();
        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_that_circuit_creates_components_with_no_wiring() {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "";

        Circuit testCircuit = Serializer.serialize(generator,receivers,connections);
        ReceiverFactory receiverFactory = new ReceiverFactory();
        Circuit.Builder circuitBuilder = Circuit.Builder.newBuilder();

        DipoleReceiver led1 = receiverFactory.createDipoleReceiver("led-1",getReceiverConfiguration("led-1".split(ID_TOKEN)[0]));
        Generator bat0 = new Generator("bat-0",getGeneratorConfiguration("bat-0".split(ID_TOKEN)[0]));

        Circuit expectedCircuit = circuitBuilder
                .setGenerator(bat0)
                .addReceiver(led1)
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_generator_only_circuit_can_be_created() {
        String generator = "bat-0";
        String receivers = "";
        String connections = "";

        Circuit testCircuit = Serializer.serialize(generator,receivers,connections);
        Circuit.Builder circuitBuilder = Circuit.Builder.newBuilder();

        Generator bat0 = new Generator("bat-0",getGeneratorConfiguration("bat-0".split(ID_TOKEN)[0]));

        Circuit expectedCircuit = circuitBuilder
                .setGenerator(bat0)
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_that_circuit_can_be_created_and_wired_correctly_with_no_generator() {
        String generator = "";
        String receivers = "res-1,led-2";
        String connections = "res-1,+,led-2,+,res-1,-,led-2,-";
        Circuit testCircuit = Serializer.serialize(generator,receivers,connections);
        ReceiverFactory receiverFactory = new ReceiverFactory();
        Circuit.Builder circuitBuilder = Circuit.Builder.newBuilder();

        DipoleReceiver resistor1 = receiverFactory.createDipoleReceiver("res-1",getReceiverConfiguration("res-1".split(ID_TOKEN)[0]));
        DipoleReceiver led2 = receiverFactory.createDipoleReceiver("led-2",getReceiverConfiguration("led-2".split(ID_TOKEN)[0]));

        Circuit expectedCircuit = circuitBuilder
                .addReceiver(resistor1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(resistor1.getFirstPin(), resistor1), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(led2.getSecondPin(), led2), new Tuple<>(resistor1.getSecondPin(), resistor1))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_that_circuit_connects_three_components_series_correctly() {
        String generator = "bat-0";
        String receivers = "res-1,led-2";
        String connections = "bat-0,+,res-1,+,res-1,-,led-2,+,led-2,-,bat-0,-";
        Circuit testCircuit = Serializer.serialize(generator, receivers, connections);
        ReceiverFactory receiverFactory = new ReceiverFactory();

        DipoleReceiver resistor1 = receiverFactory.createDipoleReceiver("res-1", getReceiverConfiguration("res-1".split(ID_TOKEN)[0]));
        DipoleReceiver led2 = receiverFactory.createDipoleReceiver("led-2", getReceiverConfiguration("led-2".split(ID_TOKEN)[0]));
        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration("bat-0".split(ID_TOKEN)[0]));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(resistor1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(resistor1.getFirstPin(), resistor1))
                .connectComponents(new Tuple<>(resistor1.getSecondPin(), resistor1), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(led2.getSecondPin(), led2), new Tuple<>(bat0.getSecondPin(), bat0))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_that_circuit_connects_parallel() {
        String generator = "bat-0";
        String receivers = "res-1,led-2";
        String connections = "bat-0,+,res-1,+,bat-0,+,led-2,+,bat-0,-,res-1,-,bat-0,-,led-2,-";
        Circuit testCircuit = Serializer.serialize(generator, receivers, connections);
        ReceiverFactory receiverFactory = new ReceiverFactory();

        DipoleReceiver resistor1 = receiverFactory.createDipoleReceiver("res-1", getReceiverConfiguration("res-1".split(ID_TOKEN)[0]));
        DipoleReceiver led2 = receiverFactory.createDipoleReceiver("led-2", getReceiverConfiguration("led-2".split(ID_TOKEN)[0]));
        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration("bat-0".split(ID_TOKEN)[0]));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(resistor1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(resistor1.getFirstPin(), resistor1))
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(bat0.getSecondPin(), bat0), new Tuple<>(resistor1.getSecondPin(), resistor1))
                .connectComponents(new Tuple<>(bat0.getSecondPin(), bat0), new Tuple<>(led2.getSecondPin(), led2))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_redundant_wiring_does_not_affect_result() {
        String generator = "bat-0";
        String receivers = "res-1";
        String connections = "bat-0,+,res-1,+,res-1,+,bat-0,+";
        Circuit testCircuit = Serializer.serialize(generator, receivers, connections);
        ReceiverFactory receiverFactory = new ReceiverFactory();

        DipoleReceiver resistor1 = receiverFactory.createDipoleReceiver("res-1", getReceiverConfiguration("res-1".split(ID_TOKEN)[0]));
        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration("bat-0".split(ID_TOKEN)[0]));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(resistor1)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(resistor1.getFirstPin(), resistor1))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

}

