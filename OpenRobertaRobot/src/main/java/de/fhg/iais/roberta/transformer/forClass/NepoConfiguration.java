package de.fhg.iais.roberta.transformer.forClass;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>This Nepo class annotation can be used to parse configuration xml blocks as exported from blockly in the configuration segment.</b><br>
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoConfiguration {
    /**
     * name of the configuration block. This is different from the blockly names, that are used in the blockly XML (see {@link #blocklyNames()}
     */
    String name();

    /**
     * the category this block belongs to (must be "CONFIGURATION_BLOCK")
     */
    String category();

    /**
     * the blockly names, as used in the XML exported from blockly, that have to be transforrmed to an object of this AST class.
     */
    String[] blocklyNames();
}
