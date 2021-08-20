package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Hide;

/**
 * This annotation can be used to parse xml hide objects from blocks.<br>
 * Example XML
 * <pre>{@code
 *  <block>
 *      <field name="SENSORPORT">G</field>
 *      <hide name="SENSORPORT" value="G"/>
 *  </block>
 * }</pre>
 *
 * <b>The field annotated with {@link NepoHide} must be of type {@link Hide} and public!</b><br>
 * The class using this Annotation should also be annoted with either {@link NepoPhrase} or {@link NepoOp}
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoHide {
    List<Class<?>> VALID_TYPES = Collections.singletonList(Hide.class);
}
