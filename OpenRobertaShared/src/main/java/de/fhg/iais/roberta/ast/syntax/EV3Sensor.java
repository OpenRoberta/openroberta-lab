package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.dbc.Assert;

public class EV3Sensor extends HardwareComponent {

    /**
     * Creates hardware component of type {@link Category#SENSOR} that will be attached to the brick configuration.
     * Client must provide valid {@link HardwareComponentType} from {@link Category#SENSOR} category.
     *
     * @param componentType of the sensor
     */
    public EV3Sensor(HardwareComponentType componentType) {
        Assert.isTrue(componentType.getCategory() == Category.SENSOR);
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
