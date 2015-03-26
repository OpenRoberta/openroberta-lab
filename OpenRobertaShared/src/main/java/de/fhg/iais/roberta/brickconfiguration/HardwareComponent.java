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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.componentType == null) ? 0 : this.componentType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        HardwareComponent other = (HardwareComponent) obj;
        if ( this.componentType == null ) {
            if ( other.componentType != null ) {
                return false;
            }
        } else if ( !this.componentType.equals(other.componentType) ) {
            return false;
        }
        return true;
    }
}
