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
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

/**
 * <b>This Nepo class annotation can be used to parse complete xml blocks as exported from blockly to AST classes.</b><br>
 * It is used for statement-like blocks
 * <br><br>
 * Each class annotated by {@link NepoPhrase} must have a constructor accepting the following types in this specific order:<br>
 * ({@link BlockDescriptor}, {@link BlocklyBlockProperties}, {@link BlocklyComment}, fieldsValues... )<br>
 * where fieldsValues... is a placeholder for the list of fields annotated with a Nepo field Annotation. They must be <b>in the order there are defined!</b>
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoPhrase {
    /**
     * Container type used to determine the type of the block.
     * Must be accessible via {@link AstFactory#getByName(String)}! (see {@link AstFactory#add(String, Category, Class, String...)})
     */
    String containerType();

    /**
     * the blockly names, as used in the XML exported from blockly, that have to be transforrmed to an object of this AST class.
     */
    String[] blocklyNames();

    /**
     * the category this block belongs to (e.g. Sensor, Actor, Stmt, ... . Deprecated, should be removed soon)
     */
    String category();

    /**
     * Define the return type of the expression.
     * Used by {@link Expr#getReturnType()}
     */
    BlocklyType blocklyType() default BlocklyType.VOID;

    /**
     * Define the return type of the expression.
     * Used by {@link Expr#getReturnType()}
     */
    NepoSampleValue[] sampleValues() default {};
}
