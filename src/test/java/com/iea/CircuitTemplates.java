package com.iea;

import com.iea.circuit.Circuit;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.generator.GeneratorConfiguration;
import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverConfiguration;
import com.iea.circuit.receiver.config.ReceiverType;
import com.iea.utils.Tuple;

import static com.iea.circuit.generator.GeneratorFactory.createGenerator;

public class CircuitTemplates {

    private final static ReceiverConfiguration RED_LED_CONFIGURATION = new ReceiverConfiguration(0.02, 1.3, 2.5, 100);
    private final static ReceiverConfiguration GREEN_LED_CONFIGURATION = new ReceiverConfiguration(0.03, 1.3, 3, 100);
    private final static ReceiverConfiguration MOTOR_CONFIGURATION = new ReceiverConfiguration(1, 2, 6, 20);
    private final static ReceiverConfiguration RESISTOR_CONFIGURATION = new ReceiverConfiguration(10, 1.5, 2, 150);
    private final static ReceiverConfiguration BUZZER_CONFIGURATION = new ReceiverConfiguration(0.03, 2, 2.5, 120);
    private final static GeneratorConfiguration GENERATOR_CONFIGURATION = new GeneratorConfiguration(1, 5);


    public static Circuit createSeriesCircuitWithOneRedLEDAndOneGreenLED() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led02 = ReceiverFactory.createReceiver("led02", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithOneRedLEDAndOneResistor() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver res01 = ReceiverFactory.createReceiver("res01", ReceiverType.DIPOLE, RESISTOR_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(res01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(res01.getFirstPin(), res01))
                .connectComponents(new Tuple<>(res01.getSecondPin(), res01), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithTwoRedLEDsAndOneGreenLED() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led02 = ReceiverFactory.createReceiver("led02", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led03 = ReceiverFactory.createReceiver("led03", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(led03)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(led03.getFirstPin(), led03))
                .connectComponents(new Tuple<>(led03.getSecondPin(), led03), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithOneGreenLED() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithOneRedLEDAndTwoResistors() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver res01 = ReceiverFactory.createReceiver("res01", ReceiverType.DIPOLE, RESISTOR_CONFIGURATION);
        Receiver res02 = ReceiverFactory.createReceiver("res02", ReceiverType.DIPOLE, RESISTOR_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(res01)
                .addReceiver(res02)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(res02.getFirstPin(), res02))
                .connectComponents(new Tuple<>(res02.getSecondPin(), res02), new Tuple<>(res01.getFirstPin(), res01))
                .connectComponents(new Tuple<>(res01.getSecondPin(), res01), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }


    public static Circuit createSeriesCircuitWithOneRedLEDAndOneBuzzer() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver buz01 = ReceiverFactory.createReceiver("buz01", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(buz01)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(buz01.getFirstPin(), buz01))
                .connectComponents(new Tuple<>(buz01.getSecondPin(), buz01), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }

    public static Circuit createSeriesCircuitWithThreeBuzzers() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver buz01 = ReceiverFactory.createReceiver("buz01", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);
        Receiver buz02 = ReceiverFactory.createReceiver("buz02", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);
        Receiver buz03 = ReceiverFactory.createReceiver("buz03", ReceiverType.DIPOLE, BUZZER_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(buz01)
                .addReceiver(buz02)
                .addReceiver(buz03)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(buz01.getFirstPin(), buz01))
                .connectComponents(new Tuple<>(buz01.getSecondPin(), buz01), new Tuple<>(buz03.getFirstPin(), buz03))
                .connectComponents(new Tuple<>(buz03.getSecondPin(), buz03), new Tuple<>(buz02.getFirstPin(), buz02))
                .connectComponents(new Tuple<>(buz02.getSecondPin(), buz02), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }


    public static Circuit createSeriesCircuitWithTwoRedLEDsAndOneGreenLEDInParallel() {
        Generator generator = createGenerator("gen01", GENERATOR_CONFIGURATION);
        Receiver led01 = ReceiverFactory.createReceiver("led01", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led02 = ReceiverFactory.createReceiver("led02", ReceiverType.DIPOLE, RED_LED_CONFIGURATION);
        Receiver led03 = ReceiverFactory.createReceiver("led03", ReceiverType.DIPOLE, GREEN_LED_CONFIGURATION);

        return Circuit.Builder.newBuilder().setGenerator(generator)
                .addReceiver(led01)
                .addReceiver(led02)
                .addReceiver(led03)
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led01.getFirstPin(), led01))
                .connectComponents(new Tuple<>(led01.getSecondPin(), led01), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led02.getFirstPin(), led02))
                .connectComponents(new Tuple<>(led02.getSecondPin(), led02), new Tuple<>(generator.getSecondPin(), generator))
                .connectComponents(new Tuple<>(generator.getFirstPin(), generator), new Tuple<>(led03.getFirstPin(), led03))
                .connectComponents(new Tuple<>(led03.getSecondPin(), led03), new Tuple<>(generator.getSecondPin(), generator))
                .build();
    }
}