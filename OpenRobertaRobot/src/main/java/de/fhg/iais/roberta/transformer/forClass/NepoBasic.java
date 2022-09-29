package de.fhg.iais.roberta.transformer.forClass;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * <b>This Nepo class annotation can be used to parse complete xml blocks as exported from blockly to AST classes.</b><br>
 * It is used for all blocks, which cannot be annotated with Nepo field annotations (if they are bad designed, no time to refactor, ...)
 * <br><br>
 * Each class annotated by {@link NepoBasic} must have public static methods xml2ast and public method ast2xml for transformation between
 * blockly blocks and AST objects and the backtransformation
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoBasic {
    /**
     * name of the block. This is different from the blockly names, that are used in the blockly XML (see {@link #blocklyNames()}
     */
    String name();

    /**
     * the category this block belongs to (e.g. Sensor, Actor, Stmt, ... . Deprecated, should be removed soon)
     */
    String category();

    /**
     * the blockly names, as used in the XML exported from blockly, that have to be transforrmed to an object of this AST class.
     */
    String[] blocklyNames();

    /**
     * the huge get sample sensor allows to integrate many sensors together with a mode of this sensor (to avoid a dropdown).
     * This list defines the association between the field name as used in blockly
     * - implicitly to the sensor (as this sample value is defined in this AST class, which relates to a sensor)
     * - explicitly from the blockly field value to the sensor's mode (you'll see, that the sensor's mode usually
     * is a suffix of the blockly field value)
     */
    F2M[] sampleValues() default {};

    /**
     * Define the return type of the expression.
     * Used by {@link Expr#getReturnType()}
     */
    BlocklyType blocklyType() default BlocklyType.NOTHING;

}
