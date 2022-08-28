package de.fhg.iais.roberta.transformer.forField;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;

/**
 * <b>This Nepo field annotation can be used to parse the 'data' XML substructure from the XML representation of blockly blocks.</b><br>
 * <p>
 * Example XML
 * <pre>{@code
 *  <block>
 *      <data>ev3</data>
 *  </block>
 * }</pre>
 * <p>
 * The field annotated with {@link NepoData} must be of type {@link String} and public<br>
 * The data object must always be present on the block that gets parsed<br>
 * The class using this Annotation should be annotated with either {@link NepoPhrase} or {@link NepoExpr}
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoData {
    List<Class<?>> VALID_TYPES = Collections.singletonList(String.class);
}
