package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * the top class of all sensor blocks. There are two ways for a client to find out which kind of aktion an {@link #Aktion}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Sensor extends Phrase {
    /**
     * returns the sensor if and only if it is an object of the class given as parameter. Otherwise an exception is thrown. The type of the returned
     * sensor is
     * the type of the requested class.<br>
     * <i>Is this complexity really needed? Isn't brute-force casting at the client side sufficient?</i>
     * 
     * @param clazz the class the object must be an instance of to return without exception
     * @return the object casted to the type requested
     */
    @SuppressWarnings("unchecked")
    public final <T> T getAs(Class<T> clazz) {
        Assert.isTrue(clazz.equals(getKind().getAktionClass()));
        return (T) this;
    }

    /**
     * @return the kind of the sensor. See enum {@link Kind} for all kinds possible<br>
     */
    abstract public Kind getKind();

    /**
     * append a nice, dense and human-readable representation of an sensor for <b>debugging and testing purposes</b>
     * 
     * @param sb the string builder, to which has to be appended
     * @param indentation number defining the level of indentation
     */
    abstract public void toStringBuilder(StringBuilder sb, int indentation);

    /**
     * append a newline, then append spaces up to an indentation level, then append an (optional) text<br>
     * helper for constructing readable {@link #toString()}- and {@link #toStringBuilder(StringBuilder, int)}-methods for statement trees
     * 
     * @param sb the string builder, to which has to be appended
     * @param indentation number defining the level of indentation
     * @param text an (optional) text to append; may be null
     */
    protected final void appendNewLine(StringBuilder sb, int indentation, String text) {
        sb.append("\n");
        for ( int i = 0; i < indentation; i++ ) {
            sb.append(" ");
        }
        if ( text != null ) {
            sb.append(text);
        }
    }

    /**
     * define the different kinds of Sensor. If a new subclass of {@link #Sensor} is created, this enum has to be extended. The new enum value has be
     * the return value of the method {@link Sensor#getKind()} of the new class.
     */
    public static enum Kind {
        ColorSensor( ColorSensor.class ), TouchSensor( TouchSensor.class ), UltraSSensor( UltraSSensor.class );

        private final Class<?> clazz;

        private Kind(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getAktionClass() {
            return this.clazz;
        }
    }
}
