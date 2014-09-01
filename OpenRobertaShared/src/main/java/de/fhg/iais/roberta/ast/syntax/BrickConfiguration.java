package de.fhg.iais.roberta.ast.syntax;

import java.util.Map;
import java.util.TreeMap;

import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.dbc.Assert;

public class BrickConfiguration {
    private final HardwareComponent sensor1;
    private final HardwareComponent sensor2;
    private final HardwareComponent sensor3;
    private final HardwareComponent sensor4;

    private final HardwareComponent actorA;
    private final HardwareComponent actorB;
    private final HardwareComponent actorC;
    private final HardwareComponent actorD;

    // needed for differential drive pilot
    // TODO change to cm!!!
    // see next TODO
    private final double wheelDiameter;
    private final double trackWidth;

    private BrickConfiguration(
        HardwareComponent sensor1,
        HardwareComponent sensor2,
        HardwareComponent sensor3,
        HardwareComponent sensor4,
        HardwareComponent actorA,
        HardwareComponent actorB,
        HardwareComponent actorC,
        HardwareComponent actorD,
        double wheelDiameter,
        double trackWidth) {
        super();
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.actorA = actorA;
        this.actorB = actorB;
        this.actorC = actorC;
        this.actorD = actorD;
        this.wheelDiameter = wheelDiameter;
        this.trackWidth = trackWidth;
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

    private static void appendOptional(StringBuilder sb, String sensorOrActor, String port, HardwareComponent hc) {
        if ( hc != null ) {
            sb.append("    .add").append(sensorOrActor).append("(");
            sb.append(sensorOrActor).append("Port.").append(port).append(", ");
            sb.append("HardwareComponent.").append(hc.name()).append(")\n");
        }
    }

    public HardwareComponent getSensor1() {
        return this.sensor1;
    }

    public HardwareComponent getSensor2() {
        return this.sensor2;
    }

    public HardwareComponent getSensor3() {
        return this.sensor3;
    }

    public HardwareComponent getSensor4() {
        return this.sensor4;
    }

    public HardwareComponent getActorA() {
        return this.actorA;
    }

    public HardwareComponent getActorB() {
        return this.actorB;
    }

    public HardwareComponent getActorC() {
        return this.actorC;
    }

    public HardwareComponent getActorD() {
        return this.actorD;
    }

    public double getWheelDiameter() {
        return this.wheelDiameter;
    }

    public double getTrackWidth() {
        return this.trackWidth;
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
        private final Map<ActorPort, HardwareComponent> actorMapping = new TreeMap<>();
        private final Map<SensorPort, HardwareComponent> sensorMapping = new TreeMap<>();
        private HardwareComponent lastVisited = null;

        // TODO taken from lejos, converted to cm, implement method to set these
        private final double wheelDiameter = 5.6f;
        private final double trackWidth = 11.2f;

        public Builder addActor(ActorPort port, HardwareComponent component) {
            this.actorMapping.put(port, component);
            return this;
        }

        public Builder addSensor(SensorPort port, HardwareComponent component) {
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
            this.lastVisited = HardwareComponent.attributesMatch(attributes);
        }

        public BrickConfiguration build() {
            return new BrickConfiguration(
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
            return "BrickConfiguration [actors=" + this.actorMapping + ", sensors=" + this.sensorMapping + "]";
        }
    }
}
