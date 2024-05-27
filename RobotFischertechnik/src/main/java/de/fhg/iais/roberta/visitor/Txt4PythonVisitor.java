package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.constants.FischertechnikConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffStopAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ServoOnForAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.light.LedBrightnessAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.colour.ColourCompare;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.CameraBallSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineColourSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineInformationSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineSensor;
import de.fhg.iais.roberta.syntax.sensor.EnvironmentalCalibrate;
import de.fhg.iais.roberta.syntax.sensor.TouchKeySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class Txt4PythonVisitor extends AbstractPythonVisitor implements ITxt4Visitor<Void> {

    private final ConfigurationAst configurationAst;
    private String drive;

    private String IMU = "";
    private String gesture = "";
    private ArrayList<String> ultrasonicSensors = new ArrayList<>();

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public Txt4PythonVisitor(
        List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans, ConfigurationAst configurationAst) {
        super(programPhrases, beans);
        this.configurationAst = configurationAst;
    }

    private ConfigurationComponent getOmniDrive() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(FischertechnikConstants.OMNIDRIVE) ) {
                return component;
            }
        }
        return null;
    }

    private ConfigurationComponent getDifferentialDrive() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(SC.DIFFERENTIALDRIVE) ) {
                return component;
            }
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        UsedHardwareBean usedHardwareBean = this.getBean(UsedHardwareBean.class);
        if ( this.programPhrases
            .stream()
            .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
            .count() > 0 ) {
            generateUserDefinedMethods();
        }
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this.getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.src.add(helperMethodImpls);
        }
        generateNNStuff("python");
        if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.CAMERA) ) {
            cameraInitialized(usedHardwareBean);
        }
        nlIndent();
        StmtList variables = mainTask.variables;
        if ( !variables.get().isEmpty() ) {
            variables.accept(this);
            nlIndent();
        }
        this.src.add("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.src.add("global ", String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.CAMERA) ) {
            nlIndent();
            this.src.add("camera_initialized()");
        }
        if ( usedHardwareBean.isSensorUsed(SC.ULTRASONIC) ) {
            nlIndent();
            this.src.add("while not(");
            for ( int i = 0; i < ultrasonicSensors.size(); i++ ) {
                String name = ultrasonicSensors.get(i);
                this.src.add(name);
                if ( i < ultrasonicSensors.size() - 1 ) {
                    this.src.add(" and ");
                }
            }
            this.src.add("):");
            incrIndentation();
            nlIndent();
            this.src.add("pass");
            decrIndentation();
            nlIndent();
        }

        if ( usedHardwareBean.isActorUsed(FischertechnikConstants.DISPLAYLED) ) {
            nlIndent();
            incrIndentation();
            this.src.add("for color, value in led_colors.items():").nlI();
            incrIndentation();
            this.src.add("if color != \"red\":").nlI();
            this.src.add("display.set_attr(color + \"Led.visible\", str(False).lower())").nlI();
            decrIndentation();
            decrIndentation();
        }
        return null;
    }

    private void getUltrasonicSensors() {

    }

    private void cameraInitialized(UsedHardwareBean usedHardwareBean) {
        nlIndent();
        this.src.add("def camera_initialized():");
        incrIndentation();
        nlIndent();
        this.src.add("while True:");
        incrIndentation();
        nlIndent();
        this.src.add("try:");
        incrIndentation();
        nlIndent();
        if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.BALL) ) {
            this.src.add("ball_detector.detected()").nlI();
        }
        if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.LINE) ) {
            this.src.add("line_detector.detected()").nlI();
        }
        if ( usedHardwareBean.isSensorUsed(SC.COLOUR) ) {
            this.src.add("color_detector.detected()").nlI();
        }
        if ( usedHardwareBean.isSensorUsed(SC.MOTION) ) {
            this.src.add("motion_detector.detected()").nlI();
        }
        this.src.add("break");
        decrIndentation();
        nlIndent();
        this.src.add("except Exception:");
        incrIndentation();
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        decrIndentation();
        decrIndentation();
        nlIndent();

    }

    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.MOTORSTART));
        String motorPort = getPortFromConfig(motorOnAction.port);
        this.src.add("(TXT_M_", motorPort, "_motor, ");
        motorOnAction.power.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String motorPort = getPortFromConfig(motorStopAction.port);
        this.src.add("TXT_M_", motorPort, "_motor.stop()");
        return null;
    }

    @Override
    public Void visitMotorOmniDiffOnAction(MotorOmniDiffOnAction motorOmniDiffOnAction) {
        //replace with forward, backward, left, right, forward left, backward right, forward right, backward left,
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            generateOmniDriveMethod(motorOmniDiffOnAction);
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFERENTIALDRIVE));
            this.src.add("(");
            if ( motorOmniDiffOnAction.direction.equals("BACKWARD") ) {
                this.src.add("-");
            }
            motorOmniDiffOnAction.power.accept(this);
            this.src.add(")");
        }
        return null;
    }

    private void generateOmniDriveMethod(MotorOmniDiffOnAction motorOmniDiffOnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVECURVE));
        this.src.add("(");

        switch ( motorOmniDiffOnAction.direction ) {
            case "BACKWARD": //backward
                this.src.add("-");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "FORWARD": //forward
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "RIGHT": //right
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "LEFT": //left
                this.src.add("-");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "BACKWARDRIGHT": //backward right
                this.src.add("0, -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0");
                break;
            case "FORWARDLEFT": //forward left
                this.src.add("0, ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0");
                break;
            case "BACKWARDLEFT": //backward left
                this.src.add("-");
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0, 0, -");
                motorOmniDiffOnAction.power.accept(this);
                break;
            case "FORWARDRIGHT": //forward right
                motorOmniDiffOnAction.power.accept(this);
                this.src.add(", 0, 0, ");
                motorOmniDiffOnAction.power.accept(this);
                break;
            default:
                break;
        }
        this.src.add(")");
    }

    @Override
    public Void visitMotorOmniDiffOnForAction(MotorOmniDiffOnForAction motorOmniDiffOnForAction) {
        //replace with forward, backward, left, right, forward left, backward right, forward right, backward left,
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            generateOmniDriveForMethod(motorOmniDiffOnForAction);
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFERENTIALDRIVEDISTANCE));
            this.src.add("(");
            motorOmniDiffOnForAction.distance.accept(this);
            this.src.add(", ");
            if ( motorOmniDiffOnForAction.direction.equals("BACKWARD") ) {
                this.src.add("-");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
            } else {
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
            }
            this.src.add(")");
        }


        return null;
    }

    @Override
    public Void visitMotorOmniDiffCurveAction(MotorOmniDiffCurveAction motorOmniDiffCurveAction) {
        String prefix = "";
        String suffix = "";

        if ( motorOmniDiffCurveAction.direction.equals("BACKWARD") ) {
            prefix = "-(";
            suffix = ")";
        }
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVECURVE));
            this.src.add("(" + prefix);
            motorOmniDiffCurveAction.powerLeft.accept(this);
            this.src.add(suffix + ", " + prefix);
            motorOmniDiffCurveAction.powerRight.accept(this);
            this.src.add(suffix + ", " + prefix);
            motorOmniDiffCurveAction.powerLeft.accept(this);
            this.src.add(suffix + ", " + prefix);
            motorOmniDiffCurveAction.powerRight.accept(this);
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFERENTIALDRIVECURVE));
            this.src.add("(" + prefix);
            motorOmniDiffCurveAction.powerLeft.accept(this);
            this.src.add(suffix + ", " + prefix);
            motorOmniDiffCurveAction.powerRight.accept(this);
        }

        this.src.add(suffix + ")");
        return null;
    }

    @Override
    public Void visitMotorOmniDiffCurveForAction(MotorOmniDiffCurveForAction motorOmniDiffCurveForAction) {
        String prefix = "";
        String suffix = "";

        if ( motorOmniDiffCurveForAction.direction.equals("BACKWARD") ) {
            prefix = "-(";
            suffix = ")";
        }
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVECURVEDISTANCE));
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFERENTIALDRIVEDISTANCE));
        }
        this.src.add("(");
        motorOmniDiffCurveForAction.distance.accept(this);
        this.src.add(", " + prefix);
        motorOmniDiffCurveForAction.powerLeft.accept(this);
        this.src.add(suffix + ", " + prefix);
        motorOmniDiffCurveForAction.powerRight.accept(this);
        this.src.add(suffix + ")");
        return null;
    }

    private void generateOmniDriveForMethod(MotorOmniDiffOnForAction motorOmniDiffOnForAction) {
        switch ( motorOmniDiffOnForAction.direction ) {
            case "BACKWARD": //backward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "FORWARD": //forward
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "RIGHT": //right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "LEFT": //left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "BACKWARDRIGHT": //backward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "FORWARDLEFT": //forward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "BACKWARDLEFT": //backward left
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", -");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            case "FORWARDRIGHT": //forward right
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE));
                this.src.add("(");
                motorOmniDiffOnForAction.distance.accept(this);
                this.src.add(", ");
                motorOmniDiffOnForAction.power.accept(this);
                break;
            default:
                break;
        }
        this.src.add(")");
    }

    @Override
    public Void visitMotorOmniDiffStopAction(MotorOmniDiffStopAction motorOmniDiffStopAction) {
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add("front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)");
        } else {
            this.src.add("left_motor.stop_sync(right_motor)");
        }
        return null;
    }

    @Override
    public Void visitMotorOmniDiffTurnAction(MotorOmniDiffTurnAction motorOmniDiffTurnAction) {
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVECURVE));
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFERENTIALDRIVECURVE));
        }
        this.src.add("(");
        if ( motorOmniDiffTurnAction.direction.equals("RIGHT") ) {
            if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
                motorOmniDiffTurnAction.power.accept(this);
                this.src.add(", -(");
                motorOmniDiffTurnAction.power.accept(this);
                this.src.add("), ");
            }
            motorOmniDiffTurnAction.power.accept(this);
            this.src.add(", -(");
            motorOmniDiffTurnAction.power.accept(this);
            this.src.add("))");
        } else {
            this.src.add("-(");
            if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
                motorOmniDiffTurnAction.power.accept(this);
                this.src.add("), ");
                motorOmniDiffTurnAction.power.accept(this);
                this.src.add(", -(");
            }
            motorOmniDiffTurnAction.power.accept(this);
            this.src.add("), ");
            motorOmniDiffTurnAction.power.accept(this);
            this.src.add(")");
        }
        return null;
    }

    @Override
    public Void visitMotorOmniDiffTurnForAction(MotorOmniDiffTurnForAction motorOmniDiffTurnForAction) {
        if ( drive.equals(FischertechnikConstants.OMNIDRIVE) ) {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.OMNIDRIVETURNDEGREES));
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DIFFDRIVETURNDEGREES));
        }
        this.src.add("(");
        if ( motorOmniDiffTurnForAction.direction.equals("LEFT") ) {
            this.src.add("-");
        }
        motorOmniDiffTurnForAction.power.accept(this);
        this.src.add(", ");
        motorOmniDiffTurnForAction.degrees.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.MOTORSTARTFOR));
        String motorPort = getPortFromConfig(motorOnForAction.port);
        this.src.add("(TXT_M_", motorPort, "_motor, ");
        motorOnForAction.power.accept(this);
        this.src.add(", ");
        if ( motorOnForAction.unit.equals("ROTATIONS") ) {
            this.src.add("360 * ");
        }
        motorOnForAction.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        String port = getEncoderSensorport(encoderSensor.getUserDefinedPort());
        if ( encoderSensor.getMode().equals("ROTATION") ) {
            this.src.add("int(TXT_M_", port, "_motor_step_counter.get_count() // STEPS_PER_ROTATION)");
        } else {
            this.src.add("int(TXT_M_", port, "_motor_step_counter.get_count() / STEPS_PER_ROTATION * 360)");
        }
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        String port = getEncoderSensorport(encoderReset.sensorPort);
        this.src.add("TXT_M_", port, "_motor_step_counter.reset()");
        return null;
    }

    @Override
    public Void visitServoOnForAction(ServoOnForAction servoOnForAction) {
        String servoPort = getPortFromConfig(servoOnForAction.port);
        this.src.add("TXT_M_", servoPort, "_servomotor.set_position(int((");
        servoOnForAction.value.accept(this);
        this.src.add(" / 180) * 512))");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String port = getPortFromConfig(ultrasonicSensor.getUserDefinedPort());
        this.src.add("TXT_M_", port, "_ultrasonic_distance_meter.get_distance()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        ConfigurationComponent configurationComponent = this.configurationAst.getConfigurationComponent(infraredSensor.getUserDefinedPort());
        String port;
        if ( infraredSensor.getSlot().equals(SC.LEFT) ) {
            port = configurationComponent.getProperty("PORTL");
        } else {
            port = configurationComponent.getProperty("PORTR");
        }
        this.src.add("(not TXT_M_", port, "_trail_follower.get_state())");
        return null;
    }

    @Override
    public Void visitLedBrightnessAction(LedBrightnessAction ledBrightnessAction) {
        String port = getPortFromConfig(ledBrightnessAction.getUserDefinedPort());
        this.src.add("TXT_M_", port, "_led.set_brightness(int(");
        this.src.add("max(min(int((");
        ledBrightnessAction.brightness.accept(this);
        this.src.add(" / 100) * 512), 512), 0)))");
        return null;
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction displayLedOnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.DISPLAYLEDON), "(");
        displayLedOnAction.colour.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        this.src.add("display.set_attr(current_led + \".active\", str(False).lower())");
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        this.src.add("display.set_attr(\"line\" + str(");
        displayTextAction.row.accept(this);
        this.src.add(") + \".text\", str(");
        displayTextAction.text.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.CLEARDISPLAY));
        this.src.add("()");
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor motionSensor) {
        this.src.add("motion_detector.detected()");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.CAMERAGETCOLOUR));
        this.src.add("()");
        return null;
    }

    @Override
    public Void visitCameraLineSensor(CameraLineSensor cameraLineSensor) {
        this.src.add("line_detector.get_line_count()");
        return null;
    }

    @Override
    public Void visitCameraLineInformationSensor(CameraLineInformationSensor cameraLineInformationSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.LINEINFORMATION), "(");
        cameraLineInformationSensor.lineId.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitCameraLineColourSensor(CameraLineColourSensor cameraLineColourSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.LINEGETCOLOUR), "(");
        cameraLineColourSensor.lineId.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitCameraBallSensor(CameraBallSensor cameraBallSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.BALLINFORMATION), "()");

        return null;
    }

    @Override
    public Void visitColourCompare(ColourCompare colourCompare) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(Txt4Methods.COLOURCOMPARE), "(");
        colourCompare.colour1.accept(this);
        this.src.add(", ");
        colourCompare.colour2.accept(this);
        this.src.add(", ");
        colourCompare.tolerance.accept(this);
        this.src.add(")");
        return null;
    }


    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String port = getPortFromConfig(keysSensor.getUserDefinedPort());
        this.src.add("TXT_M_", port, "_mini_switch.get_state()");
        return null;
    }

    @Override
    public Void visitTouchKeySensor(TouchKeySensor touchKeySensor) {
        this.src.add("display.get_attr(\"button");
        if ( touchKeySensor.getUserDefinedPort().equals(SC.LEFT) ) {
            this.src.add("Left");
        } else {
            this.src.add("Right");
        }
        this.src.add(".pressed\")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.add("_timer", timerReset.sensorPort, " = time.time()");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        String port = getPortFromConfig(lightSensor.getUserDefinedPort());
        this.src.add("((TXT_M_", port, "_color_sensor.get_voltage() / 2000) * 100)");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        int index = getSensorNumber("TXT_IMU");
        this.src.add("TXT_M_I2C_", index, "_combined_sensor_6pin.get_rotation_", gyroSensor.getSlot().toLowerCase(), "()");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        int index = getSensorNumber("TXT_IMU");
        this.src.add("TXT_M_I2C_", index, "_combined_sensor_6pin.get_acceleration_", accelerometerSensor.getSlot().toLowerCase(), "()");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        int index = getSensorNumber("TXT_IMU");
        this.src.add("TXT_M_I2C_", index, "_combined_sensor_6pin.get_magnetic_field_", compassSensor.getSlot().toLowerCase(), "()");
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        int index = getSensorNumber("GESTURE");
        if ( gestureSensor.getMode().equals(SC.COLOUR) ) {
            this.src.add("hex(int(");
        } else if ( gestureSensor.getMode().equals("PROXIMITY") || gestureSensor.getMode().equals(SC.AMBIENTLIGHT) ) {
            this.src.add("int((");
        }
        this.src.add("TXT_M_I2C_", index, "_gesture_sensor.");
        if ( gestureSensor.getMode().equals(SC.COLOUR) ) {
            this.src.add("get_hex()[1:], 16))");
        } else if ( gestureSensor.getMode().equals(SC.AMBIENTLIGHT) ) {
            this.src.add("get_ambient() / 255) * 100)");
        } else if ( gestureSensor.getMode().equals("PROXIMITY") ) {
            this.src.add("get_proximity() / 255) * 100)");
        } else if ( gestureSensor.getMode().equals("GESTURE") ) {
            this.src.add("get_gesture()");
        } else if ( gestureSensor.getMode().equals(SC.RGB) ) {
            this.src.add("get_rgb()");
        }
        return null;
    }

    @Override
    public Void visitEnvironmentalSensor(EnvironmentalSensor environmentalSensor) {
        int index = getSensorNumber(SC.ENVIRONMENTAL);
        this.src.add("TXT_M_I2C_", index, "_environment_sensor.");
        if ( environmentalSensor.getMode().equals(SC.TEMPERATURE) ) {
            this.src.add("get_temperature()");
        } else if ( environmentalSensor.getMode().equals(SC.HUMIDITY) ) {
            this.src.add("get_humidity()");
        } else if ( environmentalSensor.getMode().equals(SC.PRESSURE) ) {
            this.src.add("get_pressure()");
        } else if ( environmentalSensor.getMode().equals("IAQ") ) {
            this.src.add("get_indoor_air_quality_as_number()");
        } else if ( environmentalSensor.getMode().equals("ACCURACY") ) {
            this.src.add("get_accuracy()");
        } else if ( environmentalSensor.getMode().equals("CALIBRATIONNEED") ) {
            this.src.add("needs_calibration()");
        }
        return null;
    }

    @Override
    public Void visitEnvironmentalCalibrate(EnvironmentalCalibrate environmentalCalibrate) {
        int index = getSensorNumber(SC.ENVIRONMENTAL);
        this.src.add("TXT_M_I2C_", index, "_environment_sensor.calibrate()");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        this.src.add("TXT_M.get_loudspeaker().play(\"", playFileAction.fileName, ".wav\", False)").nlI();
        this.src.add("while TXT_M.get_loudspeaker().is_playing():");
        incrIndentation();
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("((time.time() - _timer", timerSensor.getUserDefinedPort(), ") * 1000)");
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("time.sleep(");
        waitTimeStmt.time.accept(this);
        this.src.add("/1000)");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add(colorConst.getHexIntAsString());
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor rgbColor) {
        this.src.add("int(\"{:02x}{:02x}{:02x}\".format(min(max(");
        rgbColor.R.accept(this);
        this.src.add(", 0), 255), min(max(");
        rgbColor.G.accept(this);
        this.src.add(", 0), 255), min(max(");
        rgbColor.B.accept(this);
        this.src.add(", 0), 255)), 16)");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        UsedHardwareBean usedHardwareBean = this.getBean(UsedHardwareBean.class);
        this.src.add("import fischertechnik.factories as txt_factory").nlI();

        //only motor
        if ( usedHardwareBean.isActorUsed(SC.MOTOR) || usedHardwareBean.isActorUsed(FischertechnikConstants.ENCODERMOTOR) ) {
            this.src.add("from fischertechnik.controller.Motor import Motor").nlI();
        }
        if ( usedHardwareBean.isActorUsed(C.RANDOM) || usedHardwareBean.isActorUsed(C.RANDOM_DOUBLE) ) {
            this.src.add("import random").nlI();
        }
        if ( usedHardwareBean.isActorUsed(SC.DISPLAY) ) {
            this.src.add("from lib.display import display").nlI();
        }
        if ( usedHardwareBean.isSensorUsed(SC.COLOUR) ) {
            this.src.add("from fischertechnik.models.Color import Color").nlI();
        }
        if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.COLOURCOMPARE) ) {
            this.src.add("import colorsys").nlI();
        }

        this.src.add("import math").nlI();
        this.src.add("import time").nlI().nlI();

        this.src.add("txt_factory.init()").nlI();
        this.src.add("txt_factory.init_input_factory()").nlI();
        if ( usedHardwareBean.isActorUsed(SC.LED) ) {
            this.src.add("txt_factory.init_output_factory()").nlI();
        }
        if ( usedHardwareBean.isActorUsed(SC.MOTOR) || usedHardwareBean.isActorUsed(FischertechnikConstants.ENCODERMOTOR) ) {
            this.src.add("txt_factory.init_motor_factory()").nlI();
        }
        if ( usedHardwareBean.isSensorUsed(SC.ENCODER) ) {
            this.src.add("txt_factory.init_counter_factory()").nlI();
        }
        if ( usedHardwareBean.isActorUsed(SC.SERVOMOTOR) ) {
            this.src.add("txt_factory.init_servomotor_factory()").nlI();
        }
        if ( usedHardwareBean.isSensorUsed("I2C") ) {
            this.src.add("txt_factory.init_i2c_factory()").nlI();
        }
        this.src.add("TXT_M = txt_factory.controller_factory.create_graphical_controller()").nlI();

        if ( !usedHardwareBean.getUsedActors().isEmpty() && !usedHardwareBean.getUsedSensors().isEmpty() ) {
            nlIndent();
        }
        initActors(usedHardwareBean);
        initSensors(usedHardwareBean);
        initCamera(usedHardwareBean);
        this.src.add("txt_factory.initialized()").nlI();
        enableSensors();
        generateVariables(usedHardwareBean);
        generateTimerVariables();
    }

    private void generateTimerVariables() {
        this.getBean(UsedHardwareBean.class)
            .getUsedSensors()
            .stream()
            .filter(usedSensor -> usedSensor.getType().equals(SC.TIMER))
            .collect(Collectors.groupingBy(UsedSensor::getPort))
            .keySet()
            .forEach(port -> {
                this.usedGlobalVarInFunctions.add("_timer" + port);
                nlIndent();
                this.src.add("_timer", port, " = time.time()");
            });
    }

    private void initActors(UsedHardwareBean usedHardwareBean) {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(SC.MOTOR) && usedHardwareBean.isActorUsed(SC.MOTOR) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_M", port, "_motor = txt_factory.motor_factory.create_motor(TXT_M, ", port, ")").nlI();
            }
            if ( component.componentType.equals(FischertechnikConstants.ENCODERMOTOR) ) {
                String port = component.getOptProperty("PORT").substring(1);
                if ( usedHardwareBean.isActorUsed(FischertechnikConstants.ENCODERMOTOR) ) {
                    this.src.add("TXT_M_M", port, "_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, ", port, ")").nlI();
                }
                if ( usedHardwareBean.isSensorUsed(SC.ENCODER) ) {
                    try {
                        ConfigurationComponent encoder = component.getSubComponents().get(SC.ENCODER).get(0);
                        String counterPort = encoder.getOptProperty("PORT").substring(1);
                        this.src.add("TXT_M_C", counterPort, "_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, ", counterPort, ")").nlI();
                        if ( usedHardwareBean.isActorUsed(FischertechnikConstants.ENCODERMOTOR) ) {
                            this.src.add("TXT_M_C", counterPort, "_motor_step_counter.set_motor(TXT_M_M", port, "_motor)").nlI();
                        }
                    } catch ( UnsupportedOperationException ignored ) {

                    }
                }
            }
            if ( component.componentType.equals(SC.SERVOMOTOR) && usedHardwareBean.isActorUsed(SC.SERVOMOTOR) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_S", port, "_servomotor = txt_factory.servomotor_factory.create_servomotor(TXT_M, ", port, ")").nlI();
            }
            if ( component.componentType.equals(SC.LED) && usedHardwareBean.isActorUsed(SC.LED) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_O", port, "_led = txt_factory.output_factory.create_led(TXT_M, " + port + ")").nlI();
            }
        }
    }

    private void initSensors(UsedHardwareBean usedHardwareBean) {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(SC.KEY) && usedHardwareBean.isSensorUsed(SC.KEY) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_I", port, "_mini_switch = txt_factory.input_factory.create_mini_switch(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals(SC.ULTRASONIC) && usedHardwareBean.isSensorUsed(SC.ULTRASONIC) ) {
                String port = component.getOptProperty("PORT").substring(1);
                String ultraSonicName = "TXT_M_I" + port + "_ultrasonic_distance_meter";
                ultrasonicSensors.add(ultraSonicName);
                this.src.add(ultraSonicName, " = txt_factory.input_factory.create_ultrasonic_distance_meter(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals(SC.INFRARED) && usedHardwareBean.isSensorUsed(SC.INFRARED) ) {
                String portLeft = component.getOptProperty("PORTL").substring(1);
                String portRight = component.getOptProperty("PORTR").substring(1);
                this.src.add("TXT_M_I", portLeft, "_trail_follower = txt_factory.input_factory.create_trail_follower(TXT_M, ", portLeft, ")").nlI();
                this.src.add("TXT_M_I", portRight, "_trail_follower = txt_factory.input_factory.create_trail_follower(TXT_M, ", portRight, ")").nlI();
            } else if ( component.componentType.equals(SC.LIGHT) && usedHardwareBean.isSensorUsed(SC.LIGHT) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_I", port, "_color_sensor = txt_factory.input_factory.create_color_sensor(TXT_M, ", port, ")").nlI();
            } else if ( component.componentType.equals("I2C") && usedHardwareBean.isSensorUsed("I2C") ) {
                if ( usedHardwareBean.isSensorUsed("IMU") ) {
                    int index = getSensorNumber("TXT_IMU");
                    IMU = "TXT_M_I2C_" + index + "_combined_sensor_6pin";
                    this.src.add(IMU, " = txt_factory.i2c_factory.create_combined_sensor_6pin(TXT_M, ", index, ")").nlI();
                }
                if ( usedHardwareBean.isSensorUsed("GESTURE") ) {
                    int index = getSensorNumber("GESTURE");
                    gesture = "TXT_M_I2C_" + index + "_gesture_sensor";
                    this.src.add(gesture, " = txt_factory.i2c_factory.create_gesture_sensor(TXT_M, ", index, ")").nlI();
                }
                if ( usedHardwareBean.isSensorUsed(SC.ENVIRONMENTAL) ) {
                    int index = getSensorNumber(SC.ENVIRONMENTAL);
                    this.src.add("TXT_M_I2C_", index, "_environment_sensor = txt_factory.i2c_factory.create_environment_sensor(TXT_M, ", index, ")").nlI();
                }
            } else if ( component.componentType.equals(SC.ENCODER) && usedHardwareBean.isSensorUsed(SC.ENCODER) ) {
                String port = component.getOptProperty("PORT").substring(1);
                this.src.add("TXT_M_C", port, "_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, ", port, ")").nlI();
            }
        }
    }

    private void enableSensors() {
        if ( !IMU.isEmpty() ) {
            this.src.add(IMU, ".init_accelerometer(2, 1.5625)").nlI();
            this.src.add(IMU, ".init_magnetometer(25)").nlI();
            this.src.add(IMU, ".init_gyrometer(250, 12.5)").nlI();
        }
        if ( !gesture.isEmpty() ) {
            this.src.add(gesture, ".enable_proximity()").nlI();
            this.src.add(gesture, ".enable_gesture()").nlI();
            this.src.add(gesture, ".enable_light()").nlI();
        }
    }

    private void initCamera(UsedHardwareBean usedHardwareBean) {
        if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.CAMERA) ) {
            ConfigurationComponent camera = getCamera();
            String usbPort = camera.getOptProperty("PORT").substring(3);
            int cameraHeight = 240;
            int cameraWidth = 320;
            String cameraVariable = "TXT_M_USB1_" + usbPort + "_camera";
            this.src.add("txt_factory.init_usb_factory()").nlI();
            this.src.add("txt_factory.init_camera_factory()").nlI();
            this.src.add(cameraVariable + " = txt_factory.usb_factory.create_camera(TXT_M, " + usbPort + ")").nlI();
            this.src.add(cameraVariable + ".set_rotate(False)").nlI();
            this.src.add(cameraVariable + ".set_height(" + cameraHeight + ")").nlI();
            this.src.add(cameraVariable + ".set_width(" + cameraWidth + ")").nlI();
            this.src.add(cameraVariable + ".set_fps(15)").nlI();
            this.src.add(cameraVariable + ".start()").nlI();
            this.src.add("CAMERA_HEIGHT = " + cameraHeight).nlI();
            this.src.add("CAMERA_WIDTH = " + cameraWidth).nlI();
            nlIndent();
            if ( usedHardwareBean.isSensorUsed(SC.MOTION) ) {
                String sensitivity = camera.getOptProperty("MOTION");
                this.src.add("motion_detector = txt_factory.camera_factory.create_motion_detector" +
                    "(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT," +
                    " CAMERA_WIDTH * CAMERA_HEIGHT * (" + sensitivity + " / 100) / 500)").nlI();
                this.src.add(cameraVariable, ".add_detector(motion_detector)").nlI();

            }
            if ( usedHardwareBean.isSensorUsed(SC.COLOUR) ) {
                int percent = Integer.parseInt(camera.getOptProperty("COLOURSIZE"));

                double colourWidth = cameraWidth * ((double) percent / 100);
                double colourHeight = cameraHeight * ((double) percent / 100);
                int startX = (cameraWidth - (int) colourWidth) / 2;
                int startY = (cameraHeight - (int) colourHeight) / 2;
                this.src.add("color_detector = txt_factory.camera_factory.create_color_detector(", startX, ", ", startY, ", ", (int) colourWidth, ", ", (int) colourHeight, ", 1)").nlI();
                this.src.add(cameraVariable, ".add_detector(color_detector)").nlI();
            }
            if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.LINE) ) {
                this.src.add("line_detector = txt_factory.camera_factory.create_line_detector(60, 45, 200, 150, 5, 100, -100, 100, 2)").nlI();
                this.src.add(cameraVariable, ".add_detector(line_detector)").nlI();
            }
            if ( usedHardwareBean.isSensorUsed(FischertechnikConstants.BALL) ) {
                this.src.add("ball_detector = txt_factory.camera_factory.create_ball_detector(0, 0, 320, 240, 10, 100, -100, 100, ");
                this.src.add(hexToRgb(camera.getOptProperty("COLOUR")));
                this.src.add(", 20)").nlI();
                this.src.add(cameraVariable, ".add_detector(ball_detector)").nlI();
            }
        }
    }

    private String hexToRgb(String hexStr) {
        hexStr = hexStr.startsWith("#") ? hexStr.substring(1) : hexStr;

        int hlen = hexStr.length();
        int componentLength = hlen / 3;

        StringBuilder result = new StringBuilder("[");
        for ( int i = 0; i < hlen; i += componentLength ) {
            String hexComponent = hexStr.substring(i, i + componentLength);
            int rgbComponent = Integer.parseInt(hexComponent, 16);
            result.append(rgbComponent);
            if ( i < hlen - componentLength ) {
                result.append(", ");
            }
        }
        result.append("]");

        return result.toString();
    }

    private ConfigurationComponent getCamera() {
        for ( ConfigurationComponent component : this.configurationAst.getConfigurationComponents().values() ) {
            if ( component.componentType.equals(FischertechnikConstants.TXTCAMERA) ) {
                return component;
            }
        }
        return null;
    }

    private void generateVariables(UsedHardwareBean usedHardwareBean) {
        if ( usedHardwareBean.isActorUsed(FischertechnikConstants.OMNIDRIVE) ) {
            ConfigurationComponent omniDrive = getOmniDrive();
            if ( omniDrive != null ) {
                this.src.add("#init omnidrive").nlI();
                this.src.add("front_left_motor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_FL") + "_motor").nlI();
                this.src.add("front_right_motor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_FR") + "_motor").nlI();
                this.src.add("rear_left_motor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_RL") + "_motor").nlI();
                this.src.add("rear_right_motor = ", "TXT_M_" + omniDrive.getOptProperty("MOTOR_RR") + "_motor").nlI();
                this.src.add("WHEEL_DIAMETER = ", omniDrive.getOptProperty("BRICK_WHEEL_DIAMETER")).nlI();
                this.src.add("TRACK_WIDTH = ", omniDrive.getOptProperty("BRICK_TRACK_WIDTH")).nlI();
                this.src.add("WHEEL_BASE = ", omniDrive.getOptProperty("WHEEL_BASE")).nlI();
                this.drive = FischertechnikConstants.OMNIDRIVE;
            }
        } else if ( usedHardwareBean.isActorUsed(SC.DIFFERENTIALDRIVE) ) {
            ConfigurationComponent diffDrive = getDifferentialDrive();
            if ( diffDrive != null ) {
                drive = SC.DIFFERENTIAL_DRIVE;
                this.src.add("#init differentialDrive").nlI();
                this.src.add("left_motor = ", "TXT_M_" + diffDrive.getOptProperty("MOTOR_L") + "_motor").nlI();
                this.src.add("right_motor = ", "TXT_M_" + diffDrive.getOptProperty("MOTOR_R") + "_motor").nlI();
                this.src.add("WHEEL_DIAMETER = ", diffDrive.getOptProperty("BRICK_WHEEL_DIAMETER")).nlI();
                this.src.add("TRACK_WIDTH = ", diffDrive.getOptProperty("BRICK_TRACK_WIDTH")).nlI();
            }
        }
        if ( usedHardwareBean.isSensorUsed(SC.ENCODER) ) {
            this.src.add("STEPS_PER_ROTATION = 128").nlI();
        }
        if ( usedHardwareBean.isActorUsed(FischertechnikConstants.DISPLAYLED) ) {
            this.src.add("current_led = \"redLed\"").nlI();

            this.src.add("led_colors = {\n" +
                "    \"red\": 0xcc0000,\n" +
                "    \"yellow\": 0xffff00,\n" +
                "    \"green\": 0x33cc00,\n" +
                "    \"cyan\": 0x33ffff,\n" +
                "    \"blue\": 0x3366ff,\n" +
                "    \"purple\": 0xcc33cc,\n" +
                "    \"white\": 0xffffff,\n" +
                "    \"black\": 0x000000\n" +
                "}").nlI();
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.src.add("def main():");
        incrIndentation();
        nlIndent();
        this.src.add("try:");
        incrIndentation();
        nlIndent();
        this.src.add("run()");
        decrIndentation();
        nlIndent();
        this.src.add("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.src.add("print(e)");
        decrIndentation();
        decrIndentation();
        nlIndent();
        nlIndent();

        this.src.add("main()");
    }

    private String getPortFromConfig(String name) {
        ConfigurationComponent block = configurationAst.getConfigurationComponent(name);
        return block.getComponentProperties().get("PORT");
    }

    private int getSensorNumber(String sensorType) {
        int index = 0;
        for ( ConfigurationComponent sensor : getI2CSensors() ) {
            index++;
            if ( sensor.componentType.equals(sensorType) ) {
                break;
            }
        }
        Assert.isTrue(index > 0, "Sensor is missing in the configuration");
        return index;
    }

    private List<ConfigurationComponent> getI2CSensors() {
        ConfigurationComponent I2C = getI2C();
        return I2C.getSubComponents().get("BUS");
    }

    private ConfigurationComponent getI2C() {
        return configurationAst.optConfigurationComponentByType("I2C");
    }

    private String getEncoderSensorport(String encoderPort) {
        for ( Map.Entry<String, ConfigurationComponent> entry : configurationAst.getAllConfigurationComponentByType("ENCODERMOTOR").entrySet() ) {
            ConfigurationComponent motor = entry.getValue();
            try {
                ConfigurationComponent encoder = motor.getSubComponents().get("ENCODER").get(0);
                if ( encoder.userDefinedPortName.equals(encoderPort) ) {
                    return encoder.getComponentProperties().get("PORT");
                }
            } catch ( UnsupportedOperationException ignored ) {
            }
        }
        return configurationAst.getConfigurationComponent(encoderPort).getComponentProperties().get("PORT");
    }

}
