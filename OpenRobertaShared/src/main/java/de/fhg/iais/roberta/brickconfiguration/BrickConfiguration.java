package de.fhg.iais.roberta.brickconfiguration;

/**
 * This is a top class of all classes used to represent the hardware configuration of the brick. Every class that represents more specific brick hardware
 * configuration must extend this class.<br>
 * <br>
 * <b>It is used in the code generation. </b>
 */
public abstract class BrickConfiguration {

    /**
     * @return Java code used in the code generation to regenerates the same brick configuration
     */
    abstract public String generateRegenerate();

    /**
     * @return text which defines the brick configuration
     */
    abstract public String generateText(String name);

    protected static void appendOptional(StringBuilder sb, String sensorOrActor, String port, HardwareComponent hc) {
        if ( hc != null ) {
            sb.append("    .add").append(sensorOrActor).append("(");
            sb.append(port).append(", ").append(hc.generateRegenerate()).append(")\n");
        }
    }
}
