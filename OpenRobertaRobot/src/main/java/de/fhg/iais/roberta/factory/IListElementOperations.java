package de.fhg.iais.roberta.factory;

/**
 * The enumeration implementing this interface should contain the possible operations that can be performed over an element in list.
 *
 * @author kcvejoski
 */
public interface IListElementOperations extends IMode {
    /**
     * @return array of values alternative to the enumeration value.
     */
    public String[] getValues();

    /**
     * @return true if the operation does not return value
     */
    public boolean isStatment();
}
