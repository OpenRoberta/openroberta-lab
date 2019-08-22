package de.fhg.iais.roberta.visitor.codegen.utilities;

import de.fhg.iais.roberta.syntax.SC;

public class ColorSensorUtils {
    public static boolean isHiTecColorSensor(String colorSensorType) {
        return colorSensorType.toLowerCase().contains(SC.HT_COLOR.toLowerCase());
    }

    public static boolean isEV3ColorSensor(String colorSensorType) {
        return colorSensorType.equalsIgnoreCase(SC.COLOR);
    }
}
