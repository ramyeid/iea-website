package com.iea.serializer;

import com.google.common.collect.Sets;
import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverConfiguration;
import com.iea.circuit.receiver.config.ReceiverStatus;
import com.iea.circuit.receiver.config.ReceiverType;
import com.iea.utils.Tuple;
import org.junit.Test;

import java.util.Map;

import static com.iea.circuit.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.receiver.ReceiverFactory.createReceiver;
import static com.iea.serializer.Configurations.getReceiverType;
import static com.iea.serializer.Serializer.serialize;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashMap;
import static org.junit.Assert.assertEquals;

public class SerializerTest {

    private static final String ID_TOKEN = "-";

    @Test
    public void should_ensure_that_empty_strings_return_empty_circuit() throws NoMatchingPinFoundException {
        String generator = "";
        String receivers = "";
        String connections = "";

        Circuit actualCircuit = Serializer.deSerialize(generator, receivers, connections);

        assertEquals(Circuit.Builder.newBuilder().build(), actualCircuit);
    }

    @Test
    public void should_ensure_that_circuit_creates_components_with_no_wiring() throws NoMatchingPinFoundException {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "";

        Circuit actualCircuit = Serializer.deSerialize(generator, receivers, connections);

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(createGenerator("bat-0", getGeneratorConfiguration("bat-0")))
                .addReceiver(createReceiver("led-1", ReceiverType.DIPOLE, getReceiverConfiguration("led-1")))
                .build();
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void should_ensure_generator_only_circuit_can_be_created() throws NoMatchingPinFoundException {
        String generator = "bat-0";
        String receivers = "";
        String connections = "";

        Circuit actualCircuit = Serializer.deSerialize(generator, receivers, connections);

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(createGenerator("bat-0", getGeneratorConfiguration("bat-0")))
                .build();
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void should_ensure_that_circuit_can_be_created_and_wired_correctly_with_no_generator() throws NoMatchingPinFoundException {
        String generator = "";
        String receivers = "led-1,led-2";
        String connections = "led-1,+,led-2,+,led-1,-,led-2,-";

        Circuit actualCircuit = Serializer.deSerialize(generator, receivers, connections);

        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, getReceiverConfiguration("led-1"));
        Receiver led2 = createReceiver("led-2", ReceiverType.DIPOLE, getReceiverConfiguration("led-2"));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .addReceiver(led1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(led1.getFirstPin(), led1), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(led2.getSecondPin(), led2), new Tuple<>(led1.getSecondPin(), led1))
                .build();
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void should_ensure_that_circuit_connects_three_components_series_correctly() throws NoMatchingPinFoundException {
        String generator = "bat-0";
        String receivers = "led-1,led-2";
        String connections = "bat-0,+,led-1,+,led-1,-,led-2,+,led-2,-,bat-0,-";

        Circuit actualCircuit = Serializer.deSerialize(generator, receivers, connections);

        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, getReceiverConfiguration("led-1"));
        Receiver led2 = createReceiver("led-2", ReceiverType.DIPOLE, getReceiverConfiguration("led-2"));
        Generator bat0 = createGenerator("bat-0", getGeneratorConfiguration("bat-0"));
        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led1.getFirstPin(), led1))
                .connectComponents(new Tuple<>(led1.getSecondPin(), led1), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(led2.getSecondPin(), led2), new Tuple<>(bat0.getSecondPin(), bat0))
                .build();
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void should_ensure_that_circuit_connects_parallel() throws NoMatchingPinFoundException {
        String generator = "bat-0";
        String receivers = "led-1,led-2";
        String connections = "bat-0,+,led-1,+,bat-0,+,led-2,+,bat-0,-,led-1,-,bat-0,-,led-2,-";

        Circuit actualCircuit = Serializer.deSerialize(generator, receivers, connections);

        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, getReceiverConfiguration("led-1"));
        Receiver led2 = createReceiver("led-2", ReceiverType.DIPOLE, getReceiverConfiguration("led-2"));
        Generator bat0 = createGenerator("bat-0", getGeneratorConfiguration("bat-0"));
        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .addReceiver(led2)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led1.getFirstPin(), led1))
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led2.getFirstPin(), led2))
                .connectComponents(new Tuple<>(bat0.getSecondPin(), bat0), new Tuple<>(led1.getSecondPin(), led1))
                .connectComponents(new Tuple<>(bat0.getSecondPin(), bat0), new Tuple<>(led2.getSecondPin(), led2))
                .build();
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void should_ensure_redundant_wiring_does_not_affect_result() throws NoMatchingPinFoundException {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,+,led-1,+,led-1,+,bat-0,+";

        Circuit actualCircuit = Serializer.deSerialize(generator, receivers, connections);

        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, getReceiverConfiguration("led-1"));
        Generator bat0 = createGenerator("bat-0", getGeneratorConfiguration("bat-0"));

        Circuit expectedCircuit = Circuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .connectComponents(new Tuple<>(bat0.getFirstPin(), bat0), new Tuple<>(led1.getFirstPin(), led1))
                .build();

        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test(expected = NoMatchingPinFoundException.class)
    public void should_throw_pin_decode_error() throws NoMatchingPinFoundException {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,2,led-1,5,led-1,1,bat-0,4";

        Serializer.deSerialize(generator, receivers, connections);
    }


    @Test
    public void should_return_correct_string() {
        Map<Receiver, ReceiverStatus> givenMap = newHashMap();
        givenMap.put(createReceiver("res-2", getReceiverType("res"), getReceiverConfiguration("res")), ReceiverStatus.DAMAGED);
        givenMap.put(createReceiver("buz-3", ReceiverType.DIPOLE, getReceiverConfiguration("buz")), ReceiverStatus.OFF);
        givenMap.put(createReceiver("led-1", ReceiverType.DIPOLE, getReceiverConfiguration("led")), ReceiverStatus.OPTIMAL);

        String actualString = serialize(givenMap);

        assertEquals(Sets.newHashSet("buz-3:0", "led-1:2", "res-2:3"), Sets.newHashSet(actualString.split(",")));
    }

    @Test
    public void should_return_empty_string() {
        Map<Receiver, ReceiverStatus> givenMap = newHashMap();

        String actualString = serialize(givenMap);

        String expectedString = "";
        assertEquals(expectedString, actualString);
    }

    @Test
    public void should_return_one_component_in_string() {
        Map<Receiver, ReceiverStatus> givenMap = newHashMap();
        givenMap.put(createReceiver("buz-3", ReceiverType.DIPOLE, getReceiverConfiguration("buz")), ReceiverStatus.LOW);

        String actualString = serialize(givenMap);

        String expectedString = "buz-3:1";
        assertEquals(expectedString, actualString);
    }

    private ReceiverConfiguration getReceiverConfiguration(String uid) {
        return Configurations.getReceiverConfiguration(uid.split(ID_TOKEN)[0]);
    }

    private GeneratorConfiguration getGeneratorConfiguration(String uid) {
        return Configurations.getGeneratorConfiguration(uid.split(ID_TOKEN)[0]);
    }

}

