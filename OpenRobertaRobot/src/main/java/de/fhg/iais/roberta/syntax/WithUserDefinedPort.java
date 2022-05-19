package de.fhg.iais.roberta.syntax;

public interface WithUserDefinedPort<V> {

    /**
     * @return the user defined port, may be ""; never null
     */
    String getUserDefinedPort();
}
