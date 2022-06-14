package de.fhg.iais.roberta.transformer.forClass;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;


/**
 * <b>This Nepo class annotation can be used to parse a value XML substructure from the XML representation of blockly blocks, which is used to define
 * all kinds of external sensors.</b><br>
 * <br>
 * For implementation details see {@link ExternalSensor} and {@link ExternalSensor#extractPortAndModeAndSlot}
 * <br>
 * The class using this Annotation should have a second class annotation, either {@link NepoPhrase} or {@link NepoExpr}.
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoExternalSensor {
}
