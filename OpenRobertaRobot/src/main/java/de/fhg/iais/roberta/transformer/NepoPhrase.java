package de.fhg.iais.roberta.transformer;

import java.lang.annotation.*;

import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.typecheck.BlocklyType;

@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoPhrase {
    String containerType() default "";

    BlocklyType blocklyType() default BlocklyType.NOTHING;
}
