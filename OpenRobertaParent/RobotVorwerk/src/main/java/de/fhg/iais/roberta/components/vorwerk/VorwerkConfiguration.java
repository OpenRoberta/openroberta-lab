package de.fhg.iais.roberta.components.vorwerk;

import java.util.HashMap;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class VorwerkConfiguration extends Configuration {
    private final String ipAddress;
    private final String portNumber;
    private final String userName;
    private final String password;

    private static Actor leftMotor = new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT);
    private static Actor rightMotor = new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT);

    @SuppressWarnings("serial")
    public VorwerkConfiguration(String ipAddress, String portNumber, String userName, String password) {
        super(new HashMap<IActorPort, Actor>() {
            {
                put(new ActorPort("LEFT", "left"), leftMotor);
                put(new ActorPort("RIGHT", "right"), rightMotor);

            }
        }, new HashMap<ISensorPort, Sensor>() {
            {
                put(new SensorPort("LEFT_ULTRASONIC", "LEFT_ULTRASONIC"), new Sensor(SensorType.ULTRASONIC));
                put(new SensorPort("CENTER_ULTRASONIC", "CENTER_ULTRASONIC"), new Sensor(SensorType.ULTRASONIC));
                put(new SensorPort("RIGHT_ULTRASONIC", "RIGHT_ULTRASONIC"), new Sensor(SensorType.ULTRASONIC));
                put(new SensorPort("LEFT", "left"), new Sensor(SensorType.TOUCH));
                put(new SensorPort("RIGHT", "right"), new Sensor(SensorType.TOUCH));
                put(new SensorPort("X", "X"), new Sensor(SensorType.ACCELEROMETER));
                put(new SensorPort("Y", "Y"), new Sensor(SensorType.ACCELEROMETER));
                put(new SensorPort("Z", "Z"), new Sensor(SensorType.ACCELEROMETER));
                put(new SensorPort("STRENGTH", "strength"), new Sensor(SensorType.ACCELEROMETER));
            }
        }, 0.0, 0.0);

        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.userName = userName;
        this.password = password;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getPortNumber() {
        return this.portNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
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
        return leftMotor;
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
        return rightMotor;

    }

    @Override
    public int getNumberOfRightMotors() {
        return 1;
    }

    @Override
    public int getNumberOfLeftMotors() {
        return 1;
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
        private String ipAddress;
        private String portNumber;
        private String userName;
        private String password;

        public Builder setIpAddres(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder setPortNumber(String portNumber) {
            this.portNumber = portNumber;
            return this;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public Configuration build() {
            return new VorwerkConfiguration(this.ipAddress, this.portNumber, this.userName, this.password);
        }

        @Override
        public String toString() {
            return "Builder [" + this.ipAddress + ", " + this.portNumber + ", " + this.userName + ", " + this.password + "]";
        }
    }

}
