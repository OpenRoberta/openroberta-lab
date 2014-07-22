package de.fhg.iais.roberta.conf.transformer;

import java.util.Map;
import java.util.TreeMap;

import de.fhg.iais.roberta.ast.syntax.Phrase.Category;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

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
    private final double wheelDiameter;
    private final double trackWidth;

    public BrickConfiguration(
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

    public String getActorOnPort(ActorPort port) {
        switch ( port ) {
            case A:
                return this.actorA.toString();
            case B:
                return this.actorB.toString();
            case C:
                return this.actorC.toString();
            case D:
                return this.actorD.toString();
            default:
                throw new DbcException("Invalid Actor Port!");
        }
    }

    public String getSensorOnPort(SensorPort port) {
        switch ( port ) {
            case S1:
                return this.sensor1.toString();
            case S2:
                return this.sensor2.toString();
            case S3:
                return this.sensor3.toString();
            case S4:
                return this.sensor4.toString();
            default:
                throw new DbcException("Invalid Sensor Port!");
        }
    }

    /**
     * TODO save sensorModeName as String somewhere in Configuration
     * TODO implement this get Method
     * - Daniel
     * 
     * @param port
     * @return
     */
    public String getSensorModeName(SensorPort port) {
        return "";
    }

    public static class Builder {
        private final Map<ActorPort, HardwareComponent> actorMapping = new TreeMap<>();
        private final Map<SensorPort, HardwareComponent> sensorMapping = new TreeMap<>();
        private HardwareComponent lastVisited = null;

        // TODO taken from lejos, where do we set these??
        private final double wheelDiameter = 2.1f;
        private final double trackWidth = 4.4f;

        public void visitingActorPort(String visiting) {
            Assert.isTrue(this.lastVisited.getCategory() == Category.AKTOR);
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
