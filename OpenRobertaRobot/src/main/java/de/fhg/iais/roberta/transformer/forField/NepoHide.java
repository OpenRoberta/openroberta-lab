package de.fhg.iais.roberta.transformer.forField;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;

/**
 * <b>This Nepo field annotation can be used to parse the 'hide' XML substructure from the XML representation of blockly blocks.</b>
 * Example XML
 * <pre>{@code
 *  <block>
 *      <field name="SENSORPORT">G</field>
 *      <hide name="SENSORPORT" value="G"/>
 *  </block>
 * }</pre>
 *
 * <b>The field annotated with {@link NepoHide} must be of type {@link Hide} and public!</b><br>
 * The class using this Annotation should also be annotated with either {@link NepoPhrase} or {@link NepoExpr}
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoHide {
    List<Class<?>> VALID_TYPES = Collections.singletonList(Hide.class);
}
