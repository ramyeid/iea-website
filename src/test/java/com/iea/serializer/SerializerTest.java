package com.iea.serializer;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.*;
import com.iea.serializer.exception.PinDecodeError;
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

        Receiver led1 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE,"led-1", getReceiverConfiguration("led-1"));
        String s = "bat-0";
        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration(s));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
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

        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration("bat-0"));

        Circuit expectedCircuit = circuitBuilder
                .setGenerator(bat0)
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_that_circuit_can_be_created_and_wired_correctly_with_no_generator() {
        String generator = "";
        String receivers = "led-1,led-2";
        String connections = "led-1,+,led-2,+,led-1,-,led-2,-";
        Circuit testCircuit = Serializer.serialize(generator,receivers,connections);

        Receiver led1 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led-1", getReceiverConfiguration("led-1"));
        Receiver led2 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led-2", getReceiverConfiguration("led-2"));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .addReceiver(led1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(led1.getFirstPin(), led1), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(led2.getSecondPin(), led2), new Tuple<>(led1.getSecondPin(), led1))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_that_circuit_connects_three_components_series_correctly() {
        String generator = "bat-0";
        String receivers = "led-1,led-2";
        String connections = "bat-0,+,led-1,+,led-1,-,led-2,+,led-2,-,bat-0,-";
        Circuit testCircuit = Serializer.serialize(generator, receivers, connections);

        Receiver led1 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led-1", getReceiverConfiguration("led-1"));
        Receiver led2 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led-2", getReceiverConfiguration("led-2"));
        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration("bat-0"));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led1.getFirstPin(), led1))
                .connectComponents(new Tuple<>(led1.getSecondPin(), led1), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(led2.getSecondPin(), led2), new Tuple<>(bat0.getSecondPin(), bat0))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_that_circuit_connects_parallel() {
        String generator = "bat-0";
        String receivers = "led-1,led-2";
        String connections = "bat-0,+,led-1,+,bat-0,+,led-2,+,bat-0,-,led-1,-,bat-0,-,led-2,-";
        Circuit testCircuit = Serializer.serialize(generator, receivers, connections);

        Receiver led1 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led-1", getReceiverConfiguration("led-1"));
        Receiver led2 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE, "led-2", getReceiverConfiguration("led-2"));
        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration("bat-0"));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led1.getFirstPin(), led1))
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(bat0.getSecondPin(), bat0), new Tuple<>(led1.getSecondPin(), led1))
                .connectComponents(new Tuple<>(bat0.getSecondPin(), bat0), new Tuple<>(led2.getSecondPin(), led2))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test
    public void should_ensure_redundant_wiring_does_not_affect_result() {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,+,led-1,+,led-1,+,bat-0,+";
        Circuit testCircuit = Serializer.serialize(generator, receivers, connections);

        Receiver led1 = ReceiverFactory.createReceiver(ReceiverType.DIPOLE,"led-1", getReceiverConfiguration("led-1"));
        Generator bat0 = new Generator("bat-0", getGeneratorConfiguration("bat-0"));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led1.getFirstPin(), led1))
                .build();

        assertEquals(expectedCircuit, testCircuit);
    }

    @Test(expected=PinDecodeError.class)
    public void should_throw_pin_decode_error() {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,2,led-1,5,led-1,1,bat-0,4";
        Serializer.serialize(generator, receivers, connections);
    }

    private ReceiverConfiguration getReceiverConfiguration(String uid) {
        return Configurations.getReceiverConfiguration(uid.split(ID_TOKEN)[0]);
    }

    private GeneratorConfiguration getGeneratorConfiguration(String uid) {
        return Configurations.getGeneratorConfiguration(uid.split(ID_TOKEN)[0]);
    }

}

