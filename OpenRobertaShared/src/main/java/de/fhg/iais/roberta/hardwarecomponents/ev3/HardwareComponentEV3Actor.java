package de.fhg.iais.roberta.hardwarecomponents.ev3;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.hardwarecomponents.Category;
import de.fhg.iais.roberta.hardwarecomponents.HardwareComponentType;

/**
 * This class represents all possible EV3 actors.
 */
public class HardwareComponentEV3Actor extends HardwareComponentType {
    public static final HardwareComponentEV3Actor EV3_MEDIUM_MOTOR = new HardwareComponentEV3Actor("robBrick_motor_middle");
    public static final HardwareComponentEV3Actor EV3_LARGE_MOTOR = new HardwareComponentEV3Actor("robBrick_motor_big");

    /**
     * This constructors set the name that maps the actor to specific blockly block.
     *
     * @param name of the blockly block for this actor
     */
    public HardwareComponentEV3Actor(String name) {
        super(name, Category.ACTOR);
    }

    /**
     * Find EV3 actor by name of blockly block
     *
     * @param blocklyName
     * @return EV3 actor
     */
    public static HardwareComponentEV3Actor find(String blocklyName) {
        Iterator<HardwareComponentEV3Actor> iter = ACTORS.iterator();
        while ( iter.hasNext() ) {
            HardwareComponentEV3Actor actor = iter.next();
            if ( blocklyName.equals(actor.getName()) ) {
                return actor;
            }
        }
        throw new DbcException();
    }

    private static final HardwareComponentEV3Actor[] values = {
        EV3_MEDIUM_MOTOR, EV3_LARGE_MOTOR
    };

    public static final List<HardwareComponentEV3Actor> ACTORS = Collections.unmodifiableList(Arrays.asList(values));

    @Override
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + getTypeName();
    }

    @Override
    public String getTypeName() {
        int counter = 0;
        for ( HardwareComponentEV3Actor actor : ACTORS ) {
            if ( actor.getName().equals(this.getName()) ) {
                return this.getClass().getFields()[counter].getName();
            }
            counter++;
        }
        throw new DbcException();
    }
}