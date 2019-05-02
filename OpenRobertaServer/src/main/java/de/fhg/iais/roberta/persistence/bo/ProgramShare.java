package de.fhg.iais.roberta.persistence.bo;

public interface ProgramShare {

    /**
     * Returns the relation for the current sharing of the program with the corresponding entity
     *
     * @return The associated relation. Never <code>null</code>
     */
    public Relation getRelation();

    /**
     * Returns the user friendly string identifier (label) for the entity with which the program is being shared
     *
     * @return
     */
    public String getEntityLabel();

    /**
     * Returns the type of the entity with which the program is being shared
     *
     * @return
     */
    public String getEntityType();

    /**
     * Returns the program that is being shared to the entity
     *
     * @return The associated program. Never <code>null</code>
     */
    public Program getProgram();
}
