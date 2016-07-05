package de.fhg.iais.roberta.factory;

/**
 * The enumeration implementing this interface should contain all the possible color that color sensor can recognize.
 *
 * @author kcvejoski
 */
public interface IPickColor extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();

    /**
     * @return a user defined id associated to every color
     */
    public int getColorID();

    /**
     * @return the hexadecimal code for a color.
     */
    public String getHex();
}
