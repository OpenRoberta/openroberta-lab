package de.fhg.iais.roberta.brickconfiguration.ev3;

import de.fhg.iais.roberta.brickconfiguration.HardwareComponent;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.hardwarecomponents.Category;
import de.fhg.iais.roberta.hardwarecomponents.ev3.HardwareComponentEV3Sensor;

public class EV3Sensor extends HardwareComponent {

    /**
     * Creates hardware component of type {@link Category#SENSOR} that will be attached to the brick configuration.
     * Client must provide valid {@link HardwareComponentType} from {@link Category#SENSOR} category.
     *
     * @param componentType of the sensor
     */
    public EV3Sensor(HardwareComponentEV3Sensor componentType) {
        Assert.isTrue(componentType != null);
        this.setComponentType(componentType);
    }

    @Override
    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("new EV3Sensor(").append(getComponentType().getJavaCode());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "EV3Sensor [" + getComponentType() + "]";
    }
}
