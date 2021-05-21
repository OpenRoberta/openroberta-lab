package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.typecheck.BlocklyType;

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoMutation {
    public static final String DEFAULT_FIELD_VALUE = "__this is no field, this is a value__";

    boolean required() default true;

    String fieldName() default "";

    BlocklyType fieldType() default BlocklyType.NOTHING;

    String isFieldWithDefault() default DEFAULT_FIELD_VALUE;
}
