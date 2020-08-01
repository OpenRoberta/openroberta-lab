package de.fhg.iais.roberta.visitor.codegen;

import static de.fhg.iais.roberta.syntax.lang.functions.FunctionNames.SUM;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.EdisonMethods;
import de.fhg.iais.roberta.visitor.hardware.IEdisonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class visits the Blockly blocks for the Edison robot and translates them into EdPy Python2 code (https://github.com/Bdanilko/EdPy)
 * Many methods are not supported (N O P) because the Edison robot does not allow the import of any Python module (f.e. math).
 * Also the edison robot only supports integers and has no suport for nested statements. (f.e. "if (a and b):" with a,b being booleans)
 */
public class EdisonPythonVisitor extends AbstractPythonVisitor implements IEdisonVisitor<Void> {

    /**
     * initialize the Python code generator visitor.
     * The UsedHardwareCollector will provide the Python code generator with the used methods, used sensors/actors and used variables.
     * The used sensors/actors are disregarded in the edison robot, since all sensors/actors are fixed.
     * The used methods are collected by the UsedHardwareCollector and then saved as a Set, so that they can be appended in the suffix
     *
     * @param programPhrases to generate the code from
     */
    public EdisonPythonVisitor(List<List<Phrase<Void>>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
    }

    /**
     * Generates the program prefix, i.e. all preparations that need to be executed before Blockly generated code is reached
     *
     * @param withWrapping if the source code should be wrapped by prefix/suffix
     */
    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("import Ed");
        nlIndent();
        this.sb.append("Ed.EdisonVersion = Ed.V2");
        nlIndent();
        this.sb.append("Ed.DistanceUnits = Ed.CM");
        nlIndent();
        this.sb.append("Ed.Tempo = Ed.TEMPO_SLOW");
        nlIndent();
        this.sb.append("obstacleDetectionOn = False");
        nlIndent();
        this.sb.append("Ed.LineTrackerLed(Ed.ON)");
        nlIndent();
        this.sb.append("Ed.ReadClapSensor()");
        nlIndent(); //zur Sicherheit -- um den Sensor zurückzusetzen
        this.sb.append("Ed.ReadLineState()");
        nlIndent();
        this.sb.append("Ed.TimeWait(250, Ed.TIME_MILLISECONDS)"); //möglicherweise überflüssig
    }

    /**
     * Generates the program suffix, i.e. everything that will be appended to the very end of the source .py file
     * In the suffix are NEPO helper methods for things like sum, round, min, max, etc
     *
     * @param withWrapping if the source code should be wrapped by prefix/suffix
     */
    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent(); //new line for helper methods
        nlIndent();

        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                this
                    .getBean(CodeGeneratorSetupBean.class)
                    .getHelperMethodGenerator()
                    .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
        }
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        throw new DbcException("Not supported!");
    }

    /**
     * Visit the block "controls_repeat_ext"
     */
    @Override
    protected void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(whitespace());
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).accept(this);
        this.sb.append(whitespace() + "in range(");
        expressions.get().get(2).accept(this);
        this.sb.append("):");
    }

    /**
     * Function to get readings from the obstacle detector.
     * visit a {@link InfraredSensor} for the block "robSensors_infrared_getSample"
     *
     * @param infraredSensor to be visited
     */
    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.OBSTACLEDETECTION));
        this.sb.append("(");

        switch ( infraredSensor.getPort() ) {
            case "FRONT":
                this.sb.append("Ed.OBSTACLE_AHEAD");
                break;
            case "LEFT":
                this.sb.append("Ed.OBSTACLE_LEFT");
                break;
            case "RIGHT":
                this.sb.append("Ed.OBSTACLE_RIGHT");
                break;
        }

        this.sb.append(")");
        return null;
    }

    /**
     * Function to receive IR data
     * Needs a helper method
     * visit a {@link InfraredSensor} for the block "robSensors_irseeker_getSample"
     *
     * @param irSeekerSensor
     */
    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEEK));
        switch ( irSeekerSensor.getMode() ) {
            case "RCCODE":
                this.sb.append("(1)");
                break;
            case "EDISON_CODE":
                this.sb.append("(0)");
                break;
        }
        return null;
    }

    /**
     * Function to get the light level from a phototransistor/light sensor
     * The light level reported from Edison is between 0 and 32767, but the return value is in percent.
     * That's why it is divided by 10.
     * The line tracker can also report if the robot is on a line, but for this to work the robot has to be placed on a white surface when the program starts.
     * visit a {@link LightSensor} for the block "robSensors_light_getSample"
     *
     * @param lightSensor
     */
    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        switch ( lightSensor.getPort() ) {
            case "LLIGHT":
                this.sb.append("Ed.ReadLeftLightLevel() / 10");
                break;
            case "RLIGHT":
                this.sb.append("Ed.ReadRightLightLevel() / 10");
                break;
            case "LINETRACKER":
                if ( lightSensor.getMode().equals("LINE") ) {
                    this.sb.append("Ed.ReadLineState() == Ed.LINE_ON_BLACK");
                } else {
                    this.sb.append("Ed.ReadLineTracker() / 10");
                }
                break;
        }
        return null;
    }

    /**
     * Function to detect claps from the sound sensor
     * visit a {@link SoundSensor} for the block "robSensors_sound_getSample"
     *
     * @param soundSensor
     */
    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("Ed.ReadClapSensor() == Ed.CLAP_DETECTED");
        return null;
    }

    /**
     * Function to send data via infrared
     * visit a {@link SendIRAction} for the block "edisonCommunication_ir_sendBlock"
     *
     * @param sendIRAction
     * @return
     */
    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEND));
        this.sb.append("(");
        sendIRAction.getCode().accept(this);
        this.sb.append(")");
        return null;
    }

    /**
     * Function to receive data via infrared
     * visit a {@link SendIRAction} for the block "edisonCommunication_ir_receiveBlock"
     *
     * @param receiveIRAction
     * @return
     */
    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEEK));
        this.sb.append("(0)");
        return null;
    }

    /**
     * Function to drive straight forward/backward with given power % and time/distance
     * visit a {@link DriveAction} for the block "robActions_motorDiff_on" and "robActions_motorDiff_on_for"
     *
     * @param driveAction to be visited
     */
    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        String direction = "Ed.FORWARD";
        switch ( driveAction.getDirection().toString() ) {
            case "FOREWARD":
                break;
            case "BACKWARD":
                direction = "Ed.BACKWARD";
                break;
        }

        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.DIFFDRIVE));
        this.sb.append("(").append(direction).append(", ");
        driveAction.getParam().getSpeed().accept(this);
        this.sb.append(", ");
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().accept(this);
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED");
        }

        this.sb.append(")");

        return null;
    }

    /**
     * Function to perform mathematics on a list (sum/average/min/max/...)
     * Visit a {@link MathOnListFunct} for the block "math_on_list"
     *
     * @param mathOnListFunct to be visited
     */
    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case AVERAGE: // general implementation casts to float, which is not allowed on edison
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(SUM));
                this.sb.append("(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(") / len(");
                mathOnListFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            default:
                super.visitMathOnListFunct(mathOnListFunct);
                break;
        }
        return null;
    }

    /**
     * All Math blocks (Integers and Fractions) are checked here. If the number constant is not an integer an exception will be thrown
     * Only blocks of type "math_integer" should be used with the Edison robot
     *
     * @param numConst
     * @return
     */
    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        if ( isInteger(numConst.getValue()) ) {
            super.visitNumConst(numConst);
        } else {
            throw new IllegalArgumentException("Not an integer");
        }
        return null;
    }

    /**
     * Visits the programs main task, i.e. the Blockly-blocks that are (indirectly) connected to the red "robControls_start"-block.
     * In this method, user-generated Blockly-methods are appended also
     *
     * @param mainTask the main task class to be visited
     * @return null
     */
    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.accept(this); //fill usedGlobalVarInFunctions with values

        nlIndent();
        generateUserDefinedMethods(); //Functions created by the user will be defined before the main function

        return null;
    }

    /**
     * Function to create a list
     * visit a {@link ListCreate} for the block "robLists_create_with"
     *
     * @param listCreate to be visited
     */
    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        int listSize = listCreate.getValue().get().size();

        this.sb.append("Ed.List(").append(listSize).append(", [");
        if ( listSize == 0 ) {
            this.sb.append("]");
        } else {
            for ( int i = 0; i < listSize; i++ ) {
                listCreate.getValue().get().get(i).accept(this);
                if ( i < listSize - 1 ) {
                    this.sb.append(",");
                } else {
                    this.sb.append("]");
                }
            }
        }
        this.sb.append(")");

        return null;
    }

    /**
     * Function to execute code when a key is pressed
     * visit a {@link KeysSensor} for the block "robSensors_key_getSample"
     *
     * @param keysSensor to be visited
     */
    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        switch ( keysSensor.getPort() ) {
            case "REC":
                this.sb.append("Ed.ReadKeypad() == Ed.KEYPAD_ROUND");
                break;
            case "PLAY":
                this.sb.append("Ed.ReadKeypad() == Ed.KEYPAD_TRIANGLE");
                break;
            default:
        }

        return null;
    }

    /**
     * Function to drive a curve forward/backward.
     * This is still very buggy when used together with sensors (e.g. drive a curve until an obstacle is detected).
     * visit a {@link CurveAction} for the block "robActions_motorDiff_curve_for" and "robActions_motorDiff_curve"
     *
     * @param curveAction to visit
     */
    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        String direction;

        //determine the direction
        switch ( curveAction.getDirection().toString() ) {
            case "BACKWARD":
                direction = "Ed.BACKWARD";
                break;
            case "FOREWARD":
            default:
                direction = "Ed.FORWARD";
                break;
        }
        this.sb
            .append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.DIFFCURVE))
            .append("(")
            .append(direction)
            .append(", ");
        curveAction.getParamLeft().getSpeed().accept(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().accept(this);
        if ( curveAction.getParamLeft().getDuration() == null ) {
            this.sb.append(", Ed.DISTANCE_UNLIMITED");
        } else {
            this.sb.append(", ");
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        this.sb.append(")");
        return null;
    }

    /**
     * Function to turn the robot
     * visit a {@link TurnAction} for the block "robActions_motorDiff_turn_for" and "robActions_motorDiff_turn"
     *
     * @param turnAction to be visited
     */
    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.DIFFTURN));
        this.sb.append("(Ed.SPIN_").append(turnAction.getDirection()).append(", ");
        turnAction.getParam().getSpeed().accept(this);
        this.sb.append(", ");
        if ( turnAction.getParam().getDuration() != null ) {
            turnAction.getParam().getDuration().getValue().accept(this);
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED");
        }
        this.sb.append(")");

        return null;
    }

    /**
     * Function to set the motors to a specific power-%
     * visit a {@link MotorOnAction} for the block "robActions_motor_on" and "robActions_motor_on_for"
     *
     * @param motorOnAction
     */
    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.MOTORON));
        switch ( motorOnAction.getUserDefinedPort() ) {
            case "LMOTOR":
                this.sb.append("(0, ");
                motorOnAction.getParam().getSpeed().accept(this);
                break;
            case "RMOTOR":
                this.sb.append("(1, ");
                motorOnAction.getParam().getSpeed().accept(this);
                break;
            default:
                break;
        }
        this.sb.append(", ");

        if ( motorOnAction.getDurationValue() != null ) {
            motorOnAction.getDurationValue().accept(this);
            this.sb.append(")");
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED)");
        }

        return null;
    }

    /**
     * Function to stop individual motors
     * visit a {@link MotorStopAction} for the block "robActions_motor_stop"
     *
     * @param motorStopAction
     */
    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        switch ( motorStopAction.getUserDefinedPort() ) {
            case "B":
            case "LMOTOR":
                this.sb.append("Ed.DriveLeftMotor(Ed.STOP, Ed.SPEED_1, 1)");
                break;
            case "C":
            case "RMOTOR":
                this.sb.append("Ed.DriveRightMotor(Ed.STOP, Ed.SPEED_1, 1)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        if ( binary.getOp() == Binary.Op.DIVIDE ) { // general implementation casts to float, which is not allowed on edison
            binary.getLeft().accept(this);
            this.sb.append(" / ");
            binary.getRight().accept(this);
        } else {
            super.visitBinary(binary);
        }
        return null;
    }

    /**
     * Function to stop driving (stop both motors)
     * visit a {@link MotorDriveStopAction} for the block "robActions_motorDiff_stop"
     *
     * @param stopAction
     */
    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("Ed.Drive(Ed.STOP, Ed.SPEED_1, 1)");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathPowerFunct.getFunctName()));
        this.sb.append("(");
        mathPowerFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathPowerFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    /**
     * Function to change a single number, for the blocks "math_single" and "math_round"
     *
     * @param mathSingleFunct
     * @return
     */
    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case POW10:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER));
                this.sb.append("(10, ");
                mathSingleFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            case ROUND: // TODO should be removed after some time, round block removed from toolbox
                this.sb.append("((");
                mathSingleFunct.getParam().get(0).accept(this);
                this.sb.append("+5)/10)*10");
                break;
            case ROUNDUP: // TODO should be removed after some time, round block removed from toolbox
                this.sb.append("((");
                mathSingleFunct.getParam().get(0).accept(this);
                this.sb.append("/10)+1)*10");
                break;
            case ROUNDDOWN: // TODO should be removed after some time, round block removed from toolbox
                this.sb.append("(");
                mathSingleFunct.getParam().get(0).accept(this);
                this.sb.append("/10)");
                break;
            default:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathSingleFunct.getFunctName()));
                this.sb.append("(");
                mathSingleFunct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
        }
        return null;
    }

    /**
     * Visits the Blockly wait-until-block ("robControls_wait_for")
     *
     * @param waitStmt to be visited
     * @return null
     */
    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("pass");
        decrIndentation();
        return null;
    }

    /**
     * Visits the Blockly wait-block (with user-defined waiting time): "robControls_wait_time"
     *
     * @param waitTimeStmt
     * @return null
     */
    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("Ed.TimeWait(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(", Ed.TIME_MILLISECONDS)");
        return null;
    }

    /**
     * Function to turn on the LEDs
     * visit a {@link LightAction} for the block "robActions_led_on"
     *
     * @param lightAction to be visited
     */
    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        switch ( lightAction.getPort() ) {
            case "RLED":
                this.sb.append("Ed.RightLed(Ed.ON)");
                break;
            case "LLED":
                this.sb.append("Ed.LeftLed(Ed.ON)");
                break;
            default:
                break;
        }

        return null;
    }

    /**
     * Function to turn off the LEDs
     * visit a {@link LightStatusAction} for the block "robActions_led_off"
     *
     * @param lightStatusAction to be visited
     */
    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch ( lightStatusAction.getPort() ) {
            case "1":
            case "RLED":
                this.sb.append("Ed.RightLed(Ed.OFF)");
                break;
            case "2":
            case "LLED":
                this.sb.append("Ed.LeftLed(Ed.OFF)");
                break;
            default:
                this.sb.append("Ed.LeftLed(Ed.OFF) #there is an error in your program");
                this.sb.append("Ed.RightLed(Ed.OFF)");
                break;
        }

        return null;
    }

    /**
     * Function to play a tone
     * visit a {@link ToneAction} for the block "robActions_play_tone"
     *
     * @param toneAction to be visited
     */
    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("Ed.PlayTone(8000000/");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(")");
        nlIndent();
        this.sb.append("Ed.TimeWait(");
        toneAction.getDuration().accept(this);
        this.sb.append(", Ed.TIME_MILLISECONDS)");
        return null;
    }

    /**
     * Function to play a note
     * visit a {@link PlayNoteAction} for the block "mbedActions_play_note"
     *
     * @param playNoteAction
     */
    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("Ed.PlayTone(4000000/"); //eigentlich 8mio aber die zahlen bei tiefen noten werden zu groß für edison
        this.sb.append(Integer.parseInt(playNoteAction.getFrequency().split("\\.")[0]));
        this.sb.append(", " + playNoteAction.getDuration() + ")");
        nlIndent();
        this.sb.append("Ed.TimeWait(" + playNoteAction.getDuration() + ", Ed.TIME_MILLISECONDS)");

        return null;
    }

    /**
     * Function to play a sound file/note file
     * visit a {@link PlayFileAction} for the block "robActions_play_file"
     *
     * @param playFileAction
     */
    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        switch ( playFileAction.getFileName().toLowerCase() ) {
            case "0":
                this.sb.append("___soundfile1 = Ed.TuneString(7,\"c8e8g8z\")"); //positiv
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile1)");
                break;
            case "1":
                //                this.sb.append("___soundfile2 = Ed.TuneString(35, \"c4d8e4d4f4e4d8c8d4a4g4f4e4d4e8c8g2z\")"); //dt. Nationalhymne
                //                nlIndent();
                //                this.sb.append("Ed.PlayTune(___soundfile2)");
                //                break;
                this.sb.append("___soundfile2 = Ed.TuneString(7,\"g8e8c8z\")"); //negativ
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile2)");
                break;
            case "2":
                this.sb.append("___soundfile3 = Ed.TuneString(13,\"g4c4g4c4g4c4z\")"); //warnung
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile3)");
                break;
            case "3":
                this.sb.append("___soundfile4 = Ed.TuneString(21, \"c8e8f4g4o4g4b4g8e4n2z\")"); //MP Tune
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile4)");
                break;
            case "4":
                this.sb.append("___soundfile5 = Ed.TuneString(55, \"c4d4e4f4g2g2a4a4a4a4g2R2a4a4a4a4g2f4f4f4e2e2g4g4g4g4c1z\")"); //Alle meine Entchen
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile5)");
                break;
        }

        nlIndent();
        this.sb.append("while (Ed.ReadMusicEnd() == Ed.MUSIC_NOT_FINISHED):");
        incrIndentation();
        nlIndent();
        this.sb.append("pass");
        decrIndentation();

        return null;
    }

    /**
     * Function to reset the sensors
     * visit a {@link ResetSensor} for the block "edisonSensors_sensor_reset"
     *
     * @param resetSensor
     * @return
     */
    @Override
    public Void visitSensorResetAction(ResetSensor<Void> resetSensor) {
        switch ( resetSensor.getSensor() ) {
            case "OBSTACLEDETECTOR":
                this.sb.append("Ed.ReadObstacleDetection()");
                break;
            case "KEYPAD":
                this.sb.append("Ed.ReadKeypad()");
                break;
            case "SOUND":
                this.sb.append("Ed.ReadClapSensor()");
                break;
            case "RCCODE":
            case "IRCODE":
                this.sb.append("Ed.ReadRemote()");
                nlIndent();
                this.sb.append("Ed.ReadIRData()");
                break;
            default:
                break;
        }

        return null;
    }

    /**
     * visit a {@link GetSampleSensor}
     * Needs to override parent method to add parenthesis.
     *
     * @param sensorGetSample to be visited
     */
    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        this.sb.append("(");
        sensorGetSample.getSensor().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("if ");
        methodIfReturn.getCondition().accept(this);
        if ( !methodIfReturn.getReturnValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(": return ");
            methodIfReturn.getReturnValue().accept(this);
        } else {
            this.sb.append(": return");
        }
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}