package com.iea.serializer;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.generator.Generator;
import com.iea.circuit.pin.Pin;

import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.serializer.exception.PinDecodeError;
import com.iea.utils.Tuple;

import java.util.List;
import java.util.stream.Collectors;

import static com.iea.serializer.Configurations.getGeneratorConfiguration;
import static com.iea.serializer.Configurations.getReceiverConfiguration;
import static com.iea.serializer.Configurations.getReceiverType;

public class Serializer {

    private static final String ID_TOKEN = "-";

    /**
     * Function used to build a circuit out of 3 strings
     * @param generator: string containing the generator's ID, e.g. "bat0"
     * @param receivers: string containing a list of receiver IDs, e.g. "res-1,led-2,buz-3"
     * @param connections: string containing lists of quadruples representing the connections
     *                     quadruple format is as follows:
     *                     sourceComponentId, sourcePin, destinationComponentId, destinationPin
     *                e.g. "bat0,+,res-1,+" connects the positive pin of bat0 to the positive pin of res-1
     *
     * Pin types: pin types for connections are mapped as follows:
     *    +  -> positive
     *    -  -> negative
     *   ~1  -> neutral 1
     *   ~2  -> neutral 2
     *
     * @return returns a circuit with the generator, receivers, and connections given
     */
    public static Circuit serialize(String generator, String receivers, String connections) {
        Circuit.Builder circuitBuilder = Circuit.Builder.newBuilder();

        //variables used for wiring components
        Component sourceComponent;
        Pin sourcePin;
        Component destinationComponent;
        Pin destinationPin;

        if (!generator.isEmpty()) { //ignore empty strings
            circuitBuilder.setGenerator(new Generator(generator, getGeneratorConfiguration(generator.split(ID_TOKEN)[0])));
        }

        if (!receivers.isEmpty()) { //ignore empty strings
            for (String receiverId : receivers.split(",")) {

                circuitBuilder.addReceiver(ReceiverFactory
                        .createReceiver(getReceiverType(receiverId.split(ID_TOKEN)[0]), receiverId ,getReceiverConfiguration(receiverId.split(ID_TOKEN)[0])));
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
                circuitBuilder.connectComponents(new Tuple<>(sourcePin, sourceComponent),
                        new Tuple<>(destinationPin, destinationComponent));
            }
        }
        return circuitBuilder.build();
    }

    /**
     * Function used to decode which pin should be returned from a component based on a string representation
     * @param pinRepresentation: string containing a representation of the concerned pin
     * @param component: Component object whose pins are being checked
     * @return returns the concerned pin of the component
     */

    private static Pin decodePin(String pinRepresentation, Component component) throws PinDecodeError {
        if (pinRepresentation.charAt(0) == '~') {
            if (pinRepresentation.charAt(1) == '1') {
                return component.getFirstPin();
            } else {
                return component.getSecondPin();
            }
        }
        List<Pin> matchingPins = component.getPins().stream().filter(t -> t.getType().toString().equals(pinRepresentation)).collect(Collectors.toList());

        if (matchingPins.size() > 1 || matchingPins.isEmpty()) {
            throw new PinDecodeError();
        }

        return matchingPins.get(0);
    }
}
