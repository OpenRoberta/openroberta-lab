package de.fhg.iais.roberta.util;

public class KeyVal {
    private final String key;
    private final String value;

    public KeyVal(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "KeyVal [key=" + this.key + ", value=" + this.value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
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
        KeyVal other = (KeyVal) obj;
        if ( this.key == null ) {
            if ( other.key != null ) {
                return false;
            }
        } else if ( !this.key.equals(other.key) ) {
            return false;
        }
        if ( this.value == null ) {
            if ( other.value != null ) {
                return false;
            }
        } else if ( !this.value.equals(other.value) ) {
            return false;
        }
        return true;
    }
}
