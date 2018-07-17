//package com.iea.orchestrator;
//
//import com.iea.PresenterNotifier;
//import com.iea.ScreenListener;
//import com.iea.circuit.Circuit;
//import com.iea.circuit.pin.Pin;
//import com.iea.circuit.Component;
//import com.iea.circuit.generator.Generator;
//import com.iea.circuit.receiver.Receiver;
//
//import java.util.List;
//
//
//public class Orchestrator implements ScreenListener {
//
//    private double amp = 0;
//    private double volt = 0;
//    private double used_volt = 0;
//
//    private final PresenterNotifier presenterNotifier;
//
//    public Orchestrator(PresenterNotifier presenterNotifier) {
//        this.presenterNotifier = presenterNotifier;
//    }
//
//    private boolean validate(List<Component> components) {
//        int generatorCount = 0;
//        amp = 0;
//        volt = 0;
//        used_volt = 0;
//        for (Component component : components) {
//            if (component instanceof Generator) {
//                amp += ((Generator) component).getMaxAmper();
//                volt += ((Generator) component).getVolt();
//                generatorCount++;
//            }
//        }
//        return generatorCount > 0;
//    }
//
//
//    public void onStart(Circuit circuit) throws Exception {
//
//
//    }
////        if (!validate(circuit))
////            throw new Exception();
////        for (int i = 0; i < components.size(); i++) {
////            Pin.connectComponents(Tuple < components., components.get(i) >, Tuple < components.get(i + 1) >);
////        }
////        for (Component component : components) {
////
////            used_volt += ((Receiver) component).getOptimalVolt();
////            if (used_volt > volt) {
////                throw new Exception();
////            }
////        }
////
////        presenterNotifier.onResults();
//
////    }
//
//    @Override
//    public void onStop() {
//
//    }
//}
