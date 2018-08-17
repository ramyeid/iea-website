package com.iea.jsmapping;

import com.iea.circuit.creator.factory.CircuitType;
import com.iea.circuit.model.Connection;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.component.generator.GeneratorFactory;
import com.iea.circuit.model.component.receiver.ReceiverFactory;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverType;
import com.iea.circuit.utils.exception.MatchingComponentNotFoundException;
import com.iea.jsmapping.exception.InvalidConnectionsStringException;
import com.iea.jsmapping.exception.NoMatchingPinFoundException;
import com.iea.jsmapping.exception.UnrecognisedCircuitTypeException;
import org.junit.Test;

import java.util.List;

import static com.iea.jsmapping.Configurations.getGeneratorConfiguration;
import static com.iea.jsmapping.Configurations.getReceiverConfiguration;
import static com.iea.jsmapping.JavascriptMapping.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class JavascriptMappingTest {

    private static final GeneratorConfiguration GPIO_CONFIGURATION = getGeneratorConfiguration("gpio");
    private static final GeneratorConfiguration BAT_CONFIGURATION = getGeneratorConfiguration("bat");
    private static final ReceiverConfiguration LED_CONFIGURATION = getReceiverConfiguration("led");
    private static final ReceiverConfiguration BUZ_CONFIGURATION = getReceiverConfiguration("buz");
    private static final ReceiverConfiguration RES_CONFIGURATION = getReceiverConfiguration("res");


    @Test
    public void string_to_generator_should_return_empty_generator_list() {
        String emptyGeneratorString = "";

        List<Generator> actualGeneratorList = stringToGeneratorList(emptyGeneratorString);

        assertEquals(emptyList(), actualGeneratorList);
    }

    @Test
    public void string_to_generator_should_return_one_generator_in_list() {
        String generatorString = "gpio-3";

        List<Generator> actualGeneratorList = stringToGeneratorList(generatorString);

        List<Generator> expectedGeneratorList = singletonList(GeneratorFactory.createGenerator("gpio-3", GPIO_CONFIGURATION));
        assertEquals(expectedGeneratorList, actualGeneratorList);
    }

    @Test
    public void string_to_generator_should_return_five_generators_in_list() {
        String generatorString = "gpio-3,gpio-5,gpio-7,gpio-11,gpio-40";
        Generator generator1 = GeneratorFactory.createGenerator("gpio-3", GPIO_CONFIGURATION);
        Generator generator2 = GeneratorFactory.createGenerator("gpio-5", GPIO_CONFIGURATION);
        Generator generator3 = GeneratorFactory.createGenerator("gpio-7", GPIO_CONFIGURATION);
        Generator generator4 = GeneratorFactory.createGenerator("gpio-11", GPIO_CONFIGURATION);
        Generator generator5 = GeneratorFactory.createGenerator("gpio-40", GPIO_CONFIGURATION);

        List<Generator> actualGeneratorList = stringToGeneratorList(generatorString);

        List<Generator> expectedGeneratorList = asList(generator1, generator2, generator3, generator4, generator5);
        assertEquals(expectedGeneratorList, actualGeneratorList);
    }

    @Test
    public void string_to_receiver_should_return_empty_receiver_list() {
        String emptyReceiversString = "";

        List<Receiver> actualReceiversList = stringToReceiversList(emptyReceiversString);

        assertEquals(emptyList(), actualReceiversList);
    }

    @Test
    public void string_to_receivers_should_return_one_receiver_in_list() {
        String receiverString = "led-1";

        List<Receiver> actualReceiversList = stringToReceiversList(receiverString);

        List<Receiver> expectedReceiversList = singletonList(ReceiverFactory.createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION));
        assertEquals(expectedReceiversList, actualReceiversList);
    }

    @Test
    public void string_to_receivers_should_return_five_receiver_in_list() {
        String receiverString = "led-0,buz-1,res-2,led-3,led-4";
        Receiver receiver1 = ReceiverFactory.createReceiver("led-0", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver receiver2 = ReceiverFactory.createReceiver("buz-1", ReceiverType.DIPOLE, BUZ_CONFIGURATION);
        Receiver receiver3 = ReceiverFactory.createReceiver("res-2", ReceiverType.RESISTOR, RES_CONFIGURATION);
        Receiver receiver4 = ReceiverFactory.createReceiver("led-3", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver receiver5 = ReceiverFactory.createReceiver("led-4", ReceiverType.DIPOLE, LED_CONFIGURATION);

        List<Receiver> actualReceiversList = stringToReceiversList(receiverString);

        List<Receiver> expectedReceiversList = asList(receiver1, receiver2, receiver3, receiver4, receiver5);
        assertEquals(expectedReceiversList, actualReceiversList);
    }

    @Test
    public void string_to_connections_should_return_empty_list() throws InvalidConnectionsStringException, MatchingComponentNotFoundException, NoMatchingPinFoundException {
        Generator generator = GeneratorFactory.createGenerator("bat-0", BAT_CONFIGURATION);
        List<Receiver> receivers = asList(
                ReceiverFactory.createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION),
                ReceiverFactory.createReceiver("buz-2", ReceiverType.DIPOLE, BUZ_CONFIGURATION));
        String connectionString = "";

        List<Connection> actualConnectionsList = stringToConnectionsList(connectionString, receivers, singletonList(generator));

        assertEquals(emptyList(), actualConnectionsList);
    }

    @Test
    public void string_to_connections_should_return_one_connection_in_list() throws InvalidConnectionsStringException, MatchingComponentNotFoundException, NoMatchingPinFoundException {
        Generator generator0 = GeneratorFactory.createGenerator("bat-0", BAT_CONFIGURATION);
        Receiver receiver1 = ReceiverFactory.createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver receiver2 = ReceiverFactory.createReceiver("buz-2", ReceiverType.DIPOLE, BUZ_CONFIGURATION);
        List<Generator> generatorList = singletonList(generator0);
        List<Receiver> receiverList = asList(receiver1, receiver2);
        String connectionString = "led-1,+,bat-0,+";

        List<Connection> actualConnectionsList = stringToConnectionsList(connectionString, receiverList, generatorList);

        List<Connection> expectedConnectionsList = singletonList(new Connection(receiver1, receiver1.getFirstPin(), generator0, generator0.getFirstPin()));
        assertEquals(expectedConnectionsList, actualConnectionsList);
    }

    @Test
    public void string_to_connections_should_return_three_connections_in_list() throws InvalidConnectionsStringException, MatchingComponentNotFoundException, NoMatchingPinFoundException {
        Generator generator0 = GeneratorFactory.createGenerator("bat-0", BAT_CONFIGURATION);
        Receiver receiver1 = ReceiverFactory.createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver receiver2 = ReceiverFactory.createReceiver("buz-2", ReceiverType.DIPOLE, BUZ_CONFIGURATION);
        List<Generator> generatorList = singletonList(generator0);
        List<Receiver> receiverList = asList(receiver1, receiver2);
        String connectionString = "led-1,+,bat-0,+;led-1,-,buz-2,+;buz-2,-,bat-0,-";

        List<Connection> actualConnectionsList = stringToConnectionsList(connectionString, receiverList, generatorList);

        List<Connection> expectedConnectionsList = asList(
                new Connection(receiver1, receiver1.getFirstPin(), generator0, generator0.getFirstPin()),
                new Connection(receiver1, receiver1.getSecondPin(), receiver2, receiver2.getFirstPin()),
                new Connection(receiver2, receiver2.getSecondPin(), generator0, generator0.getSecondPin())
        );
        assertEquals(expectedConnectionsList, actualConnectionsList);
    }

    @Test(expected = InvalidConnectionsStringException.class)
    public void string_to_connections_should_throw_invalid_connections_string_exception() throws InvalidConnectionsStringException, MatchingComponentNotFoundException, NoMatchingPinFoundException {
        Generator generator = GeneratorFactory.createGenerator("bat-0", BAT_CONFIGURATION);
        Receiver receiver = ReceiverFactory.createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        List<Generator> generatorList = singletonList(generator);
        List<Receiver> receiverList = singletonList(receiver);
        String connectionString = "led-1,+,bat-0,+,led-1,-;bat-0,+";

        stringToConnectionsList(connectionString, receiverList, generatorList);
    }

    @Test(expected = MatchingComponentNotFoundException.class)
    public void string_to_connections_should_throw_matching_component_not_found() throws InvalidConnectionsStringException, MatchingComponentNotFoundException, NoMatchingPinFoundException {
        Generator generator = GeneratorFactory.createGenerator("bat-0", BAT_CONFIGURATION);
        Receiver receiver = ReceiverFactory.createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        List<Generator> generatorList = singletonList(generator);
        List<Receiver> receiverList = singletonList(receiver);

        String connectionString = "led-1,+,bat-0,+;led-1,-,res-2,~1";

        stringToConnectionsList(connectionString, receiverList, generatorList);
    }

    @Test(expected = NoMatchingPinFoundException.class)
    public void string_to_connections_should_throw_no_matching_pin() throws InvalidConnectionsStringException, MatchingComponentNotFoundException, NoMatchingPinFoundException {
        Generator generator = GeneratorFactory.createGenerator("bat-0", BAT_CONFIGURATION);
        Receiver receiver = ReceiverFactory.createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        List<Generator> generatorList = singletonList(generator);
        List<Receiver> receiverList = singletonList(receiver);
        String connectionString = "led-1,+,bat-0,X";

        stringToConnectionsList(connectionString, receiverList, generatorList);
    }

    @Test
    public void string_to_connection_type_should_return_default_circuit() throws UnrecognisedCircuitTypeException {
        String receivedString = "DEFAULT";

        CircuitType actual = stringToCircuitType(receivedString);

        assertEquals(CircuitType.DEFAULT_CIRCUIT, actual);
    }

    @Test
    public void string_to_connection_type_should_return_rpi_circuit() throws UnrecognisedCircuitTypeException {
        String receivedString = "RPI";

        CircuitType actual = stringToCircuitType(receivedString);

        assertEquals(CircuitType.RPI_CIRCUIT, actual);
    }

    @Test(expected = UnrecognisedCircuitTypeException.class)
    public void string_to_connection_type_should_throw_unrecognised_circuit_type_exception() throws UnrecognisedCircuitTypeException {
        String receivedString = "BATTERY";

        stringToCircuitType(receivedString);
    }
}