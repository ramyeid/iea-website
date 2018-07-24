/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.pin;

public class PinFactory {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

    public static Pin createPositivePin() { return new Pin(Pin.Type.POSITIVE);}

    public static Pin createNegativePin() { return new Pin(Pin.Type.NEGATIVE);}

    public static Pin createNeutralPin() { return new Pin(Pin.Type.NEUTRAL);}

}
