package de.fhg.iais.roberta.components;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.fhg.iais.roberta.shared.action.ActorPort;
import de.fhg.iais.roberta.shared.action.MotorSide;
import de.fhg.iais.roberta.shared.sensor.SensorPort;
import de.fhg.iais.roberta.util.Formatter;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class represents model of the hardware configuration of the brick. It is used in the code generation. <br>
 * <br>
 * The {@link Configuration} contains four sensor ports and four actor ports. Client cannot connect more than that.
 */
public class Configuration {
    private final Map<ActorPort, Actor> actors;
    private final Map<SensorPort, Sensor> sensors;

    private final double wheelDiameterCM;
    private final double trackWidthCM;

    /**
     * This constructor sets the each actor connected to given port, each sensor to given port, wheel diameter and track width. <br>
     * Client must provide <b>{@code Map<ActorPort, EV3Actor>}</b> where each {@link Actor} is connected to {@link ActorPort} and
     * {@code Map<SensorPort, EV3Sensor>} where each {@link Sensor} is connected to {@link SensorPort}.
     *
     * @param actors connected to the brick
     * @param sensors connected to the brick
     * @param wheelDiameterCM of the brick wheels
     * @param trackWidthCM of the brick
     */
    public Configuration(Map<ActorPort, Actor> actors, Map<SensorPort, Sensor> sensors, double wheelDiameterCM, double trackWidthCM) {
        super();
        this.actors = actors;
        this.sensors = sensors;
        this.wheelDiameterCM = wheelDiameterCM;
        this.trackWidthCM = trackWidthCM;
    }

    /**
     * Returns the sensor connected on given port.
     *
     * @param sensorPort
     * @return connected sensor on given port
     */
    public Sensor getSensorOnPort(SensorPort sensorPort) {
        Sensor sensor = this.sensors.get(sensorPort);
        return sensor;
    }

    /**
     * Returns the actor connected on given port.<br>
     * <b>Null</b> if there is no actor connected to the port
     *
     * @param actorPort
     * @return connected actor on given port
     */
    public Actor getActorOnPort(ActorPort actorPort) {
        Actor actor = this.actors.get(actorPort);
        return actor;
    }

    /**
     * Get all the actors connected to the brick
     *
     * @return the actors
     */
    public Map<ActorPort, Actor> getActors() {
        return this.actors;
    }

    /**
     * All the sensors connected to the brick
     *
     * @return the sensors
     */
    public Map<SensorPort, Sensor> getSensors() {
        return this.sensors;
    }

    /**
     * @return the wheelDiameterCM
     */
    public double getWheelDiameterCM() {
        return this.wheelDiameterCM;
    }

    /**
     * @return the trackWidthCM
     */
    public double getTrackWidthCM() {
        return this.trackWidthCM;
    }

    /**
     * Check if the motor is regulated. Client must provide valid {@link ActorPort}.
     *
     * @param port on which the motor is connected
     * @return if the motor is regulated
     */
    public boolean isMotorRegulated(ActorPort port) {
        Actor actor = this.actors.get(port);
        Assert.isTrue(actor != null, "No actor connected to the port " + port);
        return actor.isRegulated();
    }

