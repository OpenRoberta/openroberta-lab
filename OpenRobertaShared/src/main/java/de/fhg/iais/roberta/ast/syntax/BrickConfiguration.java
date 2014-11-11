package de.fhg.iais.roberta.ast.syntax;

public abstract class BrickConfiguration {

    /**
     * @return Java code used in the code generation to regenerates the same brick configuration
     */
    abstract public String generateRegenerate();

    protected static void appendOptional(StringBuilder sb, String sensorOrActor, String port, HardwareComponent hc) {
        if ( hc != null ) {
            sb.append("    .add").append(sensorOrActor).append("(");
            sb.append(port).append(", ").append(hc.generateRegenerate()).append(")\n");
        }
    }
}
