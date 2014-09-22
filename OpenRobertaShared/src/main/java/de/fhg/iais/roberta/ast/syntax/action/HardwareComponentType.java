package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Arrays;

import de.fhg.iais.roberta.ast.syntax.Category;
import de.fhg.iais.roberta.dbc.DbcException;

public enum HardwareComponentType {
    EV3ColorSensor( Category.SENSOR, "color", "colour", "Farbe" ), //
    EV3TouchSensor( Category.SENSOR, "touch", "Berührung" ),
    EV3UltrasonicSensor( Category.SENSOR, "ultrasonic", "Ultraschall" ),
    EV3IRSensor( Category.SENSOR, "infrared", "Infrarot" ),
    RotationSensor( Category.SENSOR, "rotation", "Drehung" ), // => motor rotations -> Category.AKTOR???
    BrickSensor( Category.SENSOR, "?" ),
    EV3GyroSensor( Category.SENSOR, "gyro" ),
    EV3MediumRegulatedMotor( Category.ACTOR, "regulated", "middle" ),
    EV3LargeRegulatedMotor( Category.ACTOR, "regulated", "large" ),
    EV3MediumUnRegulatedMotor( Category.ACTOR, "unregulated", "middle" ),
    EV3LargeUnRegulatedMotor( Category.ACTOR, "unregulated", "large" ),
    BasicMotor( Category.ACTOR, "unregulated", "large" ),
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

    public Category getCategory() {
        return this.category;
    }

    public boolean attributesMatchAttributes(String... attributes) {
        for ( String attribute : attributes ) {
            attribute = attribute.toLowerCase();
            if ( Arrays.binarySearch(this.attributes, attribute) < 0 ) {
                return false;
            }
        }
        return true;
    }

    public static HardwareComponentType attributesMatch(String... attributes) {
        for ( HardwareComponentType hardwareComponent : HardwareComponentType.values() ) {
            if ( hardwareComponent.attributesMatchAttributes(attributes) ) {
                return hardwareComponent;
            }
        }
        throw new DbcException("No hardware component matches attributes " + Arrays.toString(attributes));
    }

    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + this;
    }
}