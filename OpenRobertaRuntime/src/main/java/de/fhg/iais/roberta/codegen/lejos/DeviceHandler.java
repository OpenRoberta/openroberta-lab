package de.fhg.iais.roberta.codegen.lejos;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.EncoderMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.brickconfiguration.HardwareComponent;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Sensors;
import de.fhg.iais.roberta.ev3.components.EV3Actor;

/**
 * This class instantiates all sensors (sensor modes) and actors used in blockly program.
 */
public class DeviceHandler {

    private final Map<ActorPort, RegulatedMotor> lejosRegulatedMotors = new TreeMap<>();
    private final Map<ActorPort, EncoderMotor> lejosUnregulatedMotors = new TreeMap<>();

    private EV3GyroSensor gyroSensor = null;

    private SampleProviderBean[] portS1;
    private SampleProviderBean[] portS2;
    private SampleProviderBean[] portS3;
    private SampleProviderBean[] portS4;

    private final Set<EV3Sensors> usedSensors;

    /**
     * Construct new initialization for actors and sensors on the brick. Client must provide
     * brick configuration ({@link EV3BrickConfiguration}) and used sensors in program.
     *
     * @param brickConfiguration for the particular brick
     * @param usedSensors in the blockly program
     */
    public DeviceHandler(EV3BrickConfiguration brickConfiguration, Set<EV3Sensors> usedSensors) {
        this.usedSensors = usedSensors;
        createDevices(brickConfiguration);
    }

    /**
     * Get regulated motor connected on port ({@link ActorPort})
     *
     * @param actorPort on which the motor is connected
     * @return regulate motor
     */
    public RegulatedMotor getRegulatedMotor(ActorPort actorPort) {
        return this.lejosRegulatedMotors.get(actorPort);
    }

    /**
     * Get unregulated motor connected on port ({@link ActorPort})
     *
     * @param actorPort on which the motor is connected
     * @return unregulated motor
     */
    public EncoderMotor getUnregulatedMotor(ActorPort actorPort) {
        return this.lejosUnregulatedMotors.get(actorPort);
    }

    /**
     * @return the gyroSensor
     */
    public EV3GyroSensor getGyroSensor() {
        return this.gyroSensor;
    }

    /**
     * Sample provider of given mode connected on given port. <br>
     * <br>
     * Throws an exception if the sensor mode is not available on the given port.
     *
     * @param sensorPort of the sensors
     * @param sensorMode in which sensor operates
     * @return sample provider
     */
    public SampleProvider getProvider(SensorPort sensorPort, String sensorMode) {
        SampleProviderBean[] sampleProviders = null;
        switch ( sensorPort ) {
            case S1:
                sampleProviders = this.portS1;
                break;
            case S2:
                sampleProviders = this.portS2;
                break;
            case S3:
                sampleProviders = this.portS3;
                break;
            case S4:
                sampleProviders = this.portS4;
                break;

            default:
                throw new DbcException("Invalid port " + sensorPort);
        }
        if ( sampleProviders == null ) {
            throw new DbcException("Sensor mode " + sensorMode + " not avaliable on port " + sensorPort);
        }
        return findProviderByMode(sampleProviders, sensorMode);
    }

    private void createDevices(EV3BrickConfiguration brickConfiguration) {
        initMotor(ActorPort.A, brickConfiguration.getActorOnPort(ActorPort.A), lejos.hardware.port.MotorPort.A);
        initMotor(ActorPort.B, brickConfiguration.getActorOnPort(ActorPort.B), lejos.hardware.port.MotorPort.B);
        initMotor(ActorPort.C, brickConfiguration.getActorOnPort(ActorPort.C), lejos.hardware.port.MotorPort.C);
        initMotor(ActorPort.D, brickConfiguration.getActorOnPort(ActorPort.D), lejos.hardware.port.MotorPort.D);
        this.portS1 = initSensor(SensorPort.S1, brickConfiguration.getSensorOnPort(SensorPort.S1), lejos.hardware.port.SensorPort.S1);
        this.portS2 = initSensor(SensorPort.S2, brickConfiguration.getSensorOnPort(SensorPort.S2), lejos.hardware.port.SensorPort.S2);
        this.portS3 = initSensor(SensorPort.S3, brickConfiguration.getSensorOnPort(SensorPort.S3), lejos.hardware.port.SensorPort.S3);
        this.portS4 = initSensor(SensorPort.S4, brickConfiguration.getSensorOnPort(SensorPort.S4), lejos.hardware.port.SensorPort.S4);
    }

    private void initMotor(ActorPort actorPort, EV3Actor actorType, Port hardwarePort) {
        if ( actorType != null ) {
            if ( actorType.isRegulated() ) {
                switch ( actorType.getComponentType().getTypeName() ) {
                    case "EV3_LARGE_MOTOR":
                        RegulatedMotor ev3LargeRegulatedMotor = new EV3LargeRegulatedMotor(hardwarePort);
                        this.lejosRegulatedMotors.put(actorPort, ev3LargeRegulatedMotor);
                        break;
                    case "EV3_MEDIUM_MOTOR":
                        RegulatedMotor ev3MediumRegulatedMotor = new EV3MediumRegulatedMotor(hardwarePort);
                        this.lejosRegulatedMotors.put(actorPort, ev3MediumRegulatedMotor);
                        break;
                    case "NXTRegulatedMotor":
                        RegulatedMotor nxtRegulatedMotor = new NXTRegulatedMotor(hardwarePort);
                        this.lejosRegulatedMotors.put(actorPort, nxtRegulatedMotor);
                        break;
                    default:
                        throw new DbcException("No such actor type!");
                }
            } else {
                UnregulatedMotor nxtMotor = new UnregulatedMotor(hardwarePort);
                this.lejosUnregulatedMotors.put(actorPort, nxtMotor);
            }
        }
    }

