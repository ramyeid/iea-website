package com.iea.serializer;

import com.iea.circuit.Circuit;
import com.iea.circuit.Component;
import com.iea.circuit.pin.Pin;
import com.iea.circuit.receiver.ReceiverFactory;
import com.iea.circuit.receiver.config.Receiver;
import com.iea.circuit.receiver.config.ReceiverStatus;
import com.iea.utils.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.iea.circuit.generator.GeneratorFactory.createGenerator;
import static com.iea.serializer.Configurations.*;

public class Serializer {

    private static final Logger LOGGER = LogManager.getLogger(Serializer.class);
    private static final String ID_TOKEN = "-";

    /**
     * Function used to build a circuit out of 3 strings
     *
     * @param generator:   string containing the generator's ID, e.g. "bat0"
     * @param receivers:   string containing a list of receiver IDs, e.g. "res-1,led-2,buz-3"
     * @param connections: string containing lists of quadruples representing the connections
     *                     quadruple format is as follows:
     *                     sourceComponentId, sourcePin, destinationComponentId, destinationPin
     *                     e.g. "bat0,+,res-1,+" connects the positive pin of bat0 to the positive pin of res-1
     *                     <p>
     *                     Pin types: pin types for connections are mapped as follows:
     *                     +  -> positive
     *                     -  -> negative
     *                     ~1  -> neutral 1
     *                     ~2  -> neutral 2
     * @return returns a circuit with the generator, receivers, and connections given
     */
    public static Circuit deSerialize(String generator, String receivers, String connections) throws NoMatchingPinFoundException {
        LOGGER.info("------------ Deserializer Started ------------ ");
        LOGGER.info("Generator: " + generator);
        LOGGER.info("Receivers: " + receivers);
        LOGGER.info("Connections: " + connections);

        Circuit.Builder circuitBuilder = Circuit.Builder.newBuilder();

        if (!generator.isEmpty()) {
            circuitBuilder.setGenerator(createGenerator(generator, getGeneratorConfiguration(generator.split(ID_TOKEN)[0])));
        }

        if (!receivers.isEmpty()) {
            for (String receiverId : receivers.split(",")) {

                circuitBuilder.addReceiver(ReceiverFactory
                        .createReceiver(receiverId, getReceiverType(receiverId.split(ID_TOKEN)[0]), getReceiverConfiguration(receiverId.split(ID_TOKEN)[0])));
            }
        }

        if (!connections.isEmpty()) {
            String[] connectionsList = connections.split(",");
            for (int i = 0; i < connectionsList.length; i += 4) {
                Component sourceComponent = circuitBuilder.getComponentById(connectionsList[i]);
                Pin sourcePin = decodePin(connectionsList[i + 1], sourceComponent);
                Component destinationComponent = circuitBuilder.getComponentById(connectionsList[i + 2]);
                Pin destinationPin = decodePin(connectionsList[i + 3], destinationComponent);
                circuitBuilder.connectComponents(new Tuple<>(sourcePin, sourceComponent),
                        new Tuple<>(destinationPin, destinationComponent));
            }
        }
        LOGGER.info("------------ Deserializer Finished ------------ ");
        return circuitBuilder.build();
    }


    /**
     * Function used to transform a receiver to receiverStatus map into a string
     * with the following format: receiver1Id,receiver1Status,receiver2Id,receiver2Status, ...
     *
     * @param receiverStatusMap Map containing receiver to receiverStatus mappings
     */
    public static String serialize(Map<Receiver, ReceiverStatus> receiverStatusMap) {
        LOGGER.info("------------ Serializer Started ------------ ");
        StringJoiner deserializedStringBuilder = new StringJoiner(",");
        receiverStatusMap.forEach((key, value) -> deserializedStringBuilder.add(key.getId() + ":" + String.valueOf(value.getIntValue())));
        String deserialzedStringBuilderValue = deserializedStringBuilder.toString();
        LOGGER.info("Serialized string: " + deserialzedStringBuilderValue);
        LOGGER.info("------------ Serializer Finished ------------ ");
        return deserialzedStringBuilderValue;
    }

    /**
     * Function used to decode which pin should be returned from a component based on a string representation
     *
     * @param pinRepresentation: string containing a representation of the concerned pin
     * @param component:         Component object whose pins are being checked
     * @return returns the concerned pin of the component
     */

    private static Pin decodePin(String pinRepresentation, Component component) throws NoMatchingPinFoundException {
        if (pinRepresentation.charAt(0) == '~') {
            if (pinRepresentation.charAt(1) == '1') {
                return component.getFirstPin();
            } else {
                return component.getSecondPin();
            }
        }
        List<Pin> matchingPins = component.getPins().stream().filter(t -> t.getType().toString().equals(pinRepresentation)).collect(Collectors.toList());

        if (matchingPins.size() > 1 || matchingPins.isEmpty()) {
            LOGGER.error("No matching pin found for " + pinRepresentation);
            throw new NoMatchingPinFoundException(pinRepresentation);
        }

        return matchingPins.get(0);
    }
}
