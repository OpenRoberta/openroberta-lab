package de.fhg.iais.roberta.components;

import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This is a top class of all hardware components that can be connected to any kind of brick. All more specific hardware components must extend
 * {@link HardwareComponentType}.
 */
public abstract class HardwareComponentType {
    private final String name;
    private final String shortName;
    private final Category category;

    /**
     * This constructor sets the name of the blockly block that maps to this component and {@link Category} of the hardware component.
     *
     * @param name
     * @param category
     */
    public HardwareComponentType(String name, String shortName, Category category) {
        Assert.isTrue(!name.equals("") && shortName != null && category != null);
        this.name = name;
        this.shortName = shortName;
        this.category = category;
    }

    /**
     * The name of the blockly block mapped to this hardware component.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * The short name of the blockly block mapped to this hardware component. This name is used for textual representations of this component.
     *
     * @return the short name
     */
    public String getShortName() {
        return this.shortName;
    }

    /**
     * Get the category in which belongs. See enum {@link Category} for all categories.
     *
     * @return the category
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * The name of the component.
     *
     * @return the name
     */
    public abstract String getTypeName();

    @Override
    public String toString() {
        return "HardwareComponentType [" + this.name + ", " + this.category + "]";
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.category == null) ? 0 : this.category.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.shortName == null) ? 0 : this.shortName.hashCode());
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        HardwareComponentType other = (HardwareComponentType) obj;
        if ( this.category != other.category ) {
            return false;
        }
        if ( this.name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !this.name.equals(other.name) ) {
            return false;
        }
        if ( this.shortName == null ) {
            if ( other.shortName != null ) {
                return false;
            }
        } else if ( !this.shortName.equals(other.shortName) ) {
            return false;
        }
        return true;
    }

}