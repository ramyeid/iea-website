/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.receiver;

public enum ReceiverStatus {

    OFF(0), LOW(1), OPTIMAL(2), DAMAGED(3);

    private int value;

    private ReceiverStatus(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }
}
