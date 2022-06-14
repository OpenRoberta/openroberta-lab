package de.fhg.iais.roberta.transformer.forField;

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
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;


/**
 * <b>This Nepo field annotation can be used to parse a value XML substructure, containing an inner block from the XML representation of blockly blocks.</b><br>
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
 *          <block type="math_number" id="abc" intask="true">
 *              <field name="NUM">0</field>
 *          </block>
 *      </value>
 *  </block>
 * }</pre>
 * <p>
 * The field annotated with {@link NepoValue} must be a valid type (see {@link #VALID_TYPES}) and public<br>
 * The class using this Annotation should also be annotated with either {@link NepoPhrase} or {@link NepoExpr}.
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
