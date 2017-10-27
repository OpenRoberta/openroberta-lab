package de.fhg.iais.roberta.inter.mode.general;

/**
 * The enumeration implementing this interface should contain the possible operations that can be performed over an element in list.
 *
 * @author kcvejoski
 */
public interface IListElementOperations extends IMode {

    /**
     * @return true if the operation does not return value
     */
    public boolean isStatment();
}
