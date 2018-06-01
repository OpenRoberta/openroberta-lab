package de.fhg.iais.roberta.mode.action;

import java.util.Objects;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class ActorPort implements IActorPort, Comparable<ActorPort> {
    //NO_PORT, A( "A", "MA" ), B( "B", "MB" ), C( "C", "MC" ), D( "D", "MD" ), LEFT( "left" ), RIGHT( "right" );

    private final String oraName;
    private final String codeName;

    public ActorPort(String oraName, String codeName) {
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
    public int compareTo(ActorPort other) {
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
        if ( !(obj instanceof ActorPort) ) {
            return false;
        }
        ActorPort other = (ActorPort) obj;
        return Objects.equals(this.oraName, other.oraName) && Objects.equals(this.codeName, other.codeName);
    }

    @Override
    public String toString() {
        return this.oraName;
    }

}