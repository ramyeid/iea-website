/**
 * Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 * <p>
 * This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 * express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.creator;

import com.iea.circuit.Circuit;
import com.iea.circuit.RpiCircuit;
import com.iea.circuit.creator.exception.DuplicateReceiversInCircuitsException;
import com.iea.circuit.creator.exception.InvalidNumberOfGeneratorException;
import com.iea.circuit.model.Connection;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverType;
import com.iea.circuit.utils.exception.MatchingComponentNotFoundException;
import com.iea.jsmapping.exception.InvalidConnectionsStringException;
import com.iea.jsmapping.exception.NoMatchingPinFoundException;
import org.junit.Test;

import static com.google.common.collect.Lists.newArrayList;
import static com.iea.circuit.model.component.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.model.component.receiver.ReceiverFactory.createReceiver;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;


public class RpiCircuitCreatorTest {

    private static final RpiCircuitCreator RPI_CIRCUIT_CREATOR = new RpiCircuitCreator();
    private static final GeneratorConfiguration GPIO_CONFIGURATION = new GeneratorConfiguration(0.03, 3.3);
    private static final ReceiverConfiguration LED_CONFIGURATION = new ReceiverConfiguration(0.02, 1.3, 2.5, 100);
    private static final ReceiverConfiguration RESISTOR_CONFIGURATION = new ReceiverConfiguration(10, 1.5, 2, 150);

    @Test(expected = InvalidNumberOfGeneratorException.class)
    public void rpi_creator_should_ensure_that_empty_throw_error() throws Exception {
        String generator = "";
        String receivers = "";
        String connections = "";

        Circuit actualCircuit = RPI_CIRCUIT_CREATOR.create(generator, receivers, connections);

        assertEquals(new RpiCircuit(newArrayList()), actualCircuit);
    }

    @Test(expected = NoMatchingPinFoundException.class)
    public void rpi_creator_should_throw_pin_decode_error() throws Exception {
        String generator = "gpio-3";
        String receivers = "led-1";
        String connections = "gpio-3,2,led-1,5;led-1,1,gpio-3,4";

        RPI_CIRCUIT_CREATOR.create(generator, receivers, connections);
    }

    @Test
    public void rpi_creator_should_create_three_seperate_circuits() throws Exception {
        String generators = "gpio-3,gpio-40,gpio-5";
        String receivers = "led-0,res-1,res-2";
        String connections = "res-1,~2,gpio-3,+;res-1,~1,led-0,+;led-0,-,gnd,-;gpio-40,+,res-2,~2;res-2,~1,gnd,-";

        Circuit actualCircuit = RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);

        Generator gpio3 = createGenerator("gpio-3", GPIO_CONFIGURATION);
        Generator gpio40 = createGenerator("gpio-40", GPIO_CONFIGURATION);
        Generator gpio5 = createGenerator("gpio-5", GPIO_CONFIGURATION);
        Receiver res1 = createReceiver("res-1", ReceiverType.RESISTOR, RESISTOR_CONFIGURATION);
        Receiver led0 = createReceiver("led-0", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver res2 = createReceiver("res-2", ReceiverType.RESISTOR, RESISTOR_CONFIGURATION);
        SimpleCircuit simpleCircuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio3).addReceiver(res1).addReceiver(led0).connectComponents(new Connection(res1, res1.getSecondPin(), gpio3, gpio3.getPositivePin()))
                .connectComponents(new Connection(res1, res1.getFirstPin(), led0, led0.getFirstPin())).connectComponents(new Connection(res1, res1.getSecondPin(), gpio3, gpio3.getPositivePin())).connectComponents(new Connection(
                        led0, led0.getSecondPin(), gpio3, gpio3.getNegativePin())).connectComponents(new Connection(led0, led0.getSecondPin(), gpio40, gpio40.getNegativePin())).connectComponents(new Connection(led0,
                        led0.getSecondPin(), gpio5, gpio5.getNegativePin())).addAllReceivers(emptyList()).build();
        SimpleCircuit simpleCircuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio40).addReceiver(res2).connectComponents(new Connection(gpio40, gpio40.getPositivePin(), res2, res2.getSecondPin())).connectComponents(
                new Connection(res2, res2.getFirstPin(), gpio3, gpio3.getNegativePin())).connectComponents(new Connection(res2, res2.getFirstPin(), gpio40, gpio40.getNegativePin())).connectComponents(new Connection(res2,
                res2.getFirstPin(), gpio5, gpio5.getNegativePin())).addAllReceivers(emptyList()).build();
        SimpleCircuit simpleCircuit3 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio5).addAllReceivers(emptyList()).build();
        assertEquals(new RpiCircuit(asList(simpleCircuit1, simpleCircuit2, simpleCircuit3)), actualCircuit);
    }

    @Test
    public void rpi_creator_should_create_five_seperate_circuits() throws Exception {
        String generators = "gpio-3,gpio-40,gpio-5,gpio-7,gpio-10";
        String receivers = "led-0,led-1,led-2,led-3,led-4";
        String connections = "led-0,+,gpio-3,+;led-1,+,gpio-40,+;led-2,+,gpio-5,+;led-3,+,gpio-7,+;led-4,+,gpio-10,+;led-0,-,gnd,-;led-1,-,gnd,-;led-2,-,gnd,-;led-3,-,gnd,-;led-4,-,gnd,-";

        Circuit actualCircuit = RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);

        Generator gpio3 = createGenerator("gpio-3", GPIO_CONFIGURATION);
        Generator gpio40 = createGenerator("gpio-40", GPIO_CONFIGURATION);
        Generator gpio5 = createGenerator("gpio-5", GPIO_CONFIGURATION);
        Generator gpio7 = createGenerator("gpio-7", GPIO_CONFIGURATION);
        Generator gpio10 = createGenerator("gpio-10", GPIO_CONFIGURATION);

        Receiver led0 = createReceiver("led-0", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver led2 = createReceiver("led-2", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver led3 = createReceiver("led-3", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver led4 = createReceiver("led-4", ReceiverType.DIPOLE, LED_CONFIGURATION);

        SimpleCircuit simpleCircuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio3).addReceiver(led0).connectComponents(new Connection(led0, led0.getFirstPin(), gpio3, gpio3.getPositivePin())).connectComponents(
                new Connection(led0, led0.getSecondPin(), gpio3, gpio3.getNegativePin())).connectComponents(new Connection(led0, led0.getSecondPin(), gpio40, gpio40.getNegativePin())).connectComponents(new Connection(led0,
                led0.getSecondPin(), gpio5, gpio5.getNegativePin())).connectComponents(new Connection(led0, led0.getSecondPin(), gpio7, gpio7.getNegativePin())).connectComponents(new Connection(led0, led0.getSecondPin(),
                gpio10, gpio10.getNegativePin())).build();
        SimpleCircuit simpleCircuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio40).addReceiver(led1).connectComponents(new Connection(led1, led1.getFirstPin(), gpio40, gpio40.getPositivePin())).connectComponents(
                new Connection(led1, led1.getSecondPin(), gpio3, gpio3.getNegativePin())).connectComponents(new Connection(led1, led1.getSecondPin(), gpio40, gpio40.getNegativePin())).connectComponents(new Connection(led1,
                led1.getSecondPin(), gpio5, gpio5.getNegativePin())).connectComponents(new Connection(led1, led1.getSecondPin(), gpio7, gpio7.getNegativePin())).connectComponents(new Connection(led1, led1.getSecondPin(),
                gpio10, gpio10.getNegativePin())).build();
        SimpleCircuit simpleCircuit3 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio5).addReceiver(led2).connectComponents(new Connection(led2, led2.getFirstPin(), gpio5, gpio5.getPositivePin())).connectComponents(
                new Connection(led2, led2.getSecondPin(), gpio3, gpio3.getNegativePin())).connectComponents(new Connection(led2, led2.getSecondPin(), gpio40, gpio40.getNegativePin())).connectComponents(new Connection(led2,
                led2.getSecondPin(), gpio5, gpio5.getNegativePin())).connectComponents(new Connection(led2, led2.getSecondPin(), gpio7, gpio7.getNegativePin())).connectComponents(new Connection(led2, led2.getSecondPin(),
                gpio10, gpio10.getNegativePin())).build();
        SimpleCircuit simpleCircuit4 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio7).addReceiver(led3).connectComponents(new Connection(led3, led3.getFirstPin(), gpio7, gpio7.getPositivePin())).connectComponents(
                new Connection(led3, led3.getSecondPin(), gpio3, gpio3.getNegativePin())).connectComponents(new Connection(led3, led3.getSecondPin(), gpio40, gpio40.getNegativePin())).connectComponents(new Connection(led3,
                led3.getSecondPin(), gpio5, gpio5.getNegativePin())).connectComponents(new Connection(led3, led3.getSecondPin(), gpio7, gpio7.getNegativePin())).connectComponents(new Connection(led3, led3.getSecondPin(),
                gpio10, gpio10.getNegativePin())).build();
        SimpleCircuit simpleCircuit5 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio10).addReceiver(led4).connectComponents(new Connection(led4, led4.getFirstPin(), gpio10, gpio10.getPositivePin())).connectComponents(
                new Connection(led4, led4.getSecondPin(), gpio3, gpio3.getNegativePin())).connectComponents(new Connection(led4, led4.getSecondPin(), gpio40, gpio40.getNegativePin())).connectComponents(new Connection(led4,
                led4.getSecondPin(), gpio5, gpio5.getNegativePin())).connectComponents(new Connection(led4, led4.getSecondPin(), gpio7, gpio7.getNegativePin())).connectComponents(new Connection(led4, led4.getSecondPin(),
                gpio10, gpio10.getNegativePin())).build();
        assertEquals(new RpiCircuit(asList(simpleCircuit1, simpleCircuit2, simpleCircuit3, simpleCircuit4, simpleCircuit5)), actualCircuit);
    }

    @Test
    public void rpi_creator_should_create_one_gpio_circuit() throws Exception {
        String generators = "gpio-3";
        String receivers = "led-0";
        String connections = "gpio-3,+,led-0,+;led-0,-,gnd,-";

        Circuit actualCircuit = RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);

        Generator gpio3 = createGenerator("gpio-3", GPIO_CONFIGURATION);
        Receiver led0 = createReceiver("led-0", ReceiverType.DIPOLE, LED_CONFIGURATION);
        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder().setGenerator(gpio3).addReceiver(led0).connectComponents(new Connection(led0, led0.getFirstPin(), gpio3, gpio3.getPositivePin())).connectComponents(
                new Connection(led0, led0.getSecondPin(), gpio3, gpio3.getNegativePin())).build();
        assertEquals(new RpiCircuit(singletonList(simpleCircuit)), actualCircuit);

    }

    @Test
    public void rpi_creator_should_create_one_gpio_circuit_ignoring_redundant_wiring() throws Exception {
        String generators = "gpio-3";
        String receivers = "led-0";
        String connections = "gnd,-,led-0,-;gpio-3,+,led-0,+;led-0,-,gnd,-;led-0,+,gpio-3,+";

        Circuit actualCircuit = RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);

        Generator gpio3 = createGenerator("gpio-3", GPIO_CONFIGURATION);
        Receiver led0 = createReceiver("led-0", ReceiverType.DIPOLE, LED_CONFIGURATION);
        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder().setGenerator(gpio3).addReceiver(led0).connectComponents(new Connection(led0, led0.getFirstPin(), gpio3, gpio3.getPositivePin())).connectComponents(
                new Connection(led0, led0.getSecondPin(), gpio3, gpio3.getNegativePin())).build();
        assertEquals(new RpiCircuit(singletonList(simpleCircuit)), actualCircuit);

    }

    @Test
    public void rpi_creator_should_create_one_circuit_with_one_generator() throws Exception {
        String generators = "gpio-7";
        String receivers = "";
        String connections = "";

        Circuit actualCircuit = RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);

        Generator gpio7 = createGenerator("gpio-7", GPIO_CONFIGURATION);
        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder().setGenerator(gpio7).addAllReceivers(emptyList()).build();
        assertEquals(new RpiCircuit(singletonList(simpleCircuit)), actualCircuit);

    }

    @Test
    public void pi_rpi_creator_should_create_empty_circuits() throws Exception {
        String generators = "gpio-3,gpio-40,gpio-5";
        String receivers = "";
        String connections = "";

        Circuit actualCircuit = RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);

        Generator gpio3 = createGenerator("gpio-3", GPIO_CONFIGURATION);
        Generator gpio40 = createGenerator("gpio-40", GPIO_CONFIGURATION);
        Generator gpio5 = createGenerator("gpio-5", GPIO_CONFIGURATION);
        SimpleCircuit simpleCircuit1 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio3).addAllReceivers(emptyList()).build();
        SimpleCircuit simpleCircuit2 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio40).addAllReceivers(emptyList()).build();
        SimpleCircuit simpleCircuit3 = SimpleCircuit.Builder.newBuilder().setGenerator(gpio5).addAllReceivers(emptyList()).build();

        assertEquals(new RpiCircuit(asList(simpleCircuit1, simpleCircuit2, simpleCircuit3)), actualCircuit);
    }

    @Test(expected = InvalidNumberOfGeneratorException.class)
    public void rpi_creator_should_throw_invalid_number_of_generators_for_no_generator_circuit() throws Exception {
        String generators = "";
        String receivers = "res-0,led-1";
        String connections = "res-0,~1,led-1,+;res-0,~2,led-1,-";

        RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);
    }

    @Test(expected = InvalidConnectionsStringException.class)
    public void rpi_creator_should_throw_invalid_connections_string_exception_due_to_bad_formatting() throws Exception {
        String generators = "gpio-3,gpio-5";
        String receivers = "res-0,led-1";
        String connections = "res-0,~1;led-1,+;res-0,~2;led-1,-";

        RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);
    }

    @Test(expected = MatchingComponentNotFoundException.class)
    public void rpi_creator_should_throw_matching_component_not_found_exception() throws Exception {
        String generators = "gpio-3";
        String receivers = "res-0";
        String connections = "gpio-3,+,buz-1,+";

        RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);
    }

    @Test(expected = DuplicateReceiversInCircuitsException.class)
    public void should_throw_duplicate_receivers_in_circuitsException_if_receiver_in_multiple_circuits() throws NoMatchingPinFoundException, InvalidConnectionsStringException, InvalidNumberOfGeneratorException, MatchingComponentNotFoundException, DuplicateReceiversInCircuitsException {
        String generators = "gpio-3,gpio-40,gpio-5";
        String receivers = "led-0,res-1";
        String connections = "res-1,~2,gpio-3,+;res-1,~1,led-0,+;led-0,-,gnd,-;gpio-40,+,res-1,~2;res-1,~1,gnd,-";

        RPI_CIRCUIT_CREATOR.create(generators, receivers, connections);
    }

    //TODO add tests similar to should_ensure_circuit_is_created_with_receivers_in_closed_circuit_with_generator and tests that follows.
    //TODO that test that if there's a component that is not in a closed circuit it is not inserted n the circuit and that this if this is connected to an obbject that is connected to generator the connection is cleaned.
}
