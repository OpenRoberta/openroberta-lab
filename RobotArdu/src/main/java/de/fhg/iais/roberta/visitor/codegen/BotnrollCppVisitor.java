package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
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
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IBotnrollVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This representation is correct C code for Arduino.</b> <br>
 */
public final class BotnrollCppVisitor extends AbstractCommonArduinoCppVisitor implements IBotnrollVisitor<Void> {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public BotnrollCppVisitor(List<List<Phrase<Void>>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        String toChar = "";
        String varType = showTextAction.getMsg().getVarType().toString();
        boolean isVar = showTextAction.getMsg().getKind().getName().toString().equals("VAR");
        String mode = null;
        Expr<Void> tt = showTextAction.getMsg();
        if ( !(tt instanceof StmtExpr) && tt.getKind().hasName("SENSOR_EXPR") ) {
            ExternalSensor<Void> sens = (ExternalSensor<Void>) ((SensorExpr<Void>) tt).getSens();
            if ( sens.getKind().hasName("COLOR_SENSING") ) {
                mode = sens.getMode();
            }
        }

        this.sb.append("one.lcd");
        if ( showTextAction.getY().toString().equals("NumConst [1]") || showTextAction.getY().toString().equals("NumConst [2]") ) {
            showTextAction.getY().accept(this);
        } else {
            this.sb.append("1");
        }

        this.sb.append("(");

        if ( isVar && varType.equals("STRING")
            || mode != null && !mode.equals("RED") && !mode.equals("RGB") && !mode.equals("COLOUR") && !mode.equals("LIGHT") ) {
            toChar = ".c_str()";
        }

        if ( varType.equals("BOOLEAN") ) {
            this.sb.append("bnr.boolToString(");
            showTextAction.getMsg().accept(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().accept(this);
        }

        this.sb.append(toChar + ");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("bnr.lcdClear();");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("one.led(" + lightAction.getMode().getValues()[0] + ");");
        return null;

    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //9 - sound port
        this.sb.append("tone(9, ");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        //9 - sound port
        this.sb.append("tone(9, ");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append(", ");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");
        final boolean reverse = leftMotor.isReverse() && rightMotor.isReverse();
        String methodName;
        String port = null;
        final boolean isDuration = motorOnAction.getParam().getDuration() != null;
        final boolean isServo = motorOnAction.getUserDefinedPort().equals("A") || motorOnAction.getUserDefinedPort().toString().equals("D");
        if ( isServo ) {
            methodName = motorOnAction.getUserDefinedPort().equals("A") ? "one.servo1(" : "one.servo2(";
        } else {
            methodName = isDuration ? "bnr.move1mTime(" : "one.move1m(";
            port = motorOnAction.getUserDefinedPort().equals("B") ? "1" : "2";
        }
        this.sb.append(methodName);
        if ( !isServo ) {
            this.sb.append(port + ", ");
        }
        if ( reverse ) {
            this.sb.append("-");
        }
        motorOnAction.getParam().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            motorOnAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort().toString().equals("B") ? "1" : "2";
        if ( motorStopAction.getMode() == MotorStopMode.FLOAT ) {
            this.sb.append("one.stop1m(");

        } else {
            this.sb.append("one.brake1m(");
        }
        this.sb.append(port + ")");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");

        final boolean isRegulatedDrive = leftMotor.isRegulated() && rightMotor.isRegulated();

        final boolean isDuration = driveAction.getParam().getDuration() != null;
        final boolean reverse = leftMotor.isReverse() && rightMotor.isReverse();
        final boolean localReverse = driveAction.getDirection() == DriveDirection.BACKWARD;
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
        this.sb.append(methodName);
        if ( !reverse && localReverse || reverse && !localReverse ) {
            sign = "-";
        }
        this.sb.append(sign);
        driveAction.getParam().getSpeed().accept(this);
        this.sb.append(", ");
        this.sb.append(sign);
        driveAction.getParam().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");
        final boolean isRegulatedDrive = leftMotor.isRegulated() && rightMotor.isRegulated();

        final boolean isDuration = curveAction.getParamLeft().getDuration() != null && curveAction.getParamRight().getDuration() != null;
        final boolean reverse = leftMotor.isReverse() && rightMotor.isReverse();
        final boolean localReverse = curveAction.getDirection() == DriveDirection.BACKWARD;
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
        this.sb.append(methodName);
        if ( !reverse && localReverse || reverse && !localReverse ) {
            sign = "-";
        }
        this.sb.append(sign);
        curveAction.getParamLeft().getSpeed().accept(this);
        this.sb.append(", ");
        this.sb.append(sign);
        curveAction.getParamRight().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        ConfigurationComponent leftMotor = this.configuration.getConfigurationComponent("B");
        ConfigurationComponent rightMotor = this.configuration.getConfigurationComponent("A");
        boolean isRegulatedDrive = leftMotor.isRegulated() || rightMotor.isRegulated();
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isReverseLeftMotor = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
        boolean isReverseRightMotor = rightMotor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
        boolean isTurnRight = turnAction.getDirection() == TurnDirection.RIGHT;

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
        this.sb.append(methodName);
        this.sb.append(leftMotorSign);
        turnAction.getParam().getSpeed().accept(this);
        this.sb.append(", ");
        this.sb.append(rightMotorSign);
        turnAction.getParam().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("one.stop();");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("one.readAdc(");
        // ports from 0 to 7
        this.sb.append(lightSensor.getPort()); // we could add "-1" so the number of ports would be 1-8 for users
        // botnroll's light sensor returns values from 0 to 1023, so to get a range from 0 to 100 we divide
        // the result by 10.23
        this.sb.append(") / 10.23");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        switch ( keysSensor.getPort() ) {
            case SC.LEFT:
                this.sb.append("bnr.buttonIsPressed(1)");
                break;
            case SC.RIGHT:
                this.sb.append("bnr.buttonIsPressed(3)");
                break;
            case SC.ANY:
                this.sb.append("bnr.buttonIsPressed(123)");
                break;
            case SC.ENTER:
                this.sb.append("bnr.buttonIsPressed(2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String port = colorSensor.getPort();
        String colors;
        if ( port.equals("1") ) {
            colors = "colorsLeft, ";
        } else {
            colors = "colorsRight, ";
        }
        switch ( colorSensor.getMode() ) {
            case SC.COLOUR:
                this.sb.append("bnr.colorSensorColor(");
                this.sb.append(colors);
                this.sb.append(port);
                this.sb.append(")");
                break;
            case SC.RGB:
                this.sb.append("{bnr.colorSensorRGB(" + colors + port);
                this.sb.append(")[0], bnr.colorSensorRGB(" + colors + port);
                this.sb.append(")[1], bnr.colorSensorRGB(" + colors + port);
                this.sb.append(")[2]}");
                break;
            case SC.LIGHT:
                this.sb.append("bnr.colorSensorLight(" + colors + port);
                this.sb.append(")");
                break;
            default:
                throw new DbcException("Unknown colour mode: " + colorSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("bnr.readBearing()");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        this.sb.append("one.readBattery()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String port = infraredSensor.getPort();
        switch ( infraredSensor.getMode() ) {
            case SC.OBSTACLE:
                this.sb.append("bnr.infraredSensorObstacle(");
                break;
            case SC.PRESENCE:
                this.sb.append("bnr.infraredSensorPresence(");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensor.getMode());
        }
        if ( port.equals("BOTH") ) {
            this.sb.append("3)");
        } else {
            this.sb.append(port + ")");
        }
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = ultrasonicSensor.getPort();
        if ( ultrasonicSensor.getPort().equals("3") ) {
            this.sb.append("bnr.sonar()");
        } else {
            this.sb.append("bnr.ultrasonicDistance(" + port + ")");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().accept(this);
        nlIndent();
        generateTimerVariables();
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("void setup()");
        nlIndent();
        incrIndentation();
        this.sb.append("{");
        nlIndent();
        this.sb.append("Wire.begin();");
        nlIndent();
        //set baud rate to 9600 for printing values at serial monitor:
        this.sb.append("Serial.begin(9600);   // sets baud rate to 9600bps for printing values at serial monitor.");
        nlIndent();
        // start the communication module:
        this.sb.append("one.spiConnect(SSPIN);   // starts the SPI communication module");
        nlIndent();
        this.sb.append("brm.i2cConnect(MODULE_ADDRESS);   // starts I2C communication");
        nlIndent();
        this.sb.append("brm.setModuleAddress(0x2C);");
        nlIndent();
        // stop motors:
        this.sb.append("one.stop();");
        nlIndent();
        this.sb.append("bnr.setOne(one);");
        nlIndent();
        this.sb.append("bnr.setBrm(brm);");
        generateSensors();
        nlIndent();
        generateUsedVars();
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#include <Arduino.h>");
        nlIndent();
        this.sb.append("#include <NEPODefs.h>\n");
        // Bot'n Roll ONE A library:
        this.sb.append("#include <BnrOneA.h>   // Bot'n Roll ONE A library \n");
        //Bot'n Roll CoSpace Rescue Module library (for the additional sonar kit):
        this.sb.append("#include <BnrRescue.h>   // Bot'n Roll CoSpace Rescue Module library \n");
        //additional Roberta functions:
        this.sb.append("#include <BnrRoberta.h>    // Open Roberta library \n");

        // declaration of object variable to control the Bot'n Roll ONE A and Rescue:
        this.sb.append("BnrOneA one; \n");
        this.sb.append("BnrRescue brm; \n");
        this.sb.append("BnrRoberta bnr(one, brm);  \n");
        this.sb.append("#define SSPIN  2 \n");
        this.sb.append("#define MODULE_ADDRESS 0x2C \n");
        this.sb.append("byte colorsLeft[3]={0,0,0}; \n");
        this.sb.append("byte colorsRight[3]={0,0,0};");

        super.generateProgramPrefix(withWrapping);
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            switch ( usedSensor.getType() ) {
                case SC.COLOR:
                    nlIndent();
                    this.sb.append("brm.setRgbStatus(ENABLE);");
                    break;
                case SC.INFRARED:
                    nlIndent();
                    this.sb.append("one.obstacleEmitters(ON);");
                    break;
                case SC.ULTRASONIC:
                    nlIndent();
                    this.sb.append("brm.setSonarStatus(ENABLE);");
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
