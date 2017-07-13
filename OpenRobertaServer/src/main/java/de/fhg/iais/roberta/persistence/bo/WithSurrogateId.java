package de.fhg.iais.roberta.persistence.bo;

/**
 * This interface is extended by business classes, that possess a technical key generated when the object is persisted in a database.<br>
 * <b>Be careful:</b> The key may not be used in hashcode and equals!
 * 
 * @author rbudde
 */
public interface WithSurrogateId {
    /**
     * @return the (technical) key of this object. The key is generated automatically by hibernate, when the object is saved. The key may not be used in
     *         hashcode and equals!
     */
    int getId();
}
