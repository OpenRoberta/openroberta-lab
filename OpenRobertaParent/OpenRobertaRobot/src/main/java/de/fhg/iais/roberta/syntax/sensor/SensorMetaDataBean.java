package de.fhg.iais.roberta.syntax.sensor;

public class SensorMetaDataBean {
    private String port;
    private String mode;
    private String slot;
    private boolean isPortInMutation;

    public SensorMetaDataBean(String port, String mode, String slot, boolean isPortInMutation) {
        this.port = port;
        this.mode = mode;
        this.slot = slot;
        this.setPortInMutation(isPortInMutation);
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSlot() {
        return this.slot;
    }

    public void setSlot(String slot) {
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
