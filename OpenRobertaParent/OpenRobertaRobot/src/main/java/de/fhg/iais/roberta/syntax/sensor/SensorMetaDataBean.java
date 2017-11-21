package de.fhg.iais.roberta.syntax.sensor;

import java.io.Serializable;

public class SensorMetaDataBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private String port;
    private String mode;

    public SensorMetaDataBean(String port, String mode) {
        this.port = port;
        this.mode = mode;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.mode == null) ? 0 : this.mode.hashCode());
        result = prime * result + ((this.port == null) ? 0 : this.port.hashCode());
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
        return true;
    }

}
