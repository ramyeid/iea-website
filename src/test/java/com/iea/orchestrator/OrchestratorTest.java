package com.iea.orchestrator;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.Configuration;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.utils.Tuple;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrchestratorTest {

    @Test
    public void should_Return_True_When_Circuit_Is_Closed() {
        Configuration ledconfig = new Configuration(5, 2, 4, 1);
        Configuration motorconfig = new Configuration(6, 3, 5, 2);
        Generator battery = new Generator("bat01", 10, 10);

        DipoleReceiver led = new DipoleReceiver("led01", ledconfig);
        DipoleReceiver motor = new DipoleReceiver("mot01", motorconfig);

        Circuit circuit = Circuit.Builder.newBuilder()
                .setGenerator(battery)
                .addReceiver(led)
                .addReceiver(motor)
                .connectComponents(new Tuple<Pin, Component>(battery.getPositivePin(), battery), new Tuple<Pin, Component>(led.getPositivePin(), led))
                .connectComponents(new Tuple<Pin, Component>(led.getNegativePin(), led), new Tuple<Pin, Component>(motor.getPositivePin(), motor))
                .connectComponents(new Tuple<Pin, Component>(motor.getNegativePin(), motor), new Tuple<Pin, Component>(battery.getNegativePin(), battery))
                .build();

        Orchestrator orchestrator=new Orchestrator();
        assertTrue(orchestrator.validate(circuit));

    }
}