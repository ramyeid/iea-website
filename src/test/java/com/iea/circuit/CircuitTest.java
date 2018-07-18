//package com.iea.circuit;
//
//import com.iea.circuit.generator.Battery;
//import com.iea.circuit.generator.Generator;
//import com.iea.circuit.receiver.LED;
//import com.iea.utils.Tuple;
//import org.junit.Test;
//
//public class CircuitTest {
//
//    @Test
//    public void should_build_circuit_correctly() {
//        LED led = new LED(20);
//        Battery battery = new Battery(10,30);
//
//        Circuit.Builder circuit = Circuit.Builder.newBuilder()
//                                    .setGenerator(battery)
//                                    .addReceiver(led)
//                                    .connectComponents(new Tuple<>(battery.getPositivePin(), battery), new Tuple<>(led.getPositivePin(), led))
//                                    .connectComponents(new Tuple<>(battery.getNegativePin(), battery), new Tuple<>(led.getNegativePin(), led))
//                                    .build();
//
//    }
//}