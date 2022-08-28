package de.fhg.iais.roberta.util.syntax;

public interface WithUserDefinedPort<V> {

    /**
     * @return the user defined port, may be ""; never null
     */
    String getUserDefinedPort();
}
