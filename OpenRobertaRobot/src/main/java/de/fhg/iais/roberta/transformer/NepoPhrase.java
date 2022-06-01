package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

/**
 * Annotate AST classes that general phrases in the abstract syntax tree and should be parsed with the annotation system.
 * It is used to define the {@link NepoPhrase#containerType()}.
 *
 * <br><br>
 * Each class annotated by {@link NepoPhrase} must have a constructor accepting the following types in this specific order:<br>
 * ({@link BlockType}, {@link BlocklyBlockProperties}, {@link BlocklyComment}, |fields / values / ...| )<br>
 * where |fields / values / ...| is a placeholder for all the types of the fields annotated with a Nepo-Annotation and they must also be <b>in the order there are defined!</b>
 *
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface NepoPhrase {
    /**
     * Container type used to determine the type of the block. Must be accessible via {@link BlockTypeContainer#getByName(String)}! (see {@link BlockTypeContainer#add(String, Category, Class, String...)})
     */
    String containerType() default "";
}
