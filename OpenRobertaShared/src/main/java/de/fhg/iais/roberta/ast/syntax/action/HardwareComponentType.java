package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Arrays;

import de.fhg.iais.roberta.ast.syntax.Category;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * All hardware components that can be connected to the brick.
 */
public enum HardwareComponentType {
    EV3ColorSensor( Category.SENSOR, "color", "colour", "Farbe" ), //
    EV3TouchSensor( Category.SENSOR, "touch", "Berührung" ),
    EV3UltrasonicSensor( Category.SENSOR, "ultrasonic", "Ultraschall" ),
    EV3IRSensor( Category.SENSOR, "infrared", "Infrarot" ),
    RotationSensor( Category.SENSOR, "rotation", "Drehung" ), // => motor rotations -> Category.AKTOR???
    BrickSensor( Category.SENSOR, "?" ),
    EV3GyroSensor( Category.SENSOR, "gyro" ),
    EV3MediumRegulatedMotor( Category.ACTOR, "regulated", "middle", "left", "right", "off", "on" ),
    EV3LargeRegulatedMotor( Category.ACTOR, "regulated", "large", "left", "right", "off", "on" ),
    EV3MediumUnRegulatedMotor( Category.ACTOR, "unregulated", "middle", "left", "right", "off", "on" ),
    EV3LargeUnRegulatedMotor( Category.ACTOR, "unregulated", "large", "left", "right", "off", "on" ),
    BasicMotor( Category.ACTOR, "unregulated", "large", "left", "right", "off", "on" ),
    NXTMotor( Category.ACTOR, "PH" ),
    NXTRegulatedMotor( Category.ACTOR, "PH" );

    private final Category category;
    private final String[] attributes;

    private HardwareComponentType(Category category, String... attributes) {
        this.category = category;
        this.attributes = Arrays.copyOf(attributes, attributes.length);
        for ( int i = 0; i < attributes.length; i++ ) {
            this.attributes[i] = this.attributes[i].toLowerCase();
        }
        Arrays.sort(this.attributes);
    }

    /**
     * @return category in which the component belongs
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * @param attributes that are checked
     * @return true if the attributes are contained in the hardware component
     */
    public boolean attributesMatchAttributes(String... attributes) {
        for ( String attribute : attributes ) {
            attribute = attribute.toLowerCase();
            if ( Arrays.binarySearch(this.attributes, attribute) < 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param attributes that hardware component type should contain
     * @return hardware component that contains given attributes
     */
    public static HardwareComponentType attributesMatch(String... attributes) {
        for ( HardwareComponentType hardwareComponent : HardwareComponentType.values() ) {
            if ( hardwareComponent.attributesMatchAttributes(attributes) ) {
                return hardwareComponent;
            }
        }
        throw new DbcException("No hardware component matches attributes " + Arrays.toString(attributes));
    }

    /**
     * @return valid Java code name of the enumeration
     */
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }
}