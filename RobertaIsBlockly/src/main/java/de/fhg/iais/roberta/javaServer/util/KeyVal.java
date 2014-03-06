package de.fhg.iais.roberta.javaServer.util;

public class KeyVal
{
    private final String key;
    private final String value;

    public KeyVal(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyVal [key=" + key + ", value=" + value + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        KeyVal other = (KeyVal) obj;
        if ( key == null ) {
            if ( other.key != null )
                return false;
        } else if ( !key.equals(other.key) )
            return false;
        if ( value == null ) {
            if ( other.value != null )
                return false;
        } else if ( !value.equals(other.value) )
            return false;
        return true;
    }
}
