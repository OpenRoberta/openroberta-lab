package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.typecheck.BlocklyType;


/**
 * <p>
 * This annotation can be used to parse xml value objects from blocks.<br>
 * A value object always contains another nested bock!
 * </p>
 *
 * <p>
 * The attribute {@link NepoValue#name()} is used to determine the correct value object in the block and must match the XML name attribute of the value object.
 * If the given value is not present {@link NepoValue#type()} is used to construct a {@link EmptyExpr} with the correct type.
 * </p>
 *
 * <br>
 * Example XML
 * <pre>{@code
 *  <block>
 *      <value name="VALUE">
 *          <block type="math_number" id="!k:I/O]~:8SW7#]/D()O" intask="true">
 *              <field name="NUM">0</field>
 *          </block>
 *      </value>
 *  </block>
 * }</pre>
 *
 * <b>The field annotated with {@link NepoValue} must be of type {@link Expr} or {@link Var} and public!</b><br>
 * The class using this Annotation should also be annoted with either {@link NepoPhrase} or {@link NepoOp}.
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoValue {
    List<Class<?>> VALID_TYPES = Arrays.asList(Var.class, Expr.class);

    /**
     * Is used to determine the correct value object in the block and must match the XML name attribute of the value object.
     */
    String name() default "";

    /**
     * If the value XML object is not present this is used to construct a {@link EmptyExpr} with the correct type.
     */
    BlocklyType type() default BlocklyType.NOTHING;
}
