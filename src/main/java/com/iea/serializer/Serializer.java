package com.iea.serializer;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.DipoleReceiver;
import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.utils.Tuple;

import static com.iea.serializer.Configurations.getGeneratorConfiguration;
import static com.iea.serializer.Configurations.getReceiverConfiguration;

public class Serializer {

    private static final String ID_TOKEN = "-";

    /**
     * Function used to build a circuit out of 3 strings
     * @param generator: string containing a list of generator IDs
     * @param receivers: string containing a list of receiver IDs
     * @param connections: string containing lists of quadruples representing the connections
     *                     quadruple format is as follows:
     *                     sourceComponentId, sourcePin, destinationComponentId, destinationPin
     * @return returns a circuit with the generator, receivers, and connections given
     */
    public static Circuit serialize(String generator, String receivers, String connections) {
        Circuit.Builder circuitBuilder = Circuit.Builder.newBuilder();
        ReceiverFactory receiverFactory = new ReceiverFactory();

        //variables used for wiring components
        Component sourceComponent;
        Pin sourcePin;
        Component destinationComponent;
        Pin destinationPin;

        if (!generator.isEmpty()) { //ignore empty strings
            Circuit.Builder.setGenerator(new Generator(generator, getGeneratorConfiguration(generator.split(ID_TOKEN)[0])));
        }

        if (!receivers.isEmpty()) { //ignore empty strings
            for (String receiverId : receivers.split(",")) {
                Circuit.Builder.addReceiver(receiverFactory
                        .createDipoleReceiver(receiverId, getReceiverConfiguration(receiverId.split(ID_TOKEN)[0])));
            }
        }

        if (!connections.isEmpty()) { //ignore empty strings
            String[] connectionsList = connections.split(",");
            for (int i = 0; i < connectionsList.length; i += 4) {
                // pattern of each 4 entries in the list are as follows:
                // sourceComponentId, sourcePin, destinationComponentId, destinationPin
                sourceComponent = circuitBuilder.getComponentById(connectionsList[i]);
                sourcePin = decodePin(connectionsList[i+1], sourceComponent);
                destinationComponent = circuitBuilder.getComponentById(connectionsList[i + 2]);
                destinationPin = decodePin(connectionsList[i+3], destinationComponent);
                Circuit.Builder.connectComponents(new Tuple<>(sourcePin, sourceComponent),
                        new Tuple<>(destinationPin, destinationComponent));
            }
        }
        Circuit generatedCircuit = circuitBuilder.build();
        circuitBuilder.clearComponents();
        return generatedCircuit;
    }

    /**
     * Function used to decode which pin should be returned from a component based on a string representation
     * @param pinRepresentation: string containing a representation of the concerned pin
     * @param component: Component object whose pins are being checked
     * @return returns the concerned pin of the component
     */
    private static Pin decodePin(String pinRepresentation, Component component) {
        switch (pinRepresentation) {
            case "+":
                return ((DipoleReceiver)component).getPositivePin();
            case "-":
                return ((DipoleReceiver)component).getNegativePin();
            default:
                return null;
                //throw exception
        }
    }
}
