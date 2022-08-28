package de.fhg.iais.roberta.transformer.forField;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;

/**
 * <b>This Nepo field annotation can be used to parse the 'mutation' XML substructure from the XML representation of blockly blocks.</b>
 * Example XML
 * <pre>{@code
 *  <block>
 *      <mutation next="false" declaration_type="Number"></mutation>
 *      <field name="VAR">x</field>
 *  </block>
 * }</pre>
 *
 * <b>The field annotated with {@link NepoMutation} must be of type {@link Mutation} and public!</b><br>
 * The class using this Annotation should also be annotated with either {@link NepoPhrase} or {@link NepoExpr}
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoMutation {
    List<Class<?>> VALID_TYPES = Collections.singletonList(Mutation.class);

    String fieldName() default "";
}
