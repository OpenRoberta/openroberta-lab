package de.fhg.iais.roberta.codegen.lejos;

import java.util.Map;
import java.util.TreeMap;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.NXTMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.EncoderMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.dbc.DbcException;

public class DeviceHandler {

    private final BrickConfiguration brickConfiguration;

    private final Map<ActorPort, RegulatedMotor> lejosRegulatedMotors = new TreeMap<>();
    private final Map<ActorPort, EncoderMotor> lejosUnregulatedMotors = new TreeMap<>();

    private final Map<SensorPort, EV3ColorSensor> lejosColorSensors = new TreeMap<>();
    private final Map<SensorPort, EV3TouchSensor> lejosTouchSensors = new TreeMap<>();
    private final Map<SensorPort, EV3UltrasonicSensor> lejosUltrasonicSensors = new TreeMap<>();
    private final Map<SensorPort, EV3IRSensor> lejosInfraredSensors = new TreeMap<>();
    private final Map<SensorPort, EV3GyroSensor> lejosGyroSensors = new TreeMap<>();

    private final Map<SensorPort, ColorSensorMode> colorModes = new TreeMap<>();
    private final Map<SensorPort, UltrasonicSensorMode> ultrasonicModes = new TreeMap<>();
    private final Map<SensorPort, InfraredSensorMode> infraredModes = new TreeMap<>();
    private final Map<SensorPort, GyroSensorMode> gyroModes = new TreeMap<>();

    private final Map<SensorPort, SampleProvider> lejosSampleProvider = new TreeMap<>();

    public void createMotorObject(ActorPort actorPort) {
        HardwareComponent actorType = null;
        Port hardwarePort = null;
        switch ( actorPort ) {
            case A:
                actorType = this.brickConfiguration.getActorA();
                hardwarePort = MotorPort.A;
                break;
            case B:
                actorType = this.brickConfiguration.getActorB();
                hardwarePort = MotorPort.B;
                break;
            case C:
                actorType = this.brickConfiguration.getActorC();
                hardwarePort = MotorPort.C;
                break;
            case D:
                actorType = this.brickConfiguration.getActorD();
                hardwarePort = MotorPort.D;
                break;
            default:
                throw new DbcException("Invalid actor port!");
        }

        switch ( actorType ) {
            case EV3LargeRegulatedMotor:
                RegulatedMotor ev3LargeRegulatedMotor = new EV3LargeRegulatedMotor(hardwarePort);
                this.lejosRegulatedMotors.put(actorPort, ev3LargeRegulatedMotor);
                break;
            case EV3MediumRegulatedMotor:
                RegulatedMotor ev3MediumRegulatedMotor = new EV3MediumRegulatedMotor(hardwarePort);
                this.lejosRegulatedMotors.put(actorPort, ev3MediumRegulatedMotor);
                break;
            case NXTRegulatedMotor:
                RegulatedMotor nxtRegulatedMotor = new NXTRegulatedMotor(hardwarePort);
                this.lejosRegulatedMotors.put(actorPort, nxtRegulatedMotor);
                break;
            case NXTMotor:
                // EV3Motor can be accessed by NXTMotor as unregulated motor too!!!
                EncoderMotor nxtMotor = new NXTMotor(hardwarePort);
                this.lejosUnregulatedMotors.put(actorPort, nxtMotor);
                break;
            default:
                return; // no motor on this port
        }
    }

