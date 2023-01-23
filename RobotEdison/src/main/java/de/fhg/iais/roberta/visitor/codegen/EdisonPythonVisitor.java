package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import static de.fhg.iais.roberta.util.syntax.FunctionNames.SUM;
import de.fhg.iais.roberta.visitor.EdisonMethods;
import de.fhg.iais.roberta.visitor.IEdisonVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class visits the Blockly blocks for the Edison robot and translates them into EdPy Python2 code (https://github.com/Bdanilko/EdPy) Many methods are not
 * supported (N O P) because the Edison robot does not allow the import of any Python module (f.e. math), also the edison robot only supports integers and has
 * no support for nested statements. (e.g. "if (a and b):" with a,b being booleans)
 */
public class EdisonPythonVisitor extends AbstractPythonVisitor implements IEdisonVisitor<Void> {

    /**
     * initialize the Python code generator visitor. The UsedHardwareCollector will provide the Python code generator with the used methods, used sensors/actors
     * and used variables. The used sensors/actors are disregarded in the edison robot, since all sensors/actors are fixed. The used methods are collected by
     * the UsedHardwareCollector and then saved as a Set, so that they can be appended in the suffix
     *
     * @param programPhrases to generate the code from
     */
    public EdisonPythonVisitor(List<List<Phrase>> programPhrases, ClassToInstanceMap<IProjectBean> beans) {
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
     * Generates the program suffix, i.e. everything that will be appended to the very end of the source .py file In the suffix are NEPO helper methods for
     * things like sum, round, min, max, etc
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
    public Void visitTimerSensor(TimerSensor timerSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        throw new DbcException("Not supported!");
    }

    /**
     * Visit the block "controls_repeat_ext"
     */
    @Override
    protected void generateCodeFromStmtConditionFor(String stmtType, Expr expr) {
        this.sb.append(stmtType).append(" ");
        ExprList expressions = (ExprList) expr;
        expressions.get().get(0).accept(this);
        this.sb.append(" in range(");
        expressions.get().get(2).accept(this);
        this.sb.append("):");
    }

    /**
     * Function to get readings from the obstacle detector. visit a {@link InfraredSensor} for the block "robSensors_infrared_getSample"
     *
     * @param infraredSensor to be visited
     */
    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.OBSTACLEDETECTION));
        this.sb.append("(");

        switch ( infraredSensor.getUserDefinedPort() ) {
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

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
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
     * Function to get the light level from a phototransistor/light sensor The light level reported from Edison is between 0 and 32767, but the return value is
     * in percent. That's why it is divided by 10. The line tracker can also report if the robot is on a line, but for this to work the robot has to be placed
     * on a white surface when the program starts. visit a {@link LightSensor} for the block "robSensors_light_getSample"
     *
     * @param lightSensor the sensor
     */
    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        switch ( lightSensor.getUserDefinedPort() ) {
            case "LLIGHT":
                this.sb.append("Ed.ReadLeftLightLevel() / 10");
                break;
            case "RLIGHT":
                this.sb.append("Ed.ReadRightLightLevel() / 10");
                break;
            case "LINETRACKER":
                if ( lightSensor.getMode().equals("LINE") ) {
                    this.sb.append("(Ed.ReadLineState() == Ed.LINE_ON_BLACK)");
                } else {
                    this.sb.append("Ed.ReadLineTracker() / 10");
                }
                break;
        }
        return null;
    }

    /**
     * Function to detect claps from the sound sensor visit a {@link SoundSensor} for the block "robSensors_sound_getSample"
     *
     * @param soundSensor the sensor
     */
    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.sb.append("(Ed.ReadClapSensor() == Ed.CLAP_DETECTED)");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEND));
        this.sb.append("(");
        sendIRAction.code.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEEK));
        this.sb.append("(0)");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        String direction = "Ed.FORWARD";
        switch ( driveAction.direction.toString() ) {
            case "FOREWARD":
                break;
            case "BACKWARD":
                direction = "Ed.BACKWARD";
                break;
        }
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class)
            .getHelperMethodGenerator()
            .getHelperMethodName(EdisonMethods.DIFFDRIVE));
        this.sb.append("(").append(direction).append(", ");
        driveAction.param.getSpeed().accept(this);
        this.sb.append(", ");
        if ( driveAction.param.getDuration() != null ) {
            driveAction.param.getDuration().getValue().accept(this);
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED");
        }
        this.sb.append(")");
        if ( driveAction.param.getDuration() != null ) {
            nlIndent();
            this.sb.append("Ed.ReadClapSensor()");
        }
        return null;
    }

    /**
     * Function to perform mathematics on a list (sum/average/min/max/...) Visit a {@link MathOnListFunct} for the block "math_on_list"
     *
     * @param mathOnListFunct to be visited
     */
    @Override
    public Void visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        if ( mathOnListFunct.functName == FunctionNames.AVERAGE ) {
            this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(SUM));
            this.sb.append("(");
            mathOnListFunct.param.get(0).accept(this);
            this.sb.append(") / len(");
            mathOnListFunct.param.get(0).accept(this);
            this.sb.append(")");
        } else {
            super.visitMathOnListFunct(mathOnListFunct);
        }
        return null;
    }

    @Override
    public Void visitNumConst(NumConst numConst) {
        if ( isInteger(numConst.value) ) {
            super.visitNumConst(numConst);
        } else {
            throw new IllegalArgumentException("Not an integer");
        }
        return null;
    }

    /**
     * Visits the programs main task, i.e. the Blockly-blocks that are (indirectly) connected to the red "robControls_start"-block. In this method,
     * user-generated Blockly-methods are appended also
     *
     * @param mainTask the main task class to be visited
     * @return null
     */
    @Override
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        variables.accept(this); //fill usedGlobalVarInFunctions with values

        nlIndent();
        generateUserDefinedMethods(); //Functions created by the user will be defined before the main function

        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        int listSize = listCreate.exprList.get().size();

        this.sb.append("Ed.List(").append(listSize).append(", [");
        if ( listSize == 0 ) {
            this.sb.append("]");
        } else {
            for ( int i = 0; i < listSize; i++ ) {
                listCreate.exprList.get().get(i).accept(this);
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
     * Function to execute code when a key is pressed visit a {@link KeysSensor} for the block "robSensors_key_getSample"
     *
     * @param keysSensor to be visited
     */
    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        switch ( keysSensor.getUserDefinedPort() ) {
            case "REC":
                this.sb.append("(Ed.ReadKeypad() == Ed.KEYPAD_ROUND)");
                break;
            case "PLAY":
                this.sb.append("(Ed.ReadKeypad() == Ed.KEYPAD_TRIANGLE)");
                break;
            default:
        }

        return null;
    }

    /**
     * Function to drive a curve forward/backward. This is still very buggy when used together with sensors (e.g. drive a curve until an obstacle is detected).
     * visit a {@link CurveAction} for the block "robActions_motorDiff_curve_for" and "robActions_motorDiff_curve"
     *
     * @param curveAction to visit
     */
    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        String direction;

        //determine the direction
        switch ( curveAction.direction.toString() ) {
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
        curveAction.paramLeft.getSpeed().accept(this);
        this.sb.append(", ");
        curveAction.paramRight.getSpeed().accept(this);
        if ( curveAction.paramLeft.getDuration() == null ) {
            this.sb.append(", Ed.DISTANCE_UNLIMITED");
        } else {
            this.sb.append(", ");
            curveAction.paramLeft.getDuration().getValue().accept(this);
        }
        this.sb.append(")");
        if ( curveAction.paramLeft.getDuration() != null ) {
            nlIndent();
            this.sb.append("Ed.ReadClapSensor()");
        }
        return null;
    }

    /**
     * Function to turn the robot visit a {@link TurnAction} for the block "robActions_motorDiff_turn_for" and "robActions_motorDiff_turn"
     *
     * @param turnAction to be visited
     */
    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.DIFFTURN));
        this.sb.append("(Ed.SPIN_").append(turnAction.direction).append(", ");
        turnAction.param.getSpeed().accept(this);
        this.sb.append(", ");
        if ( turnAction.param.getDuration() != null ) {
            turnAction.param.getDuration().getValue().accept(this);
        } else {
            this.sb.append("Ed.DISTANCE_UNLIMITED");
        }
        this.sb.append(")");
        if ( turnAction.param.getDuration() != null ) {
            nlIndent();
            this.sb.append("Ed.ReadClapSensor()");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.MOTORON));
        switch ( motorOnAction.getUserDefinedPort() ) {
            case "LMOTOR":
                this.sb.append("(0, ");
                motorOnAction.param.getSpeed().accept(this);
                break;
            case "RMOTOR":
                this.sb.append("(1, ");
                motorOnAction.param.getSpeed().accept(this);
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

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
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
        nlIndent();
        this.sb.append("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        if ( binary.op == Binary.Op.DIVIDE ) { // general implementation casts to float, which is not allowed on edison
            binary.left.accept(this);
            this.sb.append(" / ");
            binary.getRight().accept(this);
        } else {
            super.visitBinary(binary);
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.sb.append("Ed.Drive(Ed.STOP, Ed.SPEED_1, 1)");
        nlIndent();
        this.sb.append("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathPowerFunct.functName));
        this.sb.append("(");
        mathPowerFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathPowerFunct.param.get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case POW10:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER));
                this.sb.append("(10, ");
                mathSingleFunct.param.get(0).accept(this);
                this.sb.append(")");
                break;
            case ROUND: // TODO should be removed after some time, round block removed from toolbox
                this.sb.append("((");
                mathSingleFunct.param.get(0).accept(this);
                this.sb.append("+5)/10)*10");
                break;
            case ROUNDUP: // TODO should be removed after some time, round block removed from toolbox
                this.sb.append("((");
                mathSingleFunct.param.get(0).accept(this);
                this.sb.append("/10)+1)*10");
                break;
            case ROUNDDOWN: // TODO should be removed after some time, round block removed from toolbox
                this.sb.append("(");
                mathSingleFunct.param.get(0).accept(this);
                this.sb.append("/10)");
                break;
            default:
                this.sb.append(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathSingleFunct.functName));
                this.sb.append("(");
                mathSingleFunct.param.get(0).accept(this);
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
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.sb.append("pass");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("Ed.TimeWait(");
        waitTimeStmt.time.accept(this);
        this.sb.append(", Ed.TIME_MILLISECONDS)");
        return null;
    }

    /**
     * Function to turn on the LEDs visit a {@link LightAction} for the block "robActions_led_on"
     *
     * @param lightAction to be visited
     */
    @Override
    public Void visitLightAction(LightAction lightAction) {
        switch ( lightAction.port ) {
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
     * Function to turn off the LEDs visit a {@link LightStatusAction} for the block "robActions_led_off"
     *
     * @param lightStatusAction to be visited
     */
    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        switch ( lightStatusAction.getUserDefinedPort() ) {
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
     * Function to play a tone visit a {@link ToneAction} for the block "robActions_play_tone"
     *
     * @param toneAction to be visited
     */
    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.sb.append("Ed.PlayTone(8000000/");
        toneAction.frequency.accept(this);
        this.sb.append(", ");
        toneAction.duration.accept(this);
        this.sb.append(")");
        nlIndent();
        this.sb.append("Ed.TimeWait(");
        toneAction.duration.accept(this);
        this.sb.append(", Ed.TIME_MILLISECONDS)");
        nlIndent();
        this.sb.append("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.sb.append("Ed.PlayTone(4000000/"); //eigentlich 8mio aber die zahlen bei tiefen noten werden zu groß für edison
        this.sb.append(Integer.parseInt(playNoteAction.frequency.split("\\.")[0]));
        this.sb.append(", ").append(playNoteAction.duration).append(")");
        nlIndent();
        this.sb.append("Ed.TimeWait(").append(playNoteAction.duration).append(", Ed.TIME_MILLISECONDS)");
        nlIndent();
        this.sb.append("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        switch ( playFileAction.fileName.toLowerCase() ) {
            case "0":
                this.sb.append("___soundfile1 = Ed.TuneString(7,\"c8e8g8z\")"); //positiv
                nlIndent();
                this.sb.append("Ed.PlayTune(___soundfile1)");
                break;
            case "1":
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
        nlIndent();
        this.sb.append("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitResetSensor(ResetSensor resetSensor) {
        switch ( resetSensor.sensor ) {
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
     * visit a {@link GetSampleSensor} Needs to override parent method to add parenthesis.
     *
     * @param sensorGetSample to be visited
     */
    @Override
    public Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        this.sb.append("(");
        sensorGetSample.sensor.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.sb.append("if ");
        methodIfReturn.oraCondition.accept(this);
        if ( !methodIfReturn.oraReturnValue.getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(": return ");
            methodIfReturn.oraReturnValue.accept(this);
        } else {
            this.sb.append(": return");
        }
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}