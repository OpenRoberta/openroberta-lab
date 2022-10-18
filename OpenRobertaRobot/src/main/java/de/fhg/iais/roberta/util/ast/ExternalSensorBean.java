package de.fhg.iais.roberta.util.ast;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class ExternalSensorBean {
    private final String port;
    private final String mode;
    private final String slot;
    private final Mutation mutation;
    private final Hide hide;

    public ExternalSensorBean(String port, String mode, String slot, Mutation mutation) {
        this.port = port;
        this.mode = mode;
        this.slot = slot;
        this.mutation = mutation;
        this.hide = null;
    }

    public ExternalSensorBean(String port, String mode, String slot, Mutation mutation, List<Hide> hide) {
        this.port = port;
        this.mode = mode;
        this.slot = slot;
        this.mutation = mutation;
        if ( hide.size() == 1 ) {
            this.hide = hide.get(0);
        } else if ( hide.size() > 1 ) {
            throw new DbcException("more than 1 hide element");
        } else {
            this.hide = null;
        }
    }

    public String getPort() {
        return this.port;
    }

    public String getMode() {
        return this.mode;
    }

    public String getSlot() {
        return this.slot;
    }

    public Mutation getMutation() {
        return this.mutation;
    }

    public Hide getHide() {
        return this.hide;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.mode == null) ? 0 : this.mode.hashCode());
        result = (prime * result) + ((this.port == null) ? 0 : this.port.hashCode());
        result = (prime * result) + ((this.slot == null) ? 0 : this.slot.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        ExternalSensorBean other = (ExternalSensorBean) obj;
        if ( this.mode == null ) {
            if ( other.mode != null ) {
                return false;
            }
        } else if ( !this.mode.equals(other.mode) ) {
            return false;
        }
        if ( this.port == null ) {
            if ( other.port != null ) {
                return false;
            }
        } else if ( !this.port.equals(other.port) ) {
            return false;
        }
        if ( this.slot == null ) {
            if ( other.slot != null ) {
                return false;
            }
        } else if ( !this.slot.equals(other.slot) ) {
            return false;
        }
        return true;
    }
}