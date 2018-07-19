/**
 *  Copyright Murex S.A.S., 2003-2018. All Rights Reserved.
 *
 *  This software program is proprietary and confidential to Murex S.A.S and its affiliates ("Murex") and, without limiting the generality of the foregoing reservation of rights, shall not be accessed, used, reproduced or distributed without the
 *  express prior written consent of Murex and subject to the applicable Murex licensing terms. Any modification or removal of this copyright notice is expressly prohibited.
 */
package com.iea.circuit.receiver;

import java.util.Objects;


public class Configuration {

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Instance fields
    //~ ----------------------------------------------------------------------------------------------------------------

    private final double optimalAmper;
    private final double minVolt;
    private final double maxVolt;
    private final double resistance;

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Constructors
    //~ ----------------------------------------------------------------------------------------------------------------

    public Configuration(double optimalAmper, double minVolt, double maxVolt, double resistance) {
        this.optimalAmper = optimalAmper;
        this.minVolt = minVolt;
        this.maxVolt = maxVolt;
        this.resistance = resistance;
    }

    //~ ----------------------------------------------------------------------------------------------------------------
    //~ Methods
    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if ((o == null) || (getClass() != o.getClass()))
            return false;
        Configuration that = (Configuration) o;
        return (Double.compare(that.optimalAmper, optimalAmper) == 0) && (Double.compare(that.minVolt, minVolt) == 0) && (Double.compare(that.maxVolt, maxVolt) == 0) && (Double.compare(that.resistance, resistance) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optimalAmper, minVolt, maxVolt, resistance);
    }
}
