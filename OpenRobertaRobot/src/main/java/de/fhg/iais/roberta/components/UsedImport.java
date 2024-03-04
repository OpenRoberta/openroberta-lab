package de.fhg.iais.roberta.components;

public class UsedImport {

    private final String type;

    /**
     * Used Import flag for Code generation
     *
     * @param type the Import name or type
     */
    public UsedImport(String type) {
        this.type = type;
    }

    /**
     * @return the Type
     */
    public String getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
        UsedImport other = (UsedImport) obj;

        if ( this.type == null ) {
            if ( other.type != null ) {
                return false;
            }
        } else if ( !this.type.equals(other.type) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UsedImport [" + this.getType() + "]";
    }

}
