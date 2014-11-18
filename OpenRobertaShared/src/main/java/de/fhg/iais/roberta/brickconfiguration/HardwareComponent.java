package de.fhg.iais.roberta.brickconfiguration;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.hardwarecomponents.Category;
import de.fhg.iais.roberta.hardwarecomponents.HardwareComponentType;

abstract public class HardwareComponent {
    private HardwareComponentType componentType;

    public HardwareComponent() {
        super();
    }

    /**
     * Set a component type
     *
     * @param componentType
     */
    public void setComponentType(HardwareComponentType componentType) {
        this.componentType = componentType;
    }

    /**
     * @return type of the hardware component
     */
    public HardwareComponentType getComponentType() {
        Assert.isTrue(this.componentType != null, "Hardware type is null!");
        return this.componentType;
    }

    /**
     * @return category in which the component belongs
     */
    public Category getCategory() {
        return this.componentType.getCategory();
    }

    /**
     * @return Java code used in the code generation to regenerates the same hardware component
     */
    abstract public String generateRegenerate();
}
