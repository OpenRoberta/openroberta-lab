package de.fhg.iais.roberta.components.arduino;

import java.util.List;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class Bob3Configuration extends Configuration {

    public Bob3Configuration() {
        super(null, null, -1, -1);

    }

    /**
     * This method returns the port on which the left motor is connected. If there is no left motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    @Override
    public IActorPort getLeftMotorPort() {
        return null;
    }

    @Override
    public Actor getLeftMotor() {
        return null;
    }

    /**
     * This method returns the port on which the right motor is connected. If there is no right motor connected throws and {@link DbcException} exception.
     *
     * @return port on which the left motor is connected
     */
    @Override
    public IActorPort getRightMotorPort() {
        return null;
    }

    @Override
    public Actor getRightMotor() {
        return null;

    }

    @Override
    public int getNumberOfRightMotors() {
        return 0;
    }

    @Override
    public int getNumberOfLeftMotors() {
        return 0;
    }

    /**
     * @return text which defines the brick configuration
     */
    @Override
    public String generateText(String name) {
        return "";
    }

    /**
     * This class is a builder of {@link Configuration}
     */
    public static class Builder extends Configuration.Builder<Builder> {

        /**
         * Add actor to the {@link Configuration}
         *
         * @param port on which the component is connected
         * @param actor we want to connect
         * @return
         */
        @Override
        public Builder addActor(IActorPort port, Actor actor) {
            return this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link ActorPort} and {@link Actor})
         *
         * @param actors we want to connect to the brick configuration
         * @return
         */
        @Override
        public Builder addActors(List<Pair<IActorPort, Actor>> actors) {
            return this;
        }

        /**
         * Add sensor to the {@link Configuration}
         *
         * @param port on which the component is connected
         * @param component we want to connect
         * @return
         */

        @Override
        public Builder addSensor(ISensorPort port, Sensor sensor) {
            return this;
        }

        /**
         * Client must provide list of {@link Pair} ({@link SensorPort} and {@link Sensor})
         *
         * @param sensors we want to connect to the brick configuration
         * @return
         */
        @Override
        public Builder addSensors(List<Pair<ISensorPort, Sensor>> sensors) {
            return this;
        }

        /**
         * Set the wheel diameter
         *
         * @param wheelDiameter in cm
         * @return
         */
        @Override
        public Builder setWheelDiameter(double wheelDiameter) {
            return this;
        }

        /**
         * Set the track width
         *
         * @param trackWidth in cm
         * @return
         */
        @Override
        public Builder setTrackWidth(double trackWidth) {
            return this;
        }

        @Override
        public Configuration build() {
            return new Bob3Configuration();
        }

        @Override
        public String toString() {
            return "No configuration provided";
        }

    }

}
