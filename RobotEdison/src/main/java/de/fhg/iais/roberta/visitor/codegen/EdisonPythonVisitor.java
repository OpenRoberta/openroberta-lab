package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
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

    @Override
    protected void visitorGenerateImports() {
        this.src.addLine("import Ed");
    }

    @Override
    protected void visitorGenerateGlobalVariables() {
        this.src.ensureBlankLines(1);
        this.src.addLine("Ed.EdisonVersion = Ed.V2");
        this.src.addLine("Ed.DistanceUnits = Ed.CM");
        this.src.addLine("Ed.Tempo = Ed.TEMPO_SLOW");
        this.src.addLine("obstacleDetectionOn = False");
        this.src.addLine("Ed.LineTrackerLed(Ed.ON)");
        this.src.addLine("Ed.ReadClapSensor()");
        this.src.addLine("Ed.ReadLineState()");
        this.src.addLine("Ed.TimeWait(250, Ed.TIME_MILLISECONDS)"); //möglicherweise überflüssig
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
        this.src.add(stmtType, " ");
        ExprList expressions = (ExprList) expr;
        expressions.get().get(0).accept(this);
        this.src.add(" in range(");
        expressions.get().get(2).accept(this);
        this.src.add("):");
    }

    /**
     * Function to get readings from the obstacle detector. visit a {@link InfraredSensor} for the block "robSensors_infrared_getSample"
     *
     * @param infraredSensor to be visited
     */
    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.OBSTACLEDETECTION));
        this.src.add("(");

        switch ( infraredSensor.getUserDefinedPort() ) {
            case "FRONT":
                this.src.add("Ed.OBSTACLE_AHEAD");
                break;
            case "LEFT":
                this.src.add("Ed.OBSTACLE_LEFT");
                break;
            case "RIGHT":
                this.src.add("Ed.OBSTACLE_RIGHT");
                break;
        }

        this.src.add(")");
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEEK));
        switch ( irSeekerSensor.getMode() ) {
            case "RCCODE":
                this.src.add("(1)");
                break;
            case "EDISON_CODE":
                this.src.add("(0)");
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
                this.src.add("Ed.ReadLeftLightLevel() / 10");
                break;
            case "RLIGHT":
                this.src.add("Ed.ReadRightLightLevel() / 10");
                break;
            case "LINETRACKER":
                if ( lightSensor.getMode().equals("LINE") ) {
                    this.src.add("(Ed.ReadLineState() == Ed.LINE_ON_BLACK)");
                } else {
                    this.src.add("Ed.ReadLineTracker() / 10");
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
        this.src.add("(Ed.ReadClapSensor() == Ed.CLAP_DETECTED)");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEND));
        this.src.add("(");
        sendIRAction.code.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.IRSEEK));
        this.src.add("(0)");
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
        this.src.add(this.getBean(CodeGeneratorSetupBean.class)
            .getHelperMethodGenerator()
            .getHelperMethodName(EdisonMethods.DIFFDRIVE));
        this.src.add("(", direction, ", ");
        driveAction.param.getSpeed().accept(this);
        this.src.add(", ");
        if ( driveAction.param.getDuration() != null ) {
            driveAction.param.getDuration().getValue().accept(this);
        } else {
            this.src.add("Ed.DISTANCE_UNLIMITED");
        }
        this.src.add(")");
        if ( driveAction.param.getDuration() != null ) {
            nlIndent();
            this.src.add("Ed.ReadClapSensor()");
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
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(SUM));
            this.src.add("(");
            mathOnListFunct.list.accept(this);
            this.src.add(") / len(");
            mathOnListFunct.list.accept(this);
            this.src.add(")");
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
        visitorGenerateUserVariablesAndMethods(mainTask);
        src.ensureBlankLines(1);
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        int listSize = listCreate.exprList.get().size();

        this.src.add("Ed.List(", listSize, ", [");
        if ( listSize == 0 ) {
            this.src.add("]");
        } else {
            for ( int i = 0; i < listSize; i++ ) {
                listCreate.exprList.get().get(i).accept(this);
                if ( i < listSize - 1 ) {
                    this.src.add(",");
                } else {
                    this.src.add("]");
                }
            }
        }
        this.src.add(")");

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
                this.src.add("(Ed.ReadKeypad() == Ed.KEYPAD_ROUND)");
                break;
            case "PLAY":
                this.src.add("(Ed.ReadKeypad() == Ed.KEYPAD_TRIANGLE)");
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
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.DIFFCURVE), "(", direction, ", ");
        curveAction.paramLeft.getSpeed().accept(this);
        this.src.add(", ");
        curveAction.paramRight.getSpeed().accept(this);
        if ( curveAction.paramLeft.getDuration() == null ) {
            this.src.add(", Ed.DISTANCE_UNLIMITED");
        } else {
            this.src.add(", ");
            curveAction.paramLeft.getDuration().getValue().accept(this);
        }
        this.src.add(")");
        if ( curveAction.paramLeft.getDuration() != null ) {
            nlIndent();
            this.src.add("Ed.ReadClapSensor()");
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
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.DIFFTURN));
        this.src.add("(Ed.SPIN_", turnAction.direction, ", ");
        turnAction.param.getSpeed().accept(this);
        this.src.add(", ");
        if ( turnAction.param.getDuration() != null ) {
            turnAction.param.getDuration().getValue().accept(this);
        } else {
            this.src.add("Ed.DISTANCE_UNLIMITED");
        }
        this.src.add(")");
        if ( turnAction.param.getDuration() != null ) {
            nlIndent();
            this.src.add("Ed.ReadClapSensor()");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        throw new DbcException("block is not implemented");
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(EdisonMethods.MOTORON));
        switch ( motorOnAction.getUserDefinedPort() ) {
            case "LMOTOR":
                this.src.add("(0, ");
                motorOnAction.param.getSpeed().accept(this);
                break;
            case "RMOTOR":
                this.src.add("(1, ");
                motorOnAction.param.getSpeed().accept(this);
                break;
            default:
                break;
        }
        this.src.add(", ");

        if ( motorOnAction.getDurationValue() != null ) {
            motorOnAction.getDurationValue().accept(this);
            this.src.add(")");
        } else {
            this.src.add("Ed.DISTANCE_UNLIMITED)");
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
                this.src.add("Ed.DriveLeftMotor(Ed.STOP, Ed.SPEED_1, 1)");
                break;
            case "C":
            case "RMOTOR":
                this.src.add("Ed.DriveRightMotor(Ed.STOP, Ed.SPEED_1, 1)");
                break;
            default:
                break;
        }
        nlIndent();
        this.src.add("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        if ( binary.op == Binary.Op.DIVIDE ) { // general implementation casts to float, which is not allowed on edison
            binary.left.accept(this);
            this.src.add(" / ");
            binary.getRight().accept(this);
        } else {
            super.visitBinary(binary);
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.src.add("Ed.Drive(Ed.STOP, Ed.SPEED_1, 1)");
        nlIndent();
        this.src.add("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathPowerFunct.functName));
        this.src.add("(");
        mathPowerFunct.param.get(0).accept(this);
        this.src.add(", ");
        mathPowerFunct.param.get(1).accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        switch ( mathSingleFunct.functName ) {
            case POW10:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(FunctionNames.POWER));
                this.src.add("(10, ");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add(")");
                break;
            case ROUND: // TODO should be removed after some time, round block removed from toolbox
                this.src.add("((");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add("+5)/10)*10");
                break;
            case ROUNDUP: // TODO should be removed after some time, round block removed from toolbox
                this.src.add("((");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add("/10)+1)*10");
                break;
            case ROUNDDOWN: // TODO should be removed after some time, round block removed from toolbox
                this.src.add("(");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add("/10)");
                break;
            default:
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(mathSingleFunct.functName));
                this.src.add("(");
                mathSingleFunct.param.get(0).accept(this);
                this.src.add(")");
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
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("Ed.TimeWait(");
        waitTimeStmt.time.accept(this);
        this.src.add(", Ed.TIME_MILLISECONDS)");
        return null;
    }

    /**
     * Function to turn the LEDs on or off
     * Visit a {@link LedAction} for the block "actions_led_edison"
     *
     * @param ledAction to be visited
     */
    @Override
    public Void visitLedAction(LedAction ledAction) {
        String edPort = "", edMode = "";
        switch ( ledAction.port ) {
            case "1":
            case "RLED":
                edPort = "Ed.RightLed";
                break;
            case "2":
            case "LLED":
                edPort = "Ed.LeftLed";
                break;
            default:
                throw new DbcException("Invalid PORT encountered in LedAction: " + ledAction.port);
        }
        switch ( ledAction.mode ) {
            case "OFF":
                edMode = "(Ed.OFF)";
                break;
            case "ON":
                edMode = "(Ed.ON)";
                break;
            default:
                throw new DbcException("Invalid MODE encountered in LedAction: " + ledAction.mode);
        }
        this.src.add(edPort, edMode);
        return null;
    }

    /**
     * Function to play a tone visit a {@link ToneAction} for the block "robActions_play_tone"
     *
     * @param toneAction to be visited
     */
    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.add("Ed.PlayTone(8000000/");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(")");
        nlIndent();
        this.src.add("Ed.TimeWait(");
        toneAction.duration.accept(this);
        this.src.add(", Ed.TIME_MILLISECONDS)");
        nlIndent();
        this.src.add("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.add("Ed.PlayTone(4000000/"); //eigentlich 8mio aber die zahlen bei tiefen noten werden zu groß für edison
        this.src.add(Integer.parseInt(playNoteAction.frequency.split("\\.")[0]));
        this.src.add(", ", playNoteAction.duration, ")");
        nlIndent();
        this.src.add("Ed.TimeWait(", playNoteAction.duration, ", Ed.TIME_MILLISECONDS)");
        nlIndent();
        this.src.add("Ed.ReadClapSensor()");
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
                this.src.add("___soundfile1 = Ed.TuneString(7,\"c8e8g8z\")"); //positiv
                nlIndent();
                this.src.add("Ed.PlayTune(___soundfile1)");
                break;
            case "1":
                this.src.add("___soundfile2 = Ed.TuneString(7,\"g8e8c8z\")"); //negativ
                nlIndent();
                this.src.add("Ed.PlayTune(___soundfile2)");
                break;
            case "2":
                this.src.add("___soundfile3 = Ed.TuneString(13,\"g4c4g4c4g4c4z\")"); //warnung
                nlIndent();
                this.src.add("Ed.PlayTune(___soundfile3)");
                break;
            case "3":
                this.src.add("___soundfile4 = Ed.TuneString(21, \"c8e8f4g4o4g4b4g8e4n2z\")"); //MP Tune
                nlIndent();
                this.src.add("Ed.PlayTune(___soundfile4)");
                break;
            case "4":
                this.src.add("___soundfile5 = Ed.TuneString(55, \"c4d4e4f4g2g2a4a4a4a4g2R2a4a4a4a4g2f4f4f4e2e2g4g4g4g4c1z\")"); //Alle meine Entchen
                nlIndent();
                this.src.add("Ed.PlayTune(___soundfile5)");
                break;
        }

        nlIndent();
        this.src.add("while (Ed.ReadMusicEnd() == Ed.MUSIC_NOT_FINISHED):");
        incrIndentation();
        nlIndent();
        this.src.add("pass");
        decrIndentation();
        nlIndent();
        this.src.add("Ed.ReadClapSensor()");
        return null;
    }

    @Override
    public Void visitResetSensor(ResetSensor resetSensor) {
        switch ( resetSensor.sensor ) {
            case "OBSTACLEDETECTOR":
                this.src.add("Ed.ReadObstacleDetection()");
                break;
            case "KEYPAD":
                this.src.add("Ed.ReadKeypad()");
                break;
            case "SOUND":
                this.src.add("Ed.ReadClapSensor()");
                break;
            case "RCCODE":
            case "IRCODE":
                this.src.add("Ed.ReadRemote()");
                nlIndent();
                this.src.add("Ed.ReadIRData()");
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
        this.src.add("(");
        sensorGetSample.sensor.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        this.src.add("if ");
        methodIfReturn.oraCondition.accept(this);
        if ( !methodIfReturn.oraReturnValue.getKind().hasName("EMPTY_EXPR") ) {
            this.src.add(": return ");
            methodIfReturn.oraReturnValue.accept(this);
        } else {
            this.src.add(": return");
        }
        return null;
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