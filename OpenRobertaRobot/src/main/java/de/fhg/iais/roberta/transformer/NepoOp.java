package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * Annotate AST classes which are operators (the leaves in an expression {@link Expr}) and should be parsed with the new Annotation system with this.
 * The annotation declares the basic properties of an operator (precedence, association), but also defines the {@link NepoPhrase#containerType()}.
 *
 * <br><br>
 * Each class annotated by {@link NepoPhrase} must have a constructor accepting the following types in this specific order:<br>
 * ({@link BlockType}, {@link BlocklyBlockProperties}, {@link BlocklyComment}, |fields / values / ...| )<br>
 * where |fields / values / ...| is a placeholder for all the types of the fields annotated with a Nepo-Annotation, and they must also be <b>in the order there are defined!</b>
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoOp {
    /**
     * Container type used to determine the type of the block. Must be accessible via {@link BlockTypeContainer#getByName(String)}! (see {@link BlockTypeContainer#add(String, Category, Class, String...)})
     */
    String containerType() default "";

    /**
     * Define the return type of the expression.
     * Used by {@link Expr#getVarType()}
     */
    BlocklyType blocklyType() default BlocklyType.NOTHING;

    /**
     * Define the precedence of the expression.
     * Used by {@link Expr#getPrecedence()}
     */
    int precedence() default 999;

    /**
     * Define the assosiaction of the expression
     * Used by {@link Expr#getAssoc()}
     */
    Assoc assoc() default Assoc.NONE;
}
