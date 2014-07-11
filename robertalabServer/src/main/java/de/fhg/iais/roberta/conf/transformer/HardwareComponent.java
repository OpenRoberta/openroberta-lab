package de.fhg.iais.roberta.conf.transformer;

import java.util.Arrays;

import de.fhg.iais.roberta.ast.syntax.Phrase.Category;
import de.fhg.iais.roberta.dbc.DbcException;

public enum HardwareComponent {
    ColorSensor( Category.SENSOR, "color", "colour", "Farbe" ), //
    TouchSensor( Category.SENSOR, "touch", "Berührung" ),
    UltraSSensor( Category.SENSOR, "ultrasonic", "Ultraschall" ),
    InfraredSensor( Category.SENSOR, "infrared", "Infrarot" ),
    RotationSensor( Category.SENSOR, "rotation", "Drehung" ),
    BrickSensor( Category.SENSOR, "?" ),
    GyroSensor( Category.SENSOR, "gyro" ),
    RegulatedMiddleMotor( Category.AKTOR, "regulated", "middle" ),
    RegulatedLargeMotor( Category.AKTOR, "regulated", "large" ),
    UnRegulatedMiddleMotor( Category.AKTOR, "unregulated", "middle" ),
    UnRegulatedLargeMotor( Category.AKTOR, "unregulated", "large" );

    private final Category category;
    private final String[] attributes;

    private HardwareComponent(Category category, String... attributes) {
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

    public static HardwareComponent attributesMatch(String... attributes) {
        for ( HardwareComponent hardwareComponent : HardwareComponent.values() ) {
            if ( hardwareComponent.attributesMatchAttributes(attributes) ) {
                return hardwareComponent;
            }
        }
        throw new DbcException("No hardwarecomponent matches attributes " + Arrays.toString(attributes));
    }
}