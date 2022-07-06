package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * the top class of all expressions. To find out which kind an {@link #Expr}-object is use {@link #getKind()}
 */
public abstract class Expr extends Phrase {

    public Expr(BlocklyProperties properties) {
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
    public BlocklyType getVarType() {
        return AnnotationHelper.getReturnType(this.getClass());
    }

}
