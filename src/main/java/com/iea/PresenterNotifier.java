package com.iea;

import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.ReceiverStatus;

import java.util.Map;


public interface PresenterNotifier {

    void onResults(Map<Receiver, ReceiverStatus> receiverWithStatus);

    void onError();
}
