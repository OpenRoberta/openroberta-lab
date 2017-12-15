package de.fhg.iais.roberta.components;

import de.fhg.iais.roberta.util.dbc.Assert;

public abstract class HardwareComponent {
    private String name;

    public HardwareComponent(String name) {
        Assert.isTrue(name != null && !name.equals(""));
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
        HardwareComponent other = (HardwareComponent) obj;
        if ( this.name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !this.name.equals(other.name) ) {
            return false;
        }
        return true;
    }
}
