package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;
import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;
import de.fhg.iais.roberta.ast.syntax.action.MotorSide;
import de.fhg.iais.roberta.dbc.Assert;

public class HardwareComponentN {
    private final HardwareComponentType componentType;
    private final DriveDirection rotationDirection;
    private final MotorSide motorSide;

    public HardwareComponentN(HardwareComponentType componentType, DriveDirection rotationDirection, MotorSide motorSide) {
        Assert.isTrue(componentType != null);
        this.componentType = componentType;
        this.rotationDirection = rotationDirection;
        this.motorSide = motorSide;
    }

    public HardwareComponentN(HardwareComponentType componentType) {
        Assert.isTrue(componentType != null);
        this.componentType = componentType;
        this.rotationDirection = null;
        this.motorSide = null;
    }

    public HardwareComponentType getComponentType() {
        return this.componentType;
    }

    public DriveDirection getRotationDirection() {
        return this.rotationDirection;
    }

    public MotorSide getMotorSide() {
        return this.motorSide;
    }

    public Category getCategory() {
        return this.componentType.getCategory();
    }

    public boolean isRegulated() {
        return this.componentType.attributesMatchAttributes("regulated");
    }

    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("new HardwareComponentN(").append(this.componentType.name());
        if ( getCategory() == Category.ACTOR ) {
            sb.append(", ").append(this.rotationDirection.name()).append(", ").append(this.motorSide.name());
        }
        return sb.toString();
    }

    public static HardwareComponentType attributesMatch(String... attributes) {
        return HardwareComponentType.attributesMatch(attributes);
    }

    @Override
    public String toString() {
        return "HardwareComponentN [componentType="
            + this.componentType
            + ", rotationDirection="
            + this.rotationDirection
            + ", motorSide="
            + this.motorSide
            + "]";
    }
}
