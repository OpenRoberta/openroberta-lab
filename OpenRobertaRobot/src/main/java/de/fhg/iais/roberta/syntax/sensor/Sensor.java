package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

/**
 * the top class of all sensors. There are two ways for a client to find out which kind of a {@link #Sensor}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Sensor extends Phrase {

    /**
     * This constructor set the kind of the sensor object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockDescriptor}.
     *
     * @param kind of the the sensor object used in AST,
     * @param properties of the block (see {@link BlocklyProperties}),
     * @param comment of the user for the specific block
     */
    public Sensor(BlocklyProperties properties) {
        super(properties);
    }
}