    public void createSampleProvider(SensorPort sensorPort) {
        HardwareComponent sensorType = null;
        Port hardwarePort = null;

        switch ( sensorPort ) {
            case S1:
                sensorType = this.brickConfiguration.getSensor1();
                hardwarePort = lejos.hardware.port.SensorPort.S1;
                break;
            case S2:
                sensorType = this.brickConfiguration.getSensor2();
                hardwarePort = lejos.hardware.port.SensorPort.S2;
                break;
            case S3:
                sensorType = this.brickConfiguration.getSensor3();
                hardwarePort = lejos.hardware.port.SensorPort.S3;
                break;
            case S4:
                sensorType = this.brickConfiguration.getSensor4();
                hardwarePort = lejos.hardware.port.SensorPort.S4;
                break;
            default:
                throw new DbcException("Invalid sensor port!");
        }

        switch ( sensorType ) {
            case EV3ColorSensor:
                EV3ColorSensor ev3ColorSensor = new EV3ColorSensor(hardwarePort);
                this.lejosColorSensors.put(sensorPort, ev3ColorSensor);
                setColorSensorMode(sensorPort, this.brickConfiguration.getPreSetColorSensorMode(sensorPort));
                break;
            case EV3IRSensor:
                EV3IRSensor ev3IRSensor = new EV3IRSensor(hardwarePort);
                this.lejosInfraredSensors.put(sensorPort, ev3IRSensor);
                setInfraredMode(sensorPort, this.brickConfiguration.getPreSetInfraredSensorMode(sensorPort));
                break;
            case EV3GyroSensor:
                EV3GyroSensor ev3GyroSensor = new EV3GyroSensor(hardwarePort);
                this.lejosGyroSensors.put(sensorPort, ev3GyroSensor);
                setGyroSensorMode(sensorPort, this.brickConfiguration.getPreSetGyroSensorMode(sensorPort));
                break;
            case EV3TouchSensor:
                EV3TouchSensor ev3TouchSensor = new EV3TouchSensor(hardwarePort);
                this.lejosTouchSensors.put(sensorPort, ev3TouchSensor);
                break;
            case EV3UltrasonicSensor:
                EV3UltrasonicSensor ev3UltrasonicSensor = new EV3UltrasonicSensor(hardwarePort);
                this.lejosUltrasonicSensors.put(sensorPort, ev3UltrasonicSensor);
                setUltrasonicSensorMode(sensorPort, this.brickConfiguration.getPreSetUltrasonicSensorMode(sensorPort));
                break;
            default:
                return; // no sensor on this port
        }
    }

    public DeviceHandler(BrickConfiguration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    public RegulatedMotor getRegulatedMotor(ActorPort actorPort) {
        return this.lejosRegulatedMotors.get(actorPort);
    }

    public EncoderMotor getUnregulatedMotor(ActorPort actorPort) {
        return this.lejosUnregulatedMotors.get(actorPort);
    }

    public EV3ColorSensor getColorSensor(SensorPort sensorPort) {
        return this.lejosColorSensors.get(sensorPort);
    }

    public EV3TouchSensor getTouchSensor(SensorPort sensorPort) {
        return this.lejosTouchSensors.get(sensorPort);
    }

    public EV3UltrasonicSensor getUltrasonicSensor(SensorPort sensorPort) {
        return this.lejosUltrasonicSensors.get(sensorPort);
    }

    public EV3IRSensor getInfraredSensor(SensorPort sensorPort) {
        return this.lejosInfraredSensors.get(sensorPort);
    }

    public EV3GyroSensor getGyroSensor(SensorPort sensorPort) {
        return this.lejosGyroSensors.get(sensorPort);
    }

    public ColorSensorMode getColorSensorModeName(SensorPort sensorPort) {
        return this.colorModes.get(sensorPort);
    }

    public UltrasonicSensorMode getUltrasonicSensorModeName(SensorPort sensorPort) {
        return this.ultrasonicModes.get(sensorPort);
    }

    public InfraredSensorMode getInfraredSensorModeName(SensorPort sensorPort) {
        return this.infraredModes.get(sensorPort);
    }

    public GyroSensorMode getGyroSensorModeName(SensorPort sensorPort) {
        return this.gyroModes.get(sensorPort);
    }

    public SampleProvider getSampleProvider(SensorPort sensorPort) {
        return this.lejosSampleProvider.get(sensorPort);
    }

    public void setColorSensorMode(SensorPort sensorPort, ColorSensorMode sensorMode) {
        SampleProvider sp = getColorSensor(sensorPort).getMode(sensorMode.toString());
        this.lejosSampleProvider.put(sensorPort, sp);
        this.colorModes.put(sensorPort, sensorMode);
    }

    public void setUltrasonicSensorMode(SensorPort sensorPort, UltrasonicSensorMode sensorMode) {
        SampleProvider sp = getUltrasonicSensor(sensorPort).getMode(sensorMode.toString());
        this.lejosSampleProvider.put(sensorPort, sp);
        this.ultrasonicModes.put(sensorPort, sensorMode);
    }

    public void setInfraredMode(SensorPort sensorPort, InfraredSensorMode sensorMode) {
        SampleProvider sp = getInfraredSensor(sensorPort).getMode(sensorMode.toString());
        this.lejosSampleProvider.put(sensorPort, sp);
        this.infraredModes.put(sensorPort, sensorMode);
    }

    public void setGyroSensorMode(SensorPort sensorPort, GyroSensorMode sensorMode) {
        SampleProvider sp = getGyroSensor(sensorPort).getMode(sensorMode.toString());
        this.lejosSampleProvider.put(sensorPort, sp);
        this.gyroModes.put(sensorPort, sensorMode);
    }

}
