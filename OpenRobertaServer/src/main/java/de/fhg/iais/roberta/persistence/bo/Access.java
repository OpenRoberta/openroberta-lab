package de.fhg.iais.roberta.persistence.bo;


/**
 * the different states of a program. TODO: Redesign. This is more a simplified proposal than a well designed class.
 * 
 * @author rbudde
 */
public enum Access {
    /**
     * private use, noboby but the owner may access the program. This is the intial state.
     */
    Private,
    /**
     * Program may be accessed by a group.
     */
    Group,
    /**
     * Program may be read by all users.
     */
    All;
}
