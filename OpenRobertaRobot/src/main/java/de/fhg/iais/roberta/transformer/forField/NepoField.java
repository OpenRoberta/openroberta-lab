package de.fhg.iais.roberta.transformer.forField;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;

/**
 * <b>This Nepo field annotation can be used to parse a field from the XML representation of blockly blocks.</b><br>
 * <p>
 * The attribute {@link NepoField#name()} must match the name attribute of the corresponding XML field object.<br>
 * The attribute {@link NepoField#value()} is the default value if there is no field object with the name in the block.
 * </p>
 * <br>
 * Example XML
 * <pre>{@code
 *  <block>
 *      <field name="MOTORPORT">B</field>
 *  </block>
 * }</pre>
 * <p>
 * The field annotated with {@link NepoField} must be of type {@link String}, {@link Boolean}, boolean, {@link Double}, double or an instance of {@link Enum} and public<br>
 * The class using this Annotation should also be annotated with either {@link NepoPhrase} or {@link NepoExpr}
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoField {
    List<Class<?>> VALID_TYPES = Arrays.asList(String.class, Boolean.class, boolean.class, Double.class, double.class, Enum.class);

    /**
     * The XML name property which is used to identify the correct field object in the block.
     */
    String name();

    /**
     * Default value if the value is not present in the XML
     * If the Java field type is not String, e.g. a double, Enum or boolean this can be the String representation of this value (e.g. double -> "TRUE", Enum -> "SET", double -> "2.15")
     */
    String value() default "";
}
