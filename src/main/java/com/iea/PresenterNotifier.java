package com.iea;

import com.iea.circuit.receiver.Receiver;
import com.iea.circuit.receiver.status.ReceiverStatus;
import com.iea.utils.Tuple;

import java.util.List;

public interface PresenterNotifier {
    void onResults(List<Tuple<Receiver, ReceiverStatus>> receiverWithStatus);
}
