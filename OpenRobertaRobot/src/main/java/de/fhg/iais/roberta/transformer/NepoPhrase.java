package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.typecheck.BlocklyType;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoPhrase {
    String containerType() default "";

    BlocklyType blocklyType() default BlocklyType.NOTHING;
}
