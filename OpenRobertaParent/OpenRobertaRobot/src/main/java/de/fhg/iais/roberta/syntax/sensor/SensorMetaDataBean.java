package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.IPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISlot;

public class SensorMetaDataBean {
    private IPort port;
    private IMode mode;
    private ISlot slot;
    private boolean isPortInMutation;

    public SensorMetaDataBean(IPort port, IMode mode, ISlot slot, boolean isPortInMutation) {
        this.port = port;
        this.mode = mode;
        this.slot = slot;
        this.setPortInMutation(isPortInMutation);
    }

    public IPort getPort() {
        return this.port;
    }

    public void setPort(IPort port) {
        this.port = port;
    }

    public IMode getMode() {
        return this.mode;
    }

    public void setMode(IMode mode) {
        this.mode = mode;
    }

    public ISlot getSlot() {
        return this.slot;
    }

    public void setSlot(ISlot slot) {
        this.slot = slot;
    }

    public boolean isPortInMutation() {
        return this.isPortInMutation;
    }

    public void setPortInMutation(boolean isPortInMutation) {
        this.isPortInMutation = isPortInMutation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.mode == null) ? 0 : this.mode.hashCode());
        result = prime * result + ((this.port == null) ? 0 : this.port.hashCode());
        result = prime * result + ((this.slot == null) ? 0 : this.slot.hashCode());
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
        SensorMetaDataBean other = (SensorMetaDataBean) obj;
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
