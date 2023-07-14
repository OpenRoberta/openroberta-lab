package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.botnroll.BotnrollLedAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IBotnrollVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class BotnrollCppVisitor extends NepoArduinoCppVisitor implements IBotnrollVisitor<Void> {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public BotnrollCppVisitor(List<List<Phrase>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        String toChar = "";
        String varType = showTextAction.msg.getVarType().toString();
        boolean isVar = showTextAction.msg.hasName("VAR");
        String mode = null;
        Expr tt = showTextAction.msg;
        if ( !(tt instanceof StmtExpr) && tt.getKind().hasName("SENSOR_EXPR") ) {
            ExternalSensor sens = (ExternalSensor) ((SensorExpr) tt).sensor;
            if ( sens.getKind().hasName("COLOR_SENSING") ) {
                mode = sens.getMode();
            }
        }

        this.src.add("one.lcd");
        if ( showTextAction.y.toString().equals("NumConst [1]") || showTextAction.y.toString().equals("NumConst [2]") ) {
            showTextAction.y.accept(this);
        } else {
            this.src.add("1");
        }

        this.src.add("(");

        if ( isVar && varType.equals("STRING")
            || mode != null && !mode.equals("RED") && !mode.equals("RGB") && !mode.equals("COLOUR") && !mode.equals("LIGHT") ) {
            toChar = ".c_str()";
        }

        if ( varType.equals("BOOLEAN") ) {
            this.src.add("bnr.boolToString(");
            showTextAction.msg.accept(this);
            this.src.add(")");
        } else {
            showTextAction.msg.accept(this);
        }

        this.src.add(toChar, ");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("bnr.lcdClear();");
        return null;
    }

    @Override
    public Void visitBotnrollLedAction(BotnrollLedAction botnrollLedAction) {
        String mode = transformOnOff2HighLow(botnrollLedAction.mode);
        this.src.add("one.led(", mode, ");");
        return null;

    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        //9 - sound port
        this.src.add("tone(9, ");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        //9 - sound port
        this.src.add("tone(9, ");
        this.src.add(playNoteAction.frequency);
        this.src.add(", ");
        this.src.add(playNoteAction.duration);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");
        final boolean reverse = leftMotor.isReverse() && rightMotor.isReverse();
        String methodName;
        String port = null;
        final boolean isDuration = motorOnAction.param.getDuration() != null;
        final boolean isServo = motorOnAction.getUserDefinedPort().equals("A") || motorOnAction.getUserDefinedPort().toString().equals("D");
        if ( isServo ) {
            methodName = motorOnAction.getUserDefinedPort().equals("A") ? "one.servo1(" : "one.servo2(";
        } else {
            methodName = isDuration ? "bnr.move1mTime(" : "one.move1m(";
            port = motorOnAction.getUserDefinedPort().equals("B") ? "1" : "2";
        }
        this.src.add(methodName);
        if ( !isServo ) {
            this.src.add(port, ", ");
        }
        if ( reverse ) {
            this.src.add("-");
        }
        motorOnAction.param.getSpeed().accept(this);
        if ( isDuration ) {
            this.src.add(", ");
            motorOnAction.param.getDuration().getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort().toString().equals("B") ? "1" : "2";
        if ( motorStopAction.mode == MotorStopMode.FLOAT ) {
            this.src.add("one.stop1m(");

        } else {
            this.src.add("one.brake1m(");
        }
        this.src.add(port, ")");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");

        final boolean isRegulatedDrive = leftMotor.isRegulated() && rightMotor.isRegulated();

        final boolean isDuration = driveAction.param.getDuration() != null;
        final boolean reverse = leftMotor.isReverse() && rightMotor.isReverse();
        final boolean localReverse = driveAction.direction == DriveDirection.BACKWARD;
        String methodName;
        String sign = "";
        if ( isDuration ) {
            methodName = "bnr.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.src.add(methodName);
        if ( !reverse && localReverse || reverse && !localReverse ) {
            sign = "-";
        }
        this.src.add(sign);
        driveAction.param.getSpeed().accept(this);
        this.src.add(", ");
        this.src.add(sign);
        driveAction.param.getSpeed().accept(this);
        if ( isDuration ) {
            this.src.add(", ");
            driveAction.param.getDuration().getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");
        final boolean isRegulatedDrive = leftMotor.isRegulated() && rightMotor.isRegulated();

        final boolean isDuration = curveAction.paramLeft.getDuration() != null && curveAction.paramRight.getDuration() != null;
        final boolean reverse = leftMotor.isReverse() && rightMotor.isReverse();
        final boolean localReverse = curveAction.direction == DriveDirection.BACKWARD;
        String methodName;
        String sign = "";
        if ( isDuration ) {
            methodName = "bnr.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.src.add(methodName);
        if ( !reverse && localReverse || reverse && !localReverse ) {
            sign = "-";
        }
        this.src.add(sign);
        curveAction.paramLeft.getSpeed().accept(this);
        this.src.add(", ");
        this.src.add(sign);
        curveAction.paramRight.getSpeed().accept(this);
        if ( isDuration ) {
            this.src.add(", ");
            curveAction.paramLeft.getDuration().getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");
        boolean isRegulatedDrive = leftMotor.isRegulated() || rightMotor.isRegulated();
        boolean isDuration = turnAction.param.getDuration() != null;
        boolean isReverseLeftMotor = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
        boolean isReverseRightMotor = rightMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
        boolean isTurnRight = turnAction.direction == TurnDirection.RIGHT;

        String methodName;
        String rightMotorSign = "";
        String leftMotorSign = "";

        if ( isTurnRight && !isReverseRightMotor ) {
            rightMotorSign = "-";
        }

        if ( !isTurnRight && !isReverseLeftMotor ) {
            leftMotorSign = "-";
        }

        if ( isDuration ) {
            methodName = "bnr.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.src.add(methodName);
        this.src.add(leftMotorSign);
        turnAction.param.getSpeed().accept(this);
        this.src.add(", ");
        this.src.add(rightMotorSign);
        turnAction.param.getSpeed().accept(this);
        if ( isDuration ) {
            this.src.add(", ");
            turnAction.param.getDuration().getValue().accept(this);
        }
        this.src.add(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.src.add("one.stop();");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("one.readAdc(");
        // ports from 0 to 7
        this.src.add(lightSensor.getUserDefinedPort()); // we could add "-1" so the number of ports would be 1-8 for users
        // botnroll's light sensor returns values from 0 to 1023, so to get a range from 0 to 100 we divide
        // the result by 10.23
        this.src.add(") / 10.23");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        switch ( keysSensor.getUserDefinedPort() ) {
            case SC.LEFT:
                this.src.add("bnr.buttonIsPressed(1)");
                break;
            case SC.RIGHT:
                this.src.add("bnr.buttonIsPressed(3)");
                break;
            case SC.ANY:
                this.src.add("bnr.buttonIsPressed(123)");
                break;
            case SC.ENTER:
                this.src.add("bnr.buttonIsPressed(2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        String port = colorSensor.getUserDefinedPort();
        String colors;
        if ( port.equals("1") ) {
            colors = "colorsLeft, ";
        } else {
            colors = "colorsRight, ";
        }
        switch ( colorSensor.getMode() ) {
            case SC.COLOUR:
                this.src.add("bnr.colorSensorColor(");
                this.src.add(colors);
                this.src.add(port);
                this.src.add(")");
                break;
            case SC.RGB:
                this.src.add("{bnr.colorSensorRGB(", colors, port);
                this.src.add(")[0], bnr.colorSensorRGB(", colors, port);
                this.src.add(")[1], bnr.colorSensorRGB(", colors, port);
                this.src.add(")[2]}");
                break;
            case SC.LIGHT:
                this.src.add("bnr.colorSensorLight(", colors, port, ")");
                break;
            default:
                throw new DbcException("Unknown colour mode: " + colorSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        this.src.add("bnr.readBearing()");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor voltageSensor) {
        this.src.add("one.readBattery()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        switch ( infraredSensor.getMode() ) {
            case SC.OBSTACLE:
                this.src.add("bnr.infraredSensorObstacle(");
                break;
            case SC.PRESENCE:
                this.src.add("bnr.infraredSensorPresence(");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensor.getMode());
        }
        if ( port.equals("BOTH") ) {
            this.src.add("3)");
        } else {
            this.src.add(port, ")");
        }
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String port = ultrasonicSensor.getUserDefinedPort();
        if ( ultrasonicSensor.getUserDefinedPort().equals("3") ) {
            this.src.add("bnr.sonar()");
        } else {
            this.src.add("bnr.ultrasonicDistance(", port, ")");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        decrIndentation();
        mainTask.variables.accept(this);
        nlIndent();
        generateTimerVariables();
        generateUserDefinedMethods();
        nlIndent();
        this.src.add("void setup()");
        nlIndent();
        incrIndentation();
        this.src.add("{");
        nlIndent();
        this.src.add("Wire.begin();");
        nlIndent();
        //set baud rate to 9600 for printing values at serial monitor:
        this.src.add("Serial.begin(9600);   // sets baud rate to 9600bps for printing values at serial monitor.");
        nlIndent();
        // start the communication module:
        this.src.add("one.spiConnect(SSPIN);   // starts the SPI communication module");
        nlIndent();
        this.src.add("brm.i2cConnect(MODULE_ADDRESS);   // starts I2C communication");
        nlIndent();
        this.src.add("brm.setModuleAddress(0x2C);");
        nlIndent();
        // stop motors:
        this.src.add("one.stop();");
        nlIndent();
        this.src.add("bnr.setOne(one);");
        nlIndent();
        this.src.add("bnr.setBrm(brm);");
        generateSensors();
        nlIndent();
        generateUsedVars();
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.src.add("#include <Arduino.h>");
        nlIndent();
        this.src.add("#include <NEPODefs.h>\n");
        // Bot'n Roll ONE A library:
        this.src.add("#include <BnrOneA.h>   // Bot'n Roll ONE A library \n");
        //Bot'n Roll CoSpace Rescue Module library (for the additional sonar kit):
        this.src.add("#include <BnrRescue.h>   // Bot'n Roll CoSpace Rescue Module library \n");
        //additional Roberta functions:
        this.src.add("#include <BnrRoberta.h>    // Open Roberta library \n");

        // declaration of object variable to control the Bot'n Roll ONE A and Rescue:
        this.src.add("BnrOneA one; \n");
        this.src.add("BnrRescue brm; \n");
        this.src.add("BnrRoberta bnr(one, brm);  \n");
        this.src.add("#define SSPIN  2 \n");
        this.src.add("#define MODULE_ADDRESS 0x2C \n");
        this.src.add("byte colorsLeft[3]={0,0,0}; \n");
        this.src.add("byte colorsRight[3]={0,0,0};");

        super.generateProgramPrefix(withWrapping);
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            switch ( usedSensor.getType() ) {
                case SC.COLOR:
                    nlIndent();
                    this.src.add("brm.setRgbStatus(ENABLE);");
                    break;
                case SC.INFRARED:
                    nlIndent();
                    this.src.add("one.obstacleEmitters(ON);");
                    break;
                case SC.ULTRASONIC:
                    nlIndent();
                    this.src.add("brm.setSonarStatus(ENABLE);");
                    break;
                case SC.VOLTAGE:
                case SC.TIMER:
                case SC.LIGHT:
                case SC.COMPASS:
                case SC.SOUND:
                case SC.TOUCH:
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedSensor.getType());
            }
        }
    }

}
