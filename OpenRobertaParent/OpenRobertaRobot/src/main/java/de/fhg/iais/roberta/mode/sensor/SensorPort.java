package de.fhg.iais.roberta.mode.sensor;

import java.util.Objects;

import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class SensorPort implements ISensorPort, Comparable<SensorPort> {

    private final String oraName;
    private final String codeName;

    public SensorPort(String oraName, String codeName) {
        this.oraName = oraName;
        this.codeName = codeName;
    }

    @Override
    public String[] getValues() {
        throw new DbcException("unsupported operation");
    }

    @Override
    public String getOraName() {
        return this.oraName;
    }

    @Override
    public String getCodeName() {
        return this.codeName;
    }

    @Override
    public int compareTo(SensorPort other) {
        int oraComp = this.oraName.compareTo(other.oraName);
        if ( oraComp == 0 ) {
            return this.codeName.compareTo(other.codeName);
        } else {
            return oraComp;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.oraName, this.codeName);
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == this ) {
            return true;
        }
        if ( !(obj instanceof SensorPort) ) {
            return false;
        }
        SensorPort other = (SensorPort) obj;
        return Objects.equals(this.oraName, other.oraName) && Objects.equals(this.codeName, other.codeName);
    }

    @Override
    public String toString() {
        return this.oraName;
    }

}