    /**
     * This method returns the port on which the left motor is connected. If there is no left motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    public ActorPort getLeftMotorPort() {
        return getMotorOnSide(MotorSide.LEFT);
    }

    /**
     * This method returns the port on which the right motor is connected. If there is no right motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    public ActorPort getRightMotorPort() {
        return getMotorOnSide(MotorSide.RIGHT);
    }

    /**
     * @return text which defines the brick configuration
     */
    public String generateText(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("robot ev3 ").append(name).append(" {\n");
        if ( this.wheelDiameterCM != 0.0 || this.trackWidthCM != 0.0 ) {
            sb.append("  size {\n");
            sb.append("    wheel diameter ").append(Formatter.d2s(this.wheelDiameterCM)).append(" cm;\n");
            sb.append("    track width    ").append(Formatter.d2s(this.trackWidthCM)).append(" cm;\n");
            sb.append("  }\n");
        }
        if ( this.sensors.size() > 0 ) {
            sb.append("  sensor port {\n");
            for ( SensorPort port : this.sensors.keySet() ) {
                sb.append("    ").append(port.getPortNumber()).append(": ");
                String sensor = this.sensors.get(port).getName().toString();
                sb.append(sensor.toLowerCase()).append(";\n");
            }
            sb.append("  }\n");
        }
        if ( this.actors.size() > 0 ) {
            sb.append("  actor port {\n");
            for ( ActorPort port : this.actors.keySet() ) {
                sb.append("    ").append(port.name()).append(": ");
                Actor actor = this.actors.get(port);
                if ( actor.getName() == ActorType.LARGE ) {
                    sb.append("large");
                } else if ( actor.getName() == ActorType.MEDIUM ) {
                    sb.append("middle");
                } else {
                    throw new RuntimeException("Key.E3");
                }
                sb.append(" motor, ");
                if ( actor.isRegulated() ) {
                    sb.append("regulated");
                } else {
                    sb.append("unregulated");
                }
                sb.append(", ");
                String rotationDirection = actor.getRotationDirection().toString().toLowerCase();
                sb.append(rotationDirection.equals("foreward") ? "forward" : rotationDirection); // TODO: remove this hack; rename FOIREWARD tp FORWARD (be careful!)
                MotorSide motorSide = actor.getMotorSide();
                if ( motorSide != MotorSide.NONE ) {
                    sb.append(", ").append(motorSide.getText());

                }
                sb.append(";\n");
            }
            sb.append("  }\n");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        // trackWidthCM and wheelDiameterCM restricted to 1 fraction digit
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.actors == null) ? 0 : this.actors.hashCode());
        result = prime * result + ((this.sensors == null) ? 0 : this.sensors.hashCode());
        long temp;
        temp = (long) (this.trackWidthCM * 10);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = (long) (this.wheelDiameterCM);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        // trackWidthCM and wheelDiameterCM restricted to 1 fraction digit
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        Configuration other = (Configuration) obj;
        if ( this.actors == null ) {
            if ( other.actors != null ) {
                return false;
            }
        } else if ( !this.actors.equals(other.actors) ) {
            return false;
        }
        if ( this.sensors == null ) {
            if ( other.sensors != null ) {
                return false;
            }
        } else if ( !this.sensors.equals(other.sensors) ) {
            return false;
        }
        if ( (long) (this.trackWidthCM * 10) != (long) (other.trackWidthCM * 10) ) {
            return false;
        }
        if ( (long) (this.wheelDiameterCM * 10) != (long) (other.wheelDiameterCM * 10) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BrickConfiguration [actors="
            + this.actors
            + ", sensors="
            + this.sensors
            + ", wheelDiameterCM="
            + this.wheelDiameterCM
            + ", trackWidthCM="
            + this.trackWidthCM
            + "]";
    }

    private ActorPort getMotorOnSide(MotorSide side) {
        Assert.isTrue(this.actors != null, "There is no actors set to the configuration!");
        for ( Map.Entry<ActorPort, Actor> entry : this.actors.entrySet() ) {
            if ( entry.getValue().getMotorSide() == side ) {
                return entry.getKey();
            }
        }
        //throw new DbcException("No left motor defined!");
        return null;
    }

    /**
     * This class is a builder of {@link Configuration}
     */
    public static class Builder {
        private final Map<ActorPort, Actor> actorMapping = new TreeMap<>();
        private final Map<SensorPort, Sensor> sensorMapping = new TreeMap<>();

        private double wheelDiameter;
        private double trackWidth;

        /**
         * Add actor to the {@link Configuration}
         *
         * @param port on which the component is connected
         * @param actor we want to connect
         * @return
         */
        public Builder addActor(ActorPort port, Actor actor) {
            this.actorMapping.put(port, actor);
            return this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link ActorPort} and {@link Actor})
         *
         * @param actors we want to connect to the brick configuration
         * @return
         */
        public Builder addActors(List<Pair<ActorPort, Actor>> actors) {
            for ( Pair<ActorPort, Actor> pair : actors ) {
                this.actorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return this;
        }

        /**
         * Add sensor to the {@link Configuration}
         *
         * @param port on which the component is connected
         * @param component we want to connect
         * @return
         */
        public Builder addSensor(SensorPort port, Sensor sensor) {
            this.sensorMapping.put(port, sensor);
            return this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link SensorPort} and {@link Sensor})
         *
         * @param sensors we want to connect to the brick configuration
         * @return
         */
        public Builder addSensors(List<Pair<SensorPort, Sensor>> sensors) {
            for ( Pair<SensorPort, Sensor> pair : sensors ) {
                this.sensorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return this;
        }

        /**
         * Set the wheel diameter
         *
         * @param wheelDiameter in cm
         * @return
         */
        public Builder setWheelDiameter(double wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return this;
        }

        /**
         * Set the track width
         *
         * @param trackWidth in cm
         * @return
         */
        public Builder setTrackWidth(double trackWidth) {
            this.trackWidth = trackWidth;
            return this;
        }

        public Configuration build() {
            return new Configuration(this.actorMapping, this.sensorMapping, this.wheelDiameter, this.trackWidth);
        }

        @Override
        public String toString() {
            return "Builder [actorMapping="
                + this.actorMapping
                + ", sensorMapping="
                + this.sensorMapping
                + ", wheelDiameter="
                + this.wheelDiameter
                + ", trackWidth="
                + this.trackWidth
                + "]";
        }
    }
}
