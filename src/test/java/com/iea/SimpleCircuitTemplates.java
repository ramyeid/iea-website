package com.iea;

import com.iea.circuit.model.Connection;
import com.iea.circuit.model.SimpleCircuit;
import com.iea.circuit.model.component.generator.Generator;
import com.iea.circuit.model.component.generator.GeneratorConfiguration;
import com.iea.circuit.model.component.receiver.ReceiverFactory;
import com.iea.circuit.model.component.receiver.config.Receiver;
import com.iea.circuit.model.component.receiver.config.ReceiverConfiguration;
import com.iea.circuit.model.component.receiver.config.ReceiverType;

import static com.iea.circuit.model.component.generator.GeneratorFactory.createGenerator;

public class SimpleCircuitTemplates {

    private final static ReceiverConfiguration RED_LED_CONFIGURATION = new ReceiverConfiguration(0.02, 1.3, 2.5, 100);
    private final static ReceiverConfiguration GREEN_LED_CONFIGURATION = new ReceiverConfiguration(0.03, 1.3, 3, 100);
    private final static ReceiverConfiguration MOTOR_CONFIGURATION = new ReceiverConfiguration(1, 2, 6, 20);
    private final static ReceiverConfiguration RESISTOR_CONFIGURATION = new ReceiverConfiguration(10, 1.5, 2, 150);
    private final static ReceiverConfiguration BUZZER_CONFIGURATION = new ReceiverConfiguration(0.03, 2, 2.5, 120);
    private final static GeneratorConfiguration GENERATOR_CONFIGURATION = new GeneratorConfiguration(1, 5);


    public static SimpleCircuit createSeriesCircuitWithOneRedLEDAndOneGreenLED() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led02 = ReceiverFactory.createReceiver("led02", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Connection(generator, generator.getFirstPin(), led01, led01.getFirstPin()))
                .connectComponents(new Connection(led01, led01.getSecondPin(), led02, led02.getFirstPin()))
                .connectComponents(new Connection(led02, led02.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }

    public static SimpleCircuit createSeriesCircuitWithOneRedLEDAndOneResistor() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver res01 = ReceiverFactory.createReceiver("res01", ReceiverType.DIPOLE, RESISTOR_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(res01)
                .connectComponents(new Connection(generator, generator.getFirstPin(), led01, led01.getFirstPin()))
                .connectComponents(new Connection(led01, led01.getSecondPin(), res01, res01.getFirstPin()))
                .connectComponents(new Connection(res01, res01.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }

    public static SimpleCircuit createSeriesCircuitWithTwoRedLEDsAndOneGreenLED() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led02 = ReceiverFactory.createReceiver("led02", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led03 = ReceiverFactory.createReceiver("led03", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(led03)
                .connectComponents(new Connection(generator, generator.getFirstPin(), led01, led01.getFirstPin()))
                .connectComponents(new Connection(led01, led01.getSecondPin(), led03, led03.getFirstPin()))
                .connectComponents(new Connection(led03, led03.getSecondPin(), led02, led02.getFirstPin()))
                .connectComponents(new Connection(led02, led02.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }

    public static SimpleCircuit createSeriesCircuitWithOneGreenLED() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .connectComponents(new Connection(generator, generator.getFirstPin(), led01, led01.getFirstPin()))
                .connectComponents(new Connection(led01, led01.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }

    public static SimpleCircuit createSeriesCircuitWithOneRedLEDAndTwoResistors() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver res01 = ReceiverFactory.createReceiver("res01", ReceiverType.DIPOLE, RESISTOR_CONFIGURATION);
        Receiver res02 = ReceiverFactory.createReceiver("res02", ReceiverType.DIPOLE, RESISTOR_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(res01)
                .addReceiver(res02)
                .connectComponents(new Connection(generator, generator.getFirstPin(), led01, led01.getFirstPin()))
                .connectComponents(new Connection(led01, led01.getSecondPin(), res02, res02.getFirstPin()))
                .connectComponents(new Connection(res02, res02.getSecondPin(), res01, res01.getFirstPin()))
                .connectComponents(new Connection(res01, res01.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }


    public static SimpleCircuit createSeriesCircuitWithOneRedLEDAndOneBuzzer() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver buz01 = ReceiverFactory.createReceiver("buz01", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(buz01)
                .connectComponents(new Connection(generator, generator.getFirstPin(), led01, led01.getFirstPin()))
                .connectComponents(new Connection(led01, led01.getSecondPin(), buz01, buz01.getFirstPin()))
                .connectComponents(new Connection(buz01, buz01.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }

    public static SimpleCircuit createSeriesCircuitWithThreeBuzzers() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver buz01 = ReceiverFactory.createReceiver("buz01", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);
        Receiver buz02 = ReceiverFactory.createReceiver("buz02", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);
        Receiver buz03 = ReceiverFactory.createReceiver("buz03", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(buz01)
                .addReceiver(buz02)
                .addReceiver(buz03)
                .connectComponents(new Connection(generator, generator.getFirstPin(), buz01, buz01.getFirstPin()))
                .connectComponents(new Connection(buz01, buz01.getSecondPin(), buz03, buz03.getFirstPin()))
                .connectComponents(new Connection(buz03, buz03.getSecondPin(), buz02, buz02.getFirstPin()))
                .connectComponents(new Connection(buz02, buz02.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }


    public static SimpleCircuit createSeriesCircuitWithTwoRedLEDsAndOneGreenLEDInParallel() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led02 = ReceiverFactory.createReceiver("led02", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led03 = ReceiverFactory.createReceiver("led03", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return SimpleCircuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(led03)
                .connectComponents(new Connection(generator, generator.getFirstPin(), led01, led01.getFirstPin()))
                .connectComponents(new Connection(led01, led01.getSecondPin(), generator, generator.getSecondPin()))
                .connectComponents(new Connection(generator, generator.getFirstPin(), led02, led02.getFirstPin()))
                .connectComponents(new Connection(led02, led02.getSecondPin(), generator, generator.getSecondPin()))
                .connectComponents(new Connection(generator, generator.getFirstPin(), led03, led03.getFirstPin()))
                .connectComponents(new Connection(led03, led03.getSecondPin(), generator, generator.getSecondPin()))
                .build();
    }
}