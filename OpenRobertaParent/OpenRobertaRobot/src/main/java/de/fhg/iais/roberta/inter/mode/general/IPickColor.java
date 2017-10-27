package de.fhg.iais.roberta.inter.mode.general;

/**
 * The enumeration implementing this interface should contain all the possible color that color sensor can recognize.
 *
 * @author kcvejoski
 */
public interface IPickColor extends IMode {

    /**
     * @return a user defined id associated to every color
     */
    public int getColorID();

    /**
     * @return the hexadecimal code for a color.
     */
    public String getHex();
}
