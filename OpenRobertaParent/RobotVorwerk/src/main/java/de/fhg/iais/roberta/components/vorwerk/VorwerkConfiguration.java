package de.fhg.iais.roberta.components.vorwerk;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorSide;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class VorwerkConfiguration extends Configuration {
    private final String ipAddress;
    private final String portNumber;
    private final String userName;
    private final String password;

    private static Actor leftMotor = new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT);
    private static Actor rightMotor = new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT);

    public VorwerkConfiguration(String ipAddress, String portNumber, String userName, String password) {
        super(null, null, 0.0, 0.0);
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
