package de.fhg.iais.roberta.components.ev3;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.HardwareComponentType;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class represents all possible EV3 actors.
 */
public class EV3Actors extends HardwareComponentType {
    public static final EV3Actors EV3_MEDIUM_MOTOR = new EV3Actors("robBrick_motor_middle", "middle motor");
    public static final EV3Actors EV3_LARGE_MOTOR = new EV3Actors("robBrick_motor_big", "big motor");

    /**
     * This constructors set the name that maps the actor to specific blockly block.
     *
     * @param name of the blockly block for this actor
     */
    private EV3Actors(String name, String shortName) {
        super(name, shortName, Category.ACTOR);
    }

    /**
     * Find EV3 actor by name of blockly block
     *
     * @param blocklyName
     * @return EV3 actor
     */
    public static EV3Actors find(String blocklyName) {
        Iterator<EV3Actors> iter = ACTORS.iterator();
        while ( iter.hasNext() ) {
            EV3Actors actor = iter.next();
            if ( blocklyName.equals(actor.getName()) ) {
                return actor;
            }
        }
        throw new DbcException();
    }

    private static final EV3Actors[] values = {
        EV3_MEDIUM_MOTOR, EV3_LARGE_MOTOR
    };

    public static final List<EV3Actors> ACTORS = Collections.unmodifiableList(Arrays.asList(values));

    @Override
    public String getTypeName() {
        int counter = 0;
        for ( EV3Actors actor : ACTORS ) {
            if ( actor.getName().equals(this.getName()) ) {
                return this.getClass().getFields()[counter].getName();
            }
            counter++;
        }
        throw new DbcException();
    }
}