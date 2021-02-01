package de.fhg.iais.roberta.syntax;

/**
 * all Phrases (at least actors) with an user defined port should implement this interface to make hardware collection esay
 */
public interface WithUserDefinedPort<V> {

    /**
     * @return the user defined port, may be ""; never null
     */
    String getUserDefinedPort();

}
