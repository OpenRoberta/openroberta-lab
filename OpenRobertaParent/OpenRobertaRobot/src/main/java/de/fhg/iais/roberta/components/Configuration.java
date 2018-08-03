package de.fhg.iais.roberta.components;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IMotorSide;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class represents model of the hardware configuration of a robot (assume we have "left" and "right" motor). It is used in the code generation. <br>
 * <br>
 * The {@link Configuration} contains four sensor ports and four actor ports. Client cannot connect more than that.
 */
public abstract class Configuration {
    protected String robotName;
    protected String subtype;

    protected final Map<IActorPort, Actor> actors;
    protected final Map<ISensorPort, Sensor> sensors;

    protected final double wheelDiameterCM;
    protected final double trackWidthCM;

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
    public Configuration(Map<IActorPort, Actor> actors, Map<ISensorPort, Sensor> sensors, double wheelDiameterCM, double trackWidthCM) {
        super();
        this.robotName = "";
        this.subtype = "";
        this.actors = actors;
        this.sensors = sensors;
        this.wheelDiameterCM = wheelDiameterCM;
        this.trackWidthCM = trackWidthCM;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getRobotName() {
        return this.robotName;
    }

    public void setSubtype(String subtype) { this.subtype = subtype; }

    public String getSubtype() {
        return this.subtype;
    }

    /**
     * Returns the sensor connected on given port.
     *
     * @param sensorPort
     * @return connected sensor on given port
     */
    public Sensor getSensorOnPort(ISensorPort sensorPort) {
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
    public Actor getActorOnPort(IActorPort actorPort) {
        final Actor actor = this.actors.get(actorPort);
        return actor;
    }

    /**
     * Get all the actors connected to the brick
     *
     * @return the actors
     */
    public Map<IActorPort, Actor> getActors() {
        return this.actors;
    }

    /**
     * All the sensors connected to the brick
     *
     * @return the sensors
     */
    public Map<ISensorPort, Sensor> getSensors() {
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
    public boolean isMotorRegulated(IActorPort port) {
        final Actor actor = this.actors.get(port);
        Assert.isTrue(actor != null, "No actor connected to the port " + port);
        return actor.isRegulated();
    }

    /**
     * This method returns the port on which the left motor is connected. If there is no left motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    public IActorPort getLeftMotorPort() {
        throw new DbcException("Implement the method in the robot specific configuration class!");
    }

    /**
     * This method returns the left motor. If there is no left motor returns *null*
     *
     * @return left motor
     */
    public Actor getLeftMotor() {
        throw new DbcException("Implement the method in the robot specific configuration class!");
    }

    /**
     * This method returns the number of right motors connected.
     *
     * @return port on which the left motor is connected
     */
    public int getNumberOfLeftMotors() {
        throw new DbcException("Implement the method in the robot specific configuration class!");
    }

    /**
     * This method returns the right motor. If there is no right motor returns *null*
     *
     * @return right motor
     */
    public Actor getRightMotor() {
        throw new DbcException("Implement the method in the robot specific configuration class!");
    }

    /**
     * This method returns the port on which the right motor is connected. If there is no right motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    public IActorPort getRightMotorPort() {
        throw new DbcException("Implement the method in the robot specific configuration class!");
    }

    /**
     * This method returns the number of right motors connected.
     *
     * @return port on which the left motor is connected
     */
    public int getNumberOfRightMotors() {
        throw new DbcException("Implement the method in the robot specific configuration class!");
    }

    /**
     * @return text which defines the brick configuration
     */
    public String generateText(String name) {
        throw new DbcException("Implement the method in the robot specific configuration class!");
    }

    @Override
    public int hashCode() {
        // trackWidthCM and wheelDiameterCM restricted to 1 fraction digit
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.actors == null ? 0 : this.actors.hashCode());
        result = prime * result + (this.sensors == null ? 0 : this.sensors.hashCode());
        long temp;
        temp = (long) (this.trackWidthCM * 10);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = (long) this.wheelDiameterCM;
        result = prime * result + (int) (temp ^ temp >>> 32);
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
        final Configuration other = (Configuration) obj;
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

    protected IActorPort getMotorPortOnSide(IMotorSide side) {
        Assert.isTrue(this.actors != null, "There is no actors set to the configuration!");
        for ( final Map.Entry<IActorPort, Actor> entry : this.actors.entrySet() ) {
            if ( entry.getValue().getMotorSide() == side ) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * This class is a builder of {@link Configuration}
     */
    public abstract static class Builder<T extends Builder<T>> {
        private final Map<IActorPort, Actor> actorMapping = new TreeMap<>();
        private final Map<ISensorPort, Sensor> sensorMapping = new TreeMap<>();

        private double wheelDiameter;
        private double trackWidth;

        /**
         * Add actor to the {@link Configuration}
         *
         * @param port on which the component is connected
         * @param actor we want to connect
         * @return
         */
        @SuppressWarnings("unchecked")
        public T addActor(IActorPort port, Actor actor) {
            this.actorMapping.put(port, actor);
            return (T) this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link ActorPort} and {@link Actor})
         *
         * @param actors we want to connect to the brick configuration
         * @return
         */
        @SuppressWarnings("unchecked")
        public T addActors(List<Pair<IActorPort, Actor>> actors) {
            for ( final Pair<IActorPort, Actor> pair : actors ) {
                this.actorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return (T) this;
        }

        /**
         * Add sensor to the {@link Configuration}
         *
         * @param port on which the component is connected
         * @param component we want to connect
         * @return
         */
        @SuppressWarnings("unchecked")
        public T addSensor(ISensorPort port, Sensor sensor) {
            this.sensorMapping.put(port, sensor);
            return (T) this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link SensorPort} and {@link Sensor})
         *
         * @param sensors we want to connect to the brick configuration
         * @return
         */
        @SuppressWarnings("unchecked")
        public T addSensors(List<Pair<ISensorPort, Sensor>> sensors) {
            for ( final Pair<ISensorPort, Sensor> pair : sensors ) {
                this.sensorMapping.put(pair.getFirst(), pair.getSecond());
            }
            return (T) this;
        }

        /**
         * Set the wheel diameter
         *
         * @param wheelDiameter in cm
         * @return
         */
        @SuppressWarnings("unchecked")
        public T setWheelDiameter(double wheelDiameter) {
            this.wheelDiameter = wheelDiameter;
            return (T) this;
        }

        /**
         * Set the track width
         *
         * @param trackWidth in cm
         * @return
         */
        @SuppressWarnings("unchecked")
        public T setTrackWidth(double trackWidth) {
            this.trackWidth = trackWidth;
            return (T) this;
        }

        public abstract Configuration build();

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
