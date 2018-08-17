/**
 * Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 * <p>
 * This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 * express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.creator;

import com.iea.circuit.Circuit;
import com.iea.circuit.DefaultCircuit;
import com.iea.circuit.creator.exception.InvalidNumberOfGeneratorException;
import com.iea.circuit.model.Connection;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverType;
import com.iea.circuit.utils.exception.MatchingComponentNotFoundException;
import com.iea.jsmapping.exception.NoMatchingPinFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static com.iea.circuit.model.component.generator.GeneratorFactory.createGenerator;
import static com.iea.circuit.model.component.receiver.ReceiverFactory.createReceiver;
import static org.junit.Assert.assertEquals;


public class DefaultCircuitCreatorTest {

    private static final DefaultCircuitCreator DEFAULT_CIRCUIT_CREATOR = new DefaultCircuitCreator();
    private static final GeneratorConfiguration GENERATOR_CONFIGURATION = new GeneratorConfiguration(1, 5);
    private static final ReceiverConfiguration LED_CONFIGURATION = new ReceiverConfiguration(0.02, 1.3, 2.5, 100);
    private static final ReceiverConfiguration RESISTOR_CONFIG = new ReceiverConfiguration(10, 1.5, 2, 150);
    private static Receiver LED_01;
    private static Receiver LED_02;
    private static Receiver LED_03;
    private static Receiver LED_04;
    private static Receiver RES_01;

    private static Generator GENERATOR;

    @Before
    public void init() {
        LED_01 = createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        LED_02 = createReceiver("led-2", ReceiverType.DIPOLE, LED_CONFIGURATION);
        LED_03 = createReceiver("led-3", ReceiverType.DIPOLE, LED_CONFIGURATION);
        LED_04 = createReceiver("led-4", ReceiverType.DIPOLE, LED_CONFIGURATION);
        RES_01 = createReceiver("res-1", ReceiverType.RESISTOR, RESISTOR_CONFIG);
        GENERATOR = createGenerator("bat-0", GENERATOR_CONFIGURATION);
    }

    @Test(expected = InvalidNumberOfGeneratorException.class)
    public void should_ensure_that_empty_strings_throw_invalid_number_of_generators() throws Exception {
        String generator = "";
        String receivers = "";
        String connections = "";

        DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);
    }

    @Test
    public void should_ensure_that_for_empty_connections_circuit_is_created_with_no_receivers() throws Exception {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "";

        Circuit actualCircuit = DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);

        Circuit expectedCircuit = new DefaultCircuit(SimpleCircuit.Builder.newBuilder().setGenerator(createGenerator("bat-0", GENERATOR_CONFIGURATION)).build());
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void default_creator_should_ensure_generator_only_circuit_can_be_created() throws Exception {
        String generator = "bat-0";
        String receivers = "";
        String connections = "";

        Circuit actualCircuit = DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);

        Circuit expectedCircuit = new DefaultCircuit(SimpleCircuit.Builder.newBuilder().setGenerator(createGenerator("bat-0", GENERATOR_CONFIGURATION)).addAllReceivers(Collections.emptyList()).build());
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test(expected = InvalidNumberOfGeneratorException.class)
    public void default_creator_should_ensure_that_wired_circuit_with_no_generator_throws_error() throws Exception {
        String generator = "";
        String receivers = "led-1,led-2";
        String connections = "led-1,+,led-2,+;led-1,-,led-2,-";

        DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);
    }

    @Test
    public void should_ensure_that_circuit_connects_three_components_series_correctly() throws Exception {
        String generator = "bat-0";
        String receivers = "led-1,led-2";
        String connections = "bat-0,+,led-1,+;led-1,-,led-2,+;led-2,-,bat-0,-";

        Circuit actualCircuit = DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);

        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver led2 = createReceiver("led-2", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Generator bat0 = createGenerator("bat-0", GENERATOR_CONFIGURATION);
        Circuit expectedCircuit = new DefaultCircuit(SimpleCircuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .addReceiver(led2)
                .connectComponents(new Connection(bat0, bat0.getFirstPin(), led1, led1.getFirstPin())).connectComponents(new Connection(led1, led1.getSecondPin(), led2, led2.getFirstPin()))
                .connectComponents(new Connection(led2, led2.getSecondPin(), bat0, bat0.getSecondPin())).build());
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void default_creator_should_ensure_that_circuit_connects_parallel() throws Exception {
        String generator = "bat-0";
        String receivers = "led-1,led-2";
        String connections = "bat-0,+,led-1,+;bat-0,+,led-2,+;bat-0,-,led-1,-;bat-0,-,led-2,-";

        Circuit actualCircuit = DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);

        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Receiver led2 = createReceiver("led-2", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Generator bat0 = createGenerator("bat-0", GENERATOR_CONFIGURATION);
        Circuit expectedCircuit = new DefaultCircuit(SimpleCircuit.Builder.newBuilder().setGenerator(bat0).addReceiver(led1).addReceiver(led2)
                .connectComponents(new Connection(bat0, bat0.getFirstPin(), led1, led1.getFirstPin())).connectComponents(new Connection(bat0, bat0.getFirstPin(), led2, led2.getFirstPin()))
                .connectComponents(new Connection(bat0, bat0.getSecondPin(), led1, led1.getSecondPin())).connectComponents(new Connection(bat0, bat0.getSecondPin(), led2, led2.getSecondPin())).build());
        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test
    public void should_ensure_redundant_wiring_does_not_affect_result() throws Exception {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,+,led-1,+;led-1,+,bat-0,+;bat-0,-,led-1,-";

        Circuit actualCircuit = DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);

        Receiver led1 = createReceiver("led-1", ReceiverType.DIPOLE, LED_CONFIGURATION);
        Generator bat0 = createGenerator("bat-0", GENERATOR_CONFIGURATION);

        Circuit expectedCircuit = new DefaultCircuit(SimpleCircuit.Builder.newBuilder()
                .setGenerator(bat0)
                .addReceiver(led1)
                .connectComponents(new Connection(bat0, bat0.getFirstPin(), led1, led1.getFirstPin()))
                .connectComponents(new Connection(bat0, bat0.getSecondPin(), led1, led1.getSecondPin()))
                .build());

        assertEquals(expectedCircuit, actualCircuit);
    }

    @Test(expected = NoMatchingPinFoundException.class)
    public void default_creator_should_throw_pin_decode_error() throws Exception {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,2,led-1,5;led-1,1,bat-0,4";

        DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);
    }

    @Test(expected = InvalidNumberOfGeneratorException.class)
    public void default_creator_should_throw_too_many_generators_error() throws Exception {
        String generator = "bat-0,bat-1";
        String receivers = "led-1";
        String connections = "";

        DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);
    }

    @Test(expected = MatchingComponentNotFoundException.class)
    public void default_creator_should_throw_matching_component_not_found_exception() throws Exception {
        String generator = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,+,res-1,~1";

        DEFAULT_CIRCUIT_CREATOR.create(generator, receivers, connections);
    }

    @Test
    public void should_ensure_circuit_is_created_with_receivers_in_closed_circuit_with_generator() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,res-1";
        String connections = "bat-0,+,led-1,+;led-1,-,res-1,~1;res-1,~2,led-2,+;led-2,-,bat-0,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(RES_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), RES_01, RES_01.getFirstPin()))
                .connectComponents(new Connection(RES_01, RES_01.getSecondPin(), LED_02, LED_02.getFirstPin()))
                .connectComponents(new Connection(LED_02, LED_02.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_with_receivers_in_closed_circuit_with_generator_with_unconnected_receiver() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,res-1";
        String connections = "bat-0,+,led-1,+;led-1,-,res-1,~1;res-1,~2,bat-0,-;res-1,~2,led-2,+";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(RES_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), RES_01, RES_01.getFirstPin()))
                .connectComponents(new Connection(RES_01, RES_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_with_receivers_in_closed_circuit_with_generator_with_seperate_closed_circuit() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,res-1,led-3,led-2";
        String connections = "bat-0,+,led-1,+;led-1,-,res-1,~1;res-1,~2,bat-0,-;led-3,-,led-2,+,led-3,+,led-2,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(RES_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), RES_01, RES_01.getFirstPin()))
                .connectComponents(new Connection(RES_01, RES_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }


    @Test
    public void should_ensure_circuit_is_created_with_no_receivers_if_open_circuit() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,res-1";
        String connections = "bat-0,+,led-1,+;led-1,-,res-1,~1;res-1,~2,led-2,+";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .build();

        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_normal_parallel_circuit() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,res-1";
        String connections = "bat-0,+,led-1,+;led-1,-,bat-0,-;bat-0,+,res-1,~1;res-1,~2,bat-0,-;bat-0,+,led-2,+;led-2,-,bat-0,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(RES_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .connectComponents(new Connection(RES_01, RES_01.getFirstPin(), GENERATOR, GENERATOR.getFirstPin()))
                .connectComponents(new Connection(RES_01, RES_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .connectComponents(new Connection(LED_02, LED_02.getFirstPin(), GENERATOR, GENERATOR.getFirstPin()))
                .connectComponents(new Connection(LED_02, LED_02.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_series_circuit_with_one_parallel_receiver() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,res-1";
        String connections = "bat-0,+,led-1,+;led-1,-,res-1,~1;led-1,-,led-2,+;res-1,~2,bat-0,-;bat-0,-,led-2,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(RES_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), RES_01, RES_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), LED_02, LED_02.getFirstPin()))
                .connectComponents(new Connection(RES_01, RES_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .connectComponents(new Connection(LED_02, LED_02.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    //TODO CHECK THIS TEST. @Assaker's algorithm in CircuitUtils is hanging.
//    @Test
//    public void should_ensure_circuit_is_created_with_no_receivers_for_series_circuit_with_reversed_pins_in_series_circu0it() throws Exception {
//        String generators = "bat-0";
//        String receivers = "led-1,led-2,res-1";
//        String connections = "bat-0,+,led-1,-;led-1,+,res-1,~1;led-1,-,led-2,+;res-1,~2,bat-0,-;bat-0,-,led-2,-";
//
//        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
//                .setGenerator(GENERATOR)
//                .addReceiver(LED_01)
//                .addReceiver(LED_02)
//                .addReceiver(RES_01)
//                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getSecondPin()))
//                .connectComponents(new Connection(LED_01, LED_01.getFirstPin(), RES_01, RES_01.getFirstPin()))
//                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), LED_02, LED_02.getFirstPin()))
//                .connectComponents(new Connection(RES_01, RES_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
//                .connectComponents(new Connection(LED_02, LED_02.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
//                .build();
//
//        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);
//
//        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
//    }

    @Test
    public void should_ensure_circuit_is_created_with_no_receivers_for_series_circuit_with_reversed_pins_in_series_circuit() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,led-3";
        String connections = "bat-0,+,led-1,-;led-1,+,led-3,+;led-1,-,led-2,+;led-3,-,bat-0,-;bat-0,-,led-2,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_with_no_receivers__for_receiver_with_switched_pins() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1";
        String connections = "bat-0,+,led-1,-;led-1,+,bat-0,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_with_for_circuit_with_series_receiver_and_reversed_pins_in_parallel_receiver() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,res-1";
        String connections = "bat-0,+,led-1,+;led-1,-,res-1,~1;led-1,-,led-2,-;res-1,~2,bat-0,-;led-2,+,bat-0,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(RES_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), RES_01, RES_01.getFirstPin()))
                .connectComponents(new Connection(RES_01, RES_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_circuit_with_multiple_receivers_with_switched_pins() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,led-3";
        String connections = "bat-0,+,led-1,+;led-1,-,led-3,-;led-3,+,led-2,+;led-2,-,bat-0,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_circuit_with_two_receivers_connected_to_each_other() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2";
        String connections = "bat-0,+,led-1,+;led-1,-,led-2,-;led-2,-,led-1,+";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_delta_connection_circuit() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,led-3";
        String connections = "bat-0,+,led-1,+;led-1,-,bat-0,-;led-2,+,bat-0,+;led-2,-,led-3,+;led-3,-,bat-0,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(LED_03)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .connectComponents(new Connection(LED_02, LED_02.getFirstPin(), GENERATOR, GENERATOR.getFirstPin()))
                .connectComponents(new Connection(LED_02, LED_02.getSecondPin(), LED_03, LED_03.getFirstPin()))
                .connectComponents(new Connection(LED_03, LED_03.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_simple_circuit_with_generator_not_connected() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,led-3";
        String connections = "led-1,+,led-2,-;led-1,-,led-3,+;led-3,-,led-2,+";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_complex_circuit_without_generator() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,led-3,led-4";
        String connections = "bat-0,+,led-1,+;led-1,-,bat-0,-;led-2,+,bat-0,-;bat-0,-,led-3,-;led-3,+,led-4,-;led-4,+,led-2,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_complex_circuit_without_a_generator() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,led-3,led-4";
        String connections = "bat-0,+,led-1,+;led-1,-,bat-0,-;led-2,+,bat-0,-;bat-0,-,led-4,-;led-4,+,led-3,-;led-3,+,led-2,-;led-1,-,led-2,+";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .addReceiver(LED_02)
                .addReceiver(LED_03)
                .addReceiver(LED_04)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .connectComponents(new Connection(LED_02, LED_02.getFirstPin(), GENERATOR, GENERATOR.getSecondPin()))
                .connectComponents(new Connection(GENERATOR, GENERATOR.getSecondPin(), LED_04, LED_04.getSecondPin()))
                .connectComponents(new Connection(LED_04, LED_04.getFirstPin(), LED_03, LED_03.getSecondPin()))
                .connectComponents(new Connection(LED_03, LED_03.getFirstPin(), LED_02, LED_02.getSecondPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), LED_02, LED_02.getFirstPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }

    @Test
    public void should_ensure_circuit_is_created_for_circuit_without_generator() throws Exception {
        String generators = "bat-0";
        String receivers = "led-1,led-2,led-3,led-4";
        String connections = "bat-0,+,led-1,+;led-1,-,bat-0,-;led-2,+,bat-0,-;bat-0,-,led-4,-;led-4,+,led-3,-;led-3,+,led-2,-;led-3,+,led-2,-";

        Circuit circuit = DEFAULT_CIRCUIT_CREATOR.create(generators, receivers, connections);

        SimpleCircuit simpleCircuit = SimpleCircuit.Builder.newBuilder()
                .setGenerator(GENERATOR)
                .addReceiver(LED_01)
                .connectComponents(new Connection(GENERATOR, GENERATOR.getFirstPin(), LED_01, LED_01.getFirstPin()))
                .connectComponents(new Connection(LED_01, LED_01.getSecondPin(), GENERATOR, GENERATOR.getSecondPin()))
                .build();
        assertEquals(new DefaultCircuit(simpleCircuit), circuit);
    }
}
