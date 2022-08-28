package de.fhg.iais.roberta.transformer.forClass;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

/**
 * <b>This Nepo class annotation can be used to parse complete xml blocks as exported from blockly to AST classes.</b><br>
 * It is used for statement-like blocks
 * <br><br>
 * Each class annotated by {@link NepoPhrase} must have a constructor accepting the following types in this specific order:<br>
 * ({@link BlockDescriptor}, {@link BlocklyProperties}, {@link BlocklyComment}, fieldsValues... )<br>
 * where fieldsValues... is a placeholder for the list of fields annotated with a Nepo field Annotation. They must be <b>in the order there are defined!</b>
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoPhrase {
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
     * Define the return type of the phrase. Defaults to NOTHING. Currently not used.
     */
    BlocklyType blocklyType() default BlocklyType.VOID;

    /**
     * the huge get sample sensor allows to integrate many sensors together with a mode of this sensor (to avoid a dropdown).
     * This list defines the association between the field name as used in blockly
     * - implicitly to the sensor (as this sample value is defined in this AST class, which relates to a sensor)
     * - explicitly from the blockly field value to the sensor's mode (you'll see, that the sensor's mode usually
     * is a suffix of the blockly field value)
     */
    F2M[] sampleValues() default {};
}
