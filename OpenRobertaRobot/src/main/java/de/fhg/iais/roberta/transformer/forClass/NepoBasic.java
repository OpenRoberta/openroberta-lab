package de.fhg.iais.roberta.transformer.forClass;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;

/**
 * <b>This Nepo class annotation can be used to parse complete xml blocks as exported from blockly to AST classes.</b><br>
 * It is used for all blocks, which cannot be annotated with Nepo field annotations (if they are bad designed, no time to refactor, ...)
 * <br><br>
 * Each class annotated by {@link NepoPhrase} must have public static methods jaxbToAst and astToBlock for transformation between
 * blockly blocks and AST objects and the backtransformation
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoBasic {
    /**
     * either all robots ({}) or a list of robots, that may use this block
     */
    String[] robots() default {};

    /**
     * the category this block belongs to (e.g. Sensor, Actor, Stmt, ... . Deprecated, should be removed soon)
     */
    String category();

    /**
     * Container type used to determine the type of the block.
     * Must be accessible via {@link BlockTypeContainer#getByName(String)}! (see {@link BlockTypeContainer#add(String, Category, Class, String...)})
     */
    String containerType();

    /**
     * the blockly names, as used in the XML exported from blockly, that have to be transforrmed to an object of this AST class.
     */
    String[] blocklyNames();

    /**
     * Define the return type of the expression.
     * Used by {@link Expr#getReturnType()}
     */
    BlocklyType blocklyType() default BlocklyType.NOTHING;
}
