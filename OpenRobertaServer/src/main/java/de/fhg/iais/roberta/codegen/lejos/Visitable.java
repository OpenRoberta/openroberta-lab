package de.fhg.iais.roberta.codegen.lejos;

/**
 * This interface is implemented by all classes representing the AST and it enables them to be visited and generated valid code from them.
 * 
 * @author kcvejoski
 */
public interface Visitable {
    /**
     * This method is implemented by all class representing the AST and enables to the target class to accept the visiotr.
     * 
     * @param visitor
     */
    public void accept(Visitor visitor);
}
