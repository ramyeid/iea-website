/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.receiver;

import java.util.Objects;

import com.iea.circuit.Component;


public abstract class Receiver extends Component {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields
    //~ ----------------------------------------------------------------------------------------------------------------

    private final Configuration configuration;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors
    //~ ----------------------------------------------------------------------------------------------------------------

    Receiver(String id, Configuration configuration) {
        super(id);
        this.configuration = configuration;
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

    public abstract ReceiverStatus retrieveStatus(double amper, double volt);

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        if (!super.equals(o))
            return false;
        Receiver receiver = (Receiver) o;
        return Objects.equals(configuration, receiver.configuration);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), configuration);
    }
}
