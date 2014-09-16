package de.fhg.iais.roberta.ast.syntax;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.util.Pair;

public class BrickConfigurationN {

    private final HardwareComponentN sensor1;
    private final HardwareComponentN sensor2;
    private final HardwareComponentN sensor3;
    private final HardwareComponentN sensor4;

    private final HardwareComponentN actorA;
    private final HardwareComponentN actorB;
    private final HardwareComponentN actorC;
    private final HardwareComponentN actorD;

    // needed for differential drive pilot
    // TODO change to cm!!!
    // see next TODO
    private final double wheelDiameterCM;
    private final double trackWidthCM;

    private BrickConfigurationN(
        HardwareComponentN sensor1,
        HardwareComponentN sensor2,
        HardwareComponentN sensor3,
        HardwareComponentN sensor4,
        HardwareComponentN actorA,
        HardwareComponentN actorB,
        HardwareComponentN actorC,
        HardwareComponentN actorD,
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

    private static void appendOptional(StringBuilder sb, String sensorOrActor, String port, HardwareComponentN hc) {
        if ( hc != null ) {
            sb.append("    .add").append(sensorOrActor).append("(");
            sb.append(sensorOrActor).append("Port.").append(port).append(", ").append(hc.generateRegenerate()).append(")\n");
        }
    }

    public HardwareComponentN getSensor1() {
        return this.sensor1;
    }

    public HardwareComponentN getSensor2() {
        return this.sensor2;
    }

    public HardwareComponentN getSensor3() {
        return this.sensor3;
    }

    public HardwareComponentN getSensor4() {
        return this.sensor4;
    }

    public HardwareComponentN getActorA() {
        return this.actorA;
    }

    public HardwareComponentN getActorB() {
        return this.actorB;
    }

    public HardwareComponentN getActorC() {
        return this.actorC;
    }

    public HardwareComponentN getActorD() {
        return this.actorD;
    }

    public double getWheelDiameterCM() {
        return this.wheelDiameterCM;
    }

    public double getTrackWidthCM() {
        return this.trackWidthCM;
    }

    // TODO
    public ColorSensorMode getPreSetColorSensorMode(SensorPort sensorPort) {
        return null;
    }

    // TODO
    public UltrasonicSensorMode getPreSetUltrasonicSensorMode(SensorPort sensorPort) {
        return null;
    }

    // TODO
    public InfraredSensorMode getPreSetInfraredSensorMode(SensorPort sensorPort) {
        return null;
    }

    // TODO
    public GyroSensorMode getPreSetGyroSensorMode(SensorPort sensorPort) {
        return null;
    }

    public static class Builder {
        private final Map<ActorPort, HardwareComponentN> actorMapping = new TreeMap<>();
        private final Map<SensorPort, HardwareComponentN> sensorMapping = new TreeMap<>();
        private HardwareComponentN lastVisited = null;

        // TODO taken from lejos, converted to cm, implement method to set these
        private double wheelDiameter;
        private double trackWidth;

        public Builder addActor(ActorPort port, HardwareComponentN component) {
            this.actorMapping.put(port, component);
            return this;
        }

        public Builder addActors(List<Pair<ActorPort, HardwareComponentN>> actors) {
            for ( Pair<ActorPort, HardwareComponentN> pair : actors ) {
                this.actorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return this;
        }

        public Builder addSensor(SensorPort port, HardwareComponentN component) {
            this.sensorMapping.put(port, component);
            return this;
        }

        public Builder addSensors(List<Pair<SensorPort, HardwareComponentN>> sensors) {
            for ( Pair<SensorPort, HardwareComponentN> pair : sensors ) {
                this.sensorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return this;
        }

        public Builder setWheelDiameter(double wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return this;
        }

        public Builder setTrackWidth(double trackWidth) {
            this.trackWidth = trackWidth;
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

        //        public void visiting(String... attributes) {
        //            this.lastVisited = HardwareComponentN.attributesMatch(attributes);
        //        }

        public BrickConfigurationN build() {
            return new BrickConfigurationN(
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
