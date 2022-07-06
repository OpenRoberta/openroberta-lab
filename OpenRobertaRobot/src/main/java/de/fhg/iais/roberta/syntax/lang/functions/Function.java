package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * the top class of all functions. There are two ways for a client to find out which kind of function {@link #Function}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Function extends Phrase {

    /**
     * create a mutable function of the given {@link BlockDescriptor}
     */
    public Function(BlocklyProperties properties) {
        super(properties);

    }

    /**
     * get the precedence of the expression
     * <b>This is the default implementation of annotated AST classes</b>
     *
     * @return the precedence
     */
    public int getPrecedence() {
        return AnnotationHelper.getPrecedence(this.getClass());
    }

    /**
     * get the association of the expression
     * <b>This is the default implementation of annotated AST classes</b>
     *
     * @return the association
     */
    public Assoc getAssoc() {
        return AnnotationHelper.getAssoc(this.getClass());
    }

    /**
     * get the BlocklyType (used for typechecking ...) of this expression
     * <b>This is the default implementation of annotated AST classes</b>
     *
     * @return the BlocklyType
     */
    public BlocklyType getReturnType() {
        return AnnotationHelper.getReturnType(this.getClass());
    }

}