    private boolean isUsed(HardwareComponent actorType) {
        return this.usedSensors.contains(actorType.getComponentType());
    }

    private SampleProviderBean[] initSensor(SensorPort sensorPort, HardwareComponent sensorType, Port hardwarePort) {
        if ( sensorType != null && isUsed(sensorType) ) {
            SampleProviderBean[] t;
            switch ( sensorType.getComponentType().getTypeName() ) {
                case "EV3_COLOR_SENSOR":
                    EV3ColorSensor ev3ColorSensor = new EV3ColorSensor(hardwarePort);
                    return colorSensorSampleProviders(sensorPort, ev3ColorSensor);

                case "EV3_IR_SENSOR":
                    EV3IRSensor ev3IRSensor = new EV3IRSensor(hardwarePort);
                    return infraredSensorSampleProviders(sensorPort, ev3IRSensor);

                case "EV3_GYRO_SENSOR":
                    this.gyroSensor = new EV3GyroSensor(hardwarePort);
                    return gyroSensorSampleProviders(sensorPort, this.gyroSensor);

                case "EV3_TOUCH_SENSOR":
                    EV3TouchSensor ev3TouchSensor = new EV3TouchSensor(hardwarePort);
                    return touchSensorSampleProviders(sensorPort, ev3TouchSensor);

                case "EV3_ULTRASONIC_SENSOR":
                    EV3UltrasonicSensor ev3UltrasonicSensor = new EV3UltrasonicSensor(hardwarePort);
                    long startTime = System.currentTimeMillis();
                    t = ultrasonicSensorSampleProviders(sensorPort, ev3UltrasonicSensor);
                    System.out.println(System.currentTimeMillis() - startTime);
                    return t;

                default:
                    throw new DbcException("No such sensor type!");
            }

        }
        return null;
    }

    private SampleProvider findProviderByMode(SampleProviderBean[] sampleProviders, String sensorMode) {
        for ( SampleProviderBean bean : sampleProviders ) {
            if ( bean.getModeName().equals(sensorMode) ) {
                return bean.getSampleProvider();
            }
        }
        throw new DbcException(sensorMode + " sample provider does not exists!");
    }

    private SampleProviderBean[] colorSensorSampleProviders(SensorPort sensorPort, EV3ColorSensor ev3ColorSensor) {
        SampleProviderBean[] colorSensorSampleProvider = new SampleProviderBean[ColorSensorMode.values().length];
        int i = 0;
        for ( ColorSensorMode sensorMode : ColorSensorMode.values() ) {
            SampleProviderBean providerBean = new SampleProviderBean();
            providerBean.setModeName(sensorMode.name());
            providerBean.setSampleProvider(ev3ColorSensor.getMode(sensorMode.getLejosModeName()));
            colorSensorSampleProvider[i] = providerBean;
            i++;
        }
        return colorSensorSampleProvider;
    }

    private SampleProviderBean[] infraredSensorSampleProviders(SensorPort sensorPort, EV3IRSensor ev3IRSensor) {
        SampleProviderBean[] infraredSensorSampleProviders = new SampleProviderBean[InfraredSensorMode.values().length];
        int i = 0;
        for ( InfraredSensorMode sensorMode : InfraredSensorMode.values() ) {
            SampleProviderBean providerBean = new SampleProviderBean();
            providerBean.setModeName(sensorMode.name());
            providerBean.setSampleProvider(ev3IRSensor.getMode(sensorMode.getLejosModeName()));
            infraredSensorSampleProviders[i] = providerBean;
            i++;
        }
        return infraredSensorSampleProviders;
    }

    private SampleProviderBean[] gyroSensorSampleProviders(SensorPort sensorPort, EV3GyroSensor ev3GyroSensor) {
        SampleProviderBean[] gyroSensorSampleProviders = new SampleProviderBean[GyroSensorMode.values().length - 1];
        int i = 0;
        for ( GyroSensorMode sensorMode : GyroSensorMode.values() ) {
            if ( sensorMode != GyroSensorMode.RESET ) {
                SampleProviderBean providerBean = new SampleProviderBean();
                providerBean.setModeName(sensorMode.name());
                providerBean.setSampleProvider(ev3GyroSensor.getMode(sensorMode.getLejosModeName()));
                gyroSensorSampleProviders[i] = providerBean;
                i++;
            }
        }
        return gyroSensorSampleProviders;
    }

    private SampleProviderBean[] ultrasonicSensorSampleProviders(SensorPort sensorPort, EV3UltrasonicSensor ev3UltrasonicSensor) {
        SampleProviderBean[] ultrasonicSensorSampleProviders = new SampleProviderBean[UltrasonicSensorMode.values().length];
        int i = 0;
        for ( UltrasonicSensorMode sensorMode : UltrasonicSensorMode.values() ) {
            SampleProviderBean providerBean = new SampleProviderBean();
            providerBean.setModeName(sensorMode.name());
            providerBean.setSampleProvider(ev3UltrasonicSensor.getMode(sensorMode.getLejosModeName()));
            ultrasonicSensorSampleProviders[i] = providerBean;
            i++;
        }
        return ultrasonicSensorSampleProviders;
    }

    private SampleProviderBean[] touchSensorSampleProviders(SensorPort sensorPort, EV3TouchSensor ev3TouchSensor) {
        SampleProviderBean[] touchSensorSampleProviders = new SampleProviderBean[1];

        SampleProviderBean providerBean = new SampleProviderBean();
        providerBean.setModeName("Touch");
        providerBean.setSampleProvider(ev3TouchSensor.getMode("Touch"));
        touchSensorSampleProviders[0] = providerBean;

        return touchSensorSampleProviders;
    }
}
