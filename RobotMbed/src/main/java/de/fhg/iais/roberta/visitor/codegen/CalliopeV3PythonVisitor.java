package de.fhg.iais.roberta.visitor.codegen;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitDualSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.MotionKitSingleSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.mbed.calliopeV3.RgbLedsOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.CallibotKeysSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.ICalliopeVisitor;

public class CalliopeV3PythonVisitor extends MbedV2PythonVisitor implements ICalliopeVisitor<Void> {
    private static final Map<String, String> CALLIBOT_TO_PIN_MAP = new HashMap<>();

    static {
        CALLIBOT_TO_PIN_MAP.put("MOTOR_L", "0");
        CALLIBOT_TO_PIN_MAP.put("MOTOR_R", "2");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_LF", "1");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_RF", "4");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_LR", "2");
        CALLIBOT_TO_PIN_MAP.put("RGBLED_RR", "3");
        CALLIBOT_TO_PIN_MAP.put("LED_L", "1");
        CALLIBOT_TO_PIN_MAP.put("LED_R", "2");
        CALLIBOT_TO_PIN_MAP.put("INFRARED_L", "2");
        CALLIBOT_TO_PIN_MAP.put("INFRARED_R", "1");
        CALLIBOT_TO_PIN_MAP.put("ULTRASONIC", "2");
        CALLIBOT_TO_PIN_MAP.put("SERVO_S1", "0x14");
        CALLIBOT_TO_PIN_MAP.put("SERVO_S2", "0x15");
    }
    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param robotConfiguration
     * @param beans
     */
    public CalliopeV3PythonVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, robotConfiguration, "calliopemini", beans);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String port = motorOnAction.getUserDefinedPort();
        String pin;
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponent(port);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotMotor = confComp == null && confCompCallibot != null;
        Assert.isTrue(confComp != null || confCompCallibot != null, "Missing both external motor and Callibot.");

        if ( isCallibotMotor ) {
            pin = getCallibotPin(confCompCallibot, port);
            if ( pin.equals("MOTOR_L") ) {
                pin = "1";
            } else {
                pin = "2";
            }
        } else {
            pin = confComp.getProperty("PIN1");
        }

        switch ( pin ) {
            case "1":
            case "2":
                this.src.add("callibot.set_speed_motor(", pin, ", ");
                motorOnAction.param.getSpeed().accept(this);
                this.src.add(")");
                break;
            case "A":
            case "B":
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.SET_MOTOR), "(\"", pin, "\", ");
                motorOnAction.param.getSpeed().accept(this);
                this.src.add(")");
                break;
            default:
                throw new DbcException("visitMotorOnAction; Invalid motor port: " + pin);

        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        String pin;
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponent(port);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotMotor = confComp == null && confCompCallibot != null;
        Assert.isTrue(confComp != null || confCompCallibot != null, "Missing both external motor and Callibot.");

        if ( isCallibotMotor ) {
            pin = getCallibotPin(confCompCallibot, port);
            if ( pin.equals("MOTOR_L") ) {
                pin = "1";
            } else {
                pin = "2";
            }
        } else {
            pin = confComp.getProperty("PIN1");
        }

        switch ( pin ) {
            case "1":
            case "2":
                this.src.add("callibot.stop_motor(", pin, ")");
                break;
            case "A":
            case "B":
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.SET_MOTOR), "(\"", pin, "\", ");
                if ( motorStopAction.mode == MotorStopMode.NONFLOAT ) {
                    this.src.add("-0.01)");
                } else {
                    this.src.add("0)");
                }
                break;
            default:
                //TODO calliopeV3 other servo motors
        }
        return null;
    }

    @Override
    public Void visitRgbLedsOnHiddenAction(RgbLedsOnHiddenAction rgbLedsOnHiddenAction) {
        String led = rgbLedsOnHiddenAction.slot;
        if ( led.equals("ALL") ) {
            this.src.add("np.fill(");
        } else {
            this.src.add("np[" + rgbLedsOnHiddenAction.slot + "] = (");
        }
        rgbLedsOnHiddenAction.colour.accept(this);
        this.src.add(")");
        nlIndent();
        this.src.add("np.show()");
        return null;
    }

    @Override
    public Void visitRgbLedsOffHiddenAction(RgbLedsOffHiddenAction rgbLedsOffHiddenAction) {
        String led = rgbLedsOffHiddenAction.slot;
        if ( led.equals("ALL") ) {
            this.src.add("np.clear()");
        } else {
            this.src.add("np[" + rgbLedsOffHiddenAction.slot + "] = ((0, 0, 0))");
            nlIndent();
            this.src.add("np.show()");
        }
        nlIndent();
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add("(", colorConst.getRedChannelInt(), ", ", colorConst.getGreenChannelInt(), ", ", colorConst.getBlueChannelInt(), ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        if ( confCompCallibot != null ) {
            this.src.add("callibot.get_ultrasonic_sensor()");
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.ULTRASONIC_GET_DISTANCE), "()");
        }
        return null;
    }

    @Override
    public Void visitCallibotKeysSensor(CallibotKeysSensor callibotKeysSensor) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String port = getCallibotPin(confComp, callibotKeysSensor.getUserDefinedPort());
        if ( port.equals("KEY_FL") ) {
            port = "1";
        } else {
            port = "2";
        }
        this.src.add("callibot.get_bumper_sensor(", port, ")");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String port = getCallibotPin(confComp, infraredSensor.getUserDefinedPort());
        if ( port.equals("INFRARED_L") ) {
            port = "1";
        } else {
            port = "2";
        }
        this.src.add("callibot.get_line_sensor(", port, ")");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        // TODO CalliopeV3 Beate
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String pin1 = getCallibotPin(confComp, rgbLedOnAction.port);
        switch ( pin1 ) {
            case "RGBLED_LF":
                pin1 = "1";
                break;
            case "RGBLED_RF":
                pin1 = "4";
                break;
            case "RGBLED_LR":
                pin1 = "2";
                break;
            case "RGBLED_RR":
                pin1 = "3";
                break;
            default:
        }
        this.src.add("callibot.set_rgb_led(", pin1, ", ");
        rgbLedOnAction.colour.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String pin1 = getCallibotPin(confComp, rgbLedOffAction.port);
        switch ( pin1 ) {
            case "RGBLED_LF":
                pin1 = "1";
                break;
            case "RGBLED_RF":
                pin1 = "4";
                break;
            case "RGBLED_LR":
                pin1 = "2";
                break;
            case "RGBLED_RR":
                pin1 = "3";
                break;
            default:
        }
        this.src.add("callibot.set_rgb_led(", pin1, ", (0, 0, 0))");
        return null;
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        this.src.add("np[1] = (");
        rgbLedOnHiddenAction.colour.accept(this);
        this.src.add(")");
        nlIndent();
        this.src.add("np.show()");
        return null;
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        this.src.add("np.clear()");
        return null;
    }

    @Override
    public Void visitLedAction(LedAction ledAction) {
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        String pin1 = getCallibotPin(confComp, ledAction.port);
        if ( pin1.equals("LED_L") ) {
            pin1 = "1";
        } else {
            pin1 = "2";
        }
        switch ( ledAction.mode ) {
            case "OFF":
                this.src.add("callibot.set_red_led_off(", pin1, ")");
                break;
            case "ON":
                this.src.add("callibot.set_red_led_on(", pin1, ")");
                break;
            default:
                throw new DbcException("Invalid MODE encountered in LedAction: " + ledAction.mode);
        }
        return null;
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        // TODO CalliopeV3 ???
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        this.src.add("set_brightness(");
        displaySetBrightnessAction.brightness.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDisplayGetBrightnessAction(DisplayGetBrightnessAction displayGetBrightnessAction) {
        this.src.add("brightness");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayShowAction(FourDigitDisplayShowAction fourDigitDisplayShowAction) {
        this.src.add("fdd.show(\"");
        fourDigitDisplayShowAction.value.accept(this);
        this.src.add("\",");
        fourDigitDisplayShowAction.position.accept(this);
        this.src.add(",");
        fourDigitDisplayShowAction.colon.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitFourDigitDisplayClearAction(FourDigitDisplayClearAction fourDigitDisplayClearAction) {
        this.src.add("fdd.clear()");
        return null;
    }

    @Override
    public Void visitLedBarSetAction(LedBarSetAction ledBarSetAction) {
        this.src.add("set_led(");
        ledBarSetAction.x.accept(this);
        this.src.add(",");
        ledBarSetAction.brightness.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitBothMotorsOnAction(BothMotorsOnAction bothMotorsOnAction) {
        String portA = bothMotorsOnAction.portA;
        String portB = bothMotorsOnAction.portB;
        ConfigurationComponent confCompA = this.robotConfiguration.optConfigurationComponent(portA);
        ConfigurationComponent confCompB = this.robotConfiguration.optConfigurationComponent(portB);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotMotor = confCompA == null && confCompB == null && confCompCallibot != null;
        if ( isCallibotMotor ) {
            ConfigurationComponent subComponentForCallibot = getBusSubComponentForCallibot(confCompCallibot, portA);
            this.src.add("callibot.set_speed(");
            if ( subComponentForCallibot.componentProperties.get("PORT").equals("MOTOR_L") ) {
                bothMotorsOnAction.speedA.accept(this);
                this.src.add(", ");
                bothMotorsOnAction.speedB.accept(this);
            } else {
                bothMotorsOnAction.speedB.accept(this);
                this.src.add(", ");
                bothMotorsOnAction.speedA.accept(this);
            }
            this.src.add(");");
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.SET_BOTH_MOTORS), "(");
            if ( confCompA.getProperty("PIN1").equals("A") ) {
                bothMotorsOnAction.speedA.accept(this);
            } else {
                bothMotorsOnAction.speedB.accept(this);
            }
            this.src.add(", ");
            if ( confCompB.getProperty("PIN1").equals("A") ) {
                bothMotorsOnAction.speedA.accept(this);
            } else {
                bothMotorsOnAction.speedB.accept(this);
            }
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitBothMotorsStopAction(BothMotorsStopAction bothMotorsStopAction) {
        Set<String> motorPorts = getMotorPins();
        boolean newLine = false;
        if ( motorPorts.contains("0") ) { // Calli:bot motors
            if ( newLine ) {
                nlIndent();
            }
            this.src.add("callibot.stop()");
        } else if ( motorPorts.contains("A") && motorPorts.contains("B") ) { // Internal motors
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.SET_BOTH_MOTORS), "(0, 0)");
            newLine = true;
        } else if ( motorPorts.contains("A") ) {
            if ( newLine ) {
                nlIndent();
            } else {
                newLine = true;
            }
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.SET_MOTOR), "(\"A\", 0)");
        } else if ( motorPorts.contains("B") ) {
            if ( newLine ) {
                nlIndent();
            } else {
                newLine = true;
            }
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(CalliopeMethods.SET_MOTOR), "(\"B\", 0)");
        }
        return null;
    }

    private Set<String> getMotorPins() {
        Set<String> motorPins = new HashSet<>();
        for ( ConfigurationComponent confComp : this.robotConfiguration.getConfigurationComponentsValues() ) {
            String componentType = confComp.componentType;
            if ( componentType.equals("MOTOR") ) {
                motorPins.add(confComp.getProperty("PIN1"));
            } else if ( componentType.equals("CALLIBOT") ) {
                for ( List<ConfigurationComponent> ccList : confComp.getSubComponents().values() ) {
                    for ( ConfigurationComponent cc : ccList ) {
                        if ( cc.componentType.equals("MOTOR") ) {
                            motorPins.add(CALLIBOT_TO_PIN_MAP.get(cc.componentProperties.get("PORT")));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableSet(motorPins);
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        // TODO CalliopeV3 ???
        return null;
    }

    @Override
    public Void visitServoSetAction(ServoSetAction servoSetAction) {
        String port = servoSetAction.getUserDefinedPort();
        ConfigurationComponent confComp = this.robotConfiguration.optConfigurationComponent(port);
        ConfigurationComponent confCompCallibot = this.robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);

        boolean isCallibotServoMotor = confComp == null && confCompCallibot != null;
        Assert.isTrue(confComp != null || confCompCallibot != null, "Missing both external servo motor and Callibot.");
        if ( isCallibotServoMotor ) {
            this.src.add("callibot.servo(");
            String pin = getCallibotPin(confCompCallibot, port);
            pin = pin.replace("SERVO_S", "");

            this.src.add(pin);
            this.src.add(", ");
        } else {
            //TODO calliopeV3 external servo no callibot
        }
        servoSetAction.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotionKitSingleSetAction(MotionKitSingleSetAction motionKitSingleSetAction) {
        // TODO CalliopeV3 ???
        return null;
    }

    @Override
    public Void visitMotionKitDualSetAction(MotionKitDualSetAction motionKitDualSetAction) {
        // TODO CalliopeV3 ???
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        // TODO CalliopeV3, work in progress, Beate
        return null;
    }

    private static String getCallibotPin(ConfigurationComponent confComp, String port) {
        for ( List<ConfigurationComponent> ccList : confComp.getSubComponents().values() ) {
            for ( ConfigurationComponent cc : ccList ) {
                if ( port.equals(cc.userDefinedPortName) ) {
                    return cc.componentProperties.get("PORT");
                }
            }
        }
        return null;
    }

    private ConfigurationComponent getBusSubComponentForCallibot(ConfigurationComponent callibot, String userDefinedName) {
        for ( ConfigurationComponent subComponent : callibot.getSubComponents().get("BUS") ) {
            if ( subComponent.userDefinedPortName.equals(userDefinedName) ) {
                return subComponent;
            }
        }
        throw new DbcException("Invalid structure for Callibot encountered.");
    }
}
