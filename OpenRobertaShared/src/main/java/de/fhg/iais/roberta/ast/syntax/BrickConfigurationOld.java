package de.fhg.iais.roberta.ast.syntax;

import java.util.Map;
import java.util.TreeMap;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.dbc.Assert;

public class BrickConfigurationOld {

    private final HardwareComponentOld sensor1;
    private final HardwareComponentOld sensor2;
    private final HardwareComponentOld sensor3;
    private final HardwareComponentOld sensor4;

    private final HardwareComponentOld actorA;
    private final HardwareComponentOld actorB;
    private final HardwareComponentOld actorC;
    private final HardwareComponentOld actorD;

    // needed for differential drive pilot
    // see next TODO
    private final double wheelDiameterCM;
    private final double trackWidthCM;

    private BrickConfigurationOld(
        HardwareComponentOld sensor1,
        HardwareComponentOld sensor2,
        HardwareComponentOld sensor3,
        HardwareComponentOld sensor4,
        HardwareComponentOld actorA,
        HardwareComponentOld actorB,
        HardwareComponentOld actorC,
        HardwareComponentOld actorD,
        double wheelDiameterCM,
        double trackWidthCM) {
        super();
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.actorA = actorA;
        this.actorB = actorB;
        this.actorC = actorC;
        this.actorD = actorD;
        this.wheelDiameterCM = wheelDiameterCM;
        this.trackWidthCM = trackWidthCM;
    }

    public String generateRegenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append("private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n");
        appendOptional(sb, "Actor", "A", this.actorA);
        appendOptional(sb, "Actor", "B", this.actorB);
        appendOptional(sb, "Actor", "C", this.actorC);
        appendOptional(sb, "Actor", "D", this.actorD);
        appendOptional(sb, "Sensor", "S1", this.sensor1);
        appendOptional(sb, "Sensor", "S2", this.sensor2);
        appendOptional(sb, "Sensor", "S3", this.sensor3);
        appendOptional(sb, "Sensor", "S4", this.sensor4);
        sb.append("    .build();");
        return sb.toString();
    }

    private static void appendOptional(StringBuilder sb, String sensorOrActor, String port, HardwareComponentOld hc) {
        if ( hc != null ) {
            sb.append("    .add").append(sensorOrActor).append("(");
            sb.append(sensorOrActor).append("Port.").append(port).append(", ");
            sb.append("HardwareComponent.").append(hc.name()).append(")\n");
        }
    }

    public HardwareComponentOld getSensor1() {
        return this.sensor1;
    }

    public HardwareComponentOld getSensor2() {
        return this.sensor2;
    }

    public HardwareComponentOld getSensor3() {
        return this.sensor3;
    }

    public HardwareComponentOld getSensor4() {
        return this.sensor4;
    }

    public HardwareComponentOld getActorA() {
        return this.actorA;
    }

    public HardwareComponentOld getActorB() {
        return this.actorB;
    }

    public HardwareComponentOld getActorC() {
        return this.actorC;
    }

    public HardwareComponentOld getActorD() {
        return this.actorD;
    }

    public double getWheelDiameterCM() {
        return this.wheelDiameterCM;
    }

    public double getTrackWidthCM() {
        return this.trackWidthCM;
    }

    public static class Builder {
        private final Map<ActorPort, HardwareComponentOld> actorMapping = new TreeMap<>();
        private final Map<SensorPort, HardwareComponentOld> sensorMapping = new TreeMap<>();
        private HardwareComponentOld lastVisited = null;

        // TODO taken from lejos, converted to cm, implement method to set these
        private final double wheelDiameter = 5.6f;
        private final double trackWidth = 11.2f;

        public Builder addActor(ActorPort port, HardwareComponentOld component) {
            this.actorMapping.put(port, component);
            return this;
        }

        public Builder addSensor(SensorPort port, HardwareComponentOld component) {
            this.sensorMapping.put(port, component);
            return this;
        }

        public void visitingActorPort(String visiting) {
            Assert.isTrue(this.lastVisited.getCategory() == Category.ACTOR);
            ActorPort port = ActorPort.get(visiting);
            this.actorMapping.put(port, this.lastVisited);
            this.lastVisited = null;
        }

        public void visitingSensorPort(String visiting) {
            Assert.isTrue(this.lastVisited.getCategory() == Category.SENSOR);
            SensorPort port = SensorPort.get(visiting);
            this.sensorMapping.put(port, this.lastVisited);
            this.lastVisited = null;
        }

        public void visiting(String... attributes) {
            this.lastVisited = HardwareComponentOld.attributesMatch(attributes);
        }

        public BrickConfigurationOld build() {
            return new BrickConfigurationOld(
                this.sensorMapping.get(SensorPort.S1),
                this.sensorMapping.get(SensorPort.S2),
                this.sensorMapping.get(SensorPort.S3),
                this.sensorMapping.get(SensorPort.S4),
                this.actorMapping.get(ActorPort.A),
                this.actorMapping.get(ActorPort.B),
                this.actorMapping.get(ActorPort.C),
                this.actorMapping.get(ActorPort.D),
                this.wheelDiameter,
                this.trackWidth);
        }

        @Override
        public String toString() {
            return "BrickConfiguration.Builder [actors=" + this.actorMapping + ", sensors=" + this.sensorMapping + "]";
        }
    }

    @Override
    public String toString() {
        return "BrickConfiguration [sensor1="
            + this.sensor1
            + ", sensor2="
            + this.sensor2
            + ", sensor3="
            + this.sensor3
            + ", sensor4="
            + this.sensor4
            + ", actorA="
            + this.actorA
            + ", actorB="
            + this.actorB
            + ", actorC="
            + this.actorC
            + ", actorD="
            + this.actorD
            + ", wheelDiameter="
            + this.wheelDiameterCM
            + ", trackWidth="
            + this.trackWidthCM
            + "]";
    }

}
