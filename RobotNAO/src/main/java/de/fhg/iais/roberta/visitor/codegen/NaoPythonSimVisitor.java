package de.fhg.iais.roberta.visitor.codegen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.nao.Animation;
import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.Autonomous;
import de.fhg.iais.roberta.syntax.action.nao.ForgetFace;
import de.fhg.iais.roberta.syntax.action.nao.GetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.GetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Hand;
import de.fhg.iais.roberta.syntax.action.nao.LearnFace;
import de.fhg.iais.roberta.syntax.action.nao.MoveJoint;
import de.fhg.iais.roberta.syntax.action.nao.NaoLedOnAction;
import de.fhg.iais.roberta.syntax.action.nao.PlayFile;
import de.fhg.iais.roberta.syntax.action.nao.PointLookAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.RecordVideo;
import de.fhg.iais.roberta.syntax.action.nao.SetMode;
import de.fhg.iais.roberta.syntax.action.nao.SetStiffness;
import de.fhg.iais.roberta.syntax.action.nao.SetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Stop;
import de.fhg.iais.roberta.syntax.action.nao.TakePicture;
import de.fhg.iais.roberta.syntax.action.nao.TurnDegrees;
import de.fhg.iais.roberta.syntax.action.nao.WalkAsync;
import de.fhg.iais.roberta.syntax.action.nao.WalkDistance;
import de.fhg.iais.roberta.syntax.action.nao.WalkTo;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrentSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.INaoVisitor;
import de.fhg.iais.roberta.visitor.NaoSimMethods;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

public final class NaoPythonSimVisitor extends AbstractPythonVisitor implements INaoVisitor<Void> {

    protected ILanguage language;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public NaoPythonSimVisitor(List<List<Phrase>> programPhrases, ILanguage language, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);

        this.language = language;
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
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while robot.step(robot.timeStep) != -1:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.src.add("wait(robot, 1)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("wait(robot, ");
        waitTimeStmt.time.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        this.src.add("robot = Nao()");
        nlIndent();
        this.src.add("robot.load_motion_files()");
        StmtList variables = mainTask.variables;
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        nlIndent();
        this.src.add("def run():");
        incrIndentation();
        nlIndent();
        this.src.add("robot.step(robot.timeStep)");

        List<Stmt> variableList = variables.get();
        if ( !variableList.isEmpty() ) {
            nlIndent();
            // insert global statement for all variables
            // TODO: there must be an easier way without the casts
            // TODO: we'd only list variables that we change, ideally we'd do this in
            // visitAssignStmt(), but we must only to this once per method and visitAssignStmt()
            // would need the list of mainTask variables (store in the class?)
            // TODO: I could store the names as a list in the instance and filter it against the parameters
            // in visitMethodVoid, visitMethodReturn
            this.src.add("global ");
            boolean first = true;
            for ( Stmt s : variables.get() ) {
                ExprStmt es = (ExprStmt) s;
                VarDeclaration vd = (VarDeclaration) es.expr;
                if ( first ) {
                    first = false;
                } else {
                    this.src.add(", ");
                }
                this.src.add(vd.getCodeSafeName());
            }
        }
        return null;
    }

    @Override
    public Void visitHand(Hand hand) {
        this.src.add("move_hand_joint(robot, ");
        switch ( hand.turnDirection ) {
            case LEFT:
                this.src.add("BodySide.LEFT, ");
                break;
            case RIGHT:
                this.src.add("BodySide.RIGHT, ");
                break;
        }
        switch ( hand.modus ) {
            case "OPEN":
                this.src.add("1)");
                break;
            case "CLOSE":
                this.src.add("0)");
                break;
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint moveJoint) {
        this.src.add("move_joint(robot, ");
        switch ( moveJoint.joint ) {
            case "HEADYAW":
                this.src.add("\"HeadYaw\"");
                break;
            case "HEADPITCH":
                this.src.add("\"HeadPitch\"");
                break;
            case "LSHOULDERPITCH":
                this.src.add("\"LShoulderPitch\"");
                break;
            case "LSHOULDERROLL":
                this.src.add("\"LShoulderRoll\"");
                break;
            case "LELBOWYAW":
                this.src.add("\"LElbowYaw\"");
                break;
            case "LELBOWROLL":
                this.src.add("\"LElbowRoll\"");
                break;
            case "LWRISTYAW":
                this.src.add("\"LWristYaw\"");
                break;
            case "LHAND":
                this.src.add("\"LHand\"");
                break;
            case "LHIPYAWPITCH":
                this.src.add("\"LHipYawPitch\"");
                break;
            case "LHIPROLL":
                this.src.add("\"LHipRoll\"");
                break;
            case "LHIPPITCH":
                this.src.add("\"LHipPitch\"");
                break;
            case "LKNEEPITCH":
                this.src.add("\"LKneePitch\"");
                break;
            case "LANKLEPITCH":
                this.src.add("\"LAnklePitch\"");
                break;
            case "RANKLEROLL":
                this.src.add("\"RAnkleRoll\"");
                break;
            case "RHIPYAWPITCH":
                this.src.add("\"RHipYawPitch\"");
                break;
            case "RHIPROLL":
                this.src.add("\"RHipRoll\"");
                break;
            case "RHIPPITCH":
                this.src.add("\"RHipPitch\"");
                break;
            case "RKNEEPITCH":
                this.src.add("\"RKneePitch\"");
                break;
            case "RANKLEPITCH":
                this.src.add("\"RAnklePitch\"");
                break;
            case "RSHOULDERPITCH":
                this.src.add("\"RShoulderPitch\"");
                break;
            case "RSHOULDERROLL":
                this.src.add("\"RShoulderRoll\"");
                break;
            case "RELBOWYAW":
                this.src.add("\"RElbowYaw\"");
                break;
            case "RELBOWROLL":
                this.src.add("\"RElbowRoll\"");
                break;
            case "RWRISTYAW":
                this.src.add("\"RWristYaw\"");
                break;
            case "RHAND":
                this.src.add("\"RHand\"");
                break;
            case "LANKLEROLL":
                this.src.add("\"LAnkleRoll\"");
                break;
            default:
                throw new DbcException("Invalid MoveJoint JOINT...");
        }
        this.src.add(", ");
        moveJoint.degrees.accept(this);
        this.src.add(", ");
        switch ( moveJoint.relativeAbsolute ) {
            case "ABSOLUTE":
                this.src.add("JointMovement.ABSOLUTE");
                break;
            case "RELATIVE":
                this.src.add("JointMovement.RELATIVE");
                break;
            default:
                throw new DbcException("Invalid MoveJoint MODE...");
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance walkDistance) {
        this.src.add("walk(robot, ");
        if ( walkDistance.walkDirection == DriveDirection.BACKWARD ) {
            this.src.add("-");
        }
        walkDistance.distanceToWalk.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees turnDegrees) {
        this.src.add("turn(robot, ");
        if ( turnDegrees.turnDirection == TurnDirection.RIGHT ) {
            this.src.add("-");
        }
        turnDegrees.degreesToTurn.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        this.src.add("set_led(robot, Led.");
        this.src.add(rgbLedOnAction.port.toString());
        this.src.add(", ");
        rgbLedOnAction.colour.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitNaoLedOnAction(NaoLedOnAction naoLedOnAction) {
        this.src.add("set_intensity(robot, Led.");
        this.src.add(naoLedOnAction.port.toString());
        this.src.add(", ");
        naoLedOnAction.intensity.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitLedAction(LedAction ledAction) {
        this.src.add("set_led(robot, Led.");
        this.src.add(ledAction.port.toString());
        this.src.add(", 0)");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.src.add("is_touched(robot, ");
        this.src.add(getEnumCode(touchSensor.getUserDefinedPort()));
        this.src.add(", ");
        this.src.add(getEnumCode(touchSensor.getSlot()));
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor sonar) {
        this.src.add("get_ultrasonic(robot)");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        this.src.add("get_gyro_");
        this.src.add(gyroSensor.getSlot().toLowerCase());
        this.src.add("(robot)");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometer) {
        this.src.add("get_accelerometer_");
        this.src.add(accelerometer.getUserDefinedPort().toLowerCase());
        this.src.add("(robot)");
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor fsr) {
        this.src.add("get_force(robot, BodySide.");
        this.src.add(fsr.getUserDefinedPort().toUpperCase());
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        boolean isWaitStmt = repeatStmt.mode == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.mode ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while robot.step(robot.timeStep) != -1 and", repeatStmt.expr);
                appendTry();
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.expr);
                appendTry();
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.expr);
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.expr);
                appendTry();
                break;
            default:
                throw new DbcException("Invalid Repeat Statement!");
        }
        incrIndentation();
        appendPassIfEmptyBody(repeatStmt);
        repeatStmt.list.accept(this);
        if ( !isWaitStmt ) {
            appendExceptionHandling();
        } else {
            appendBreakStmt(repeatStmt);
        }
        decrIndentation();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.src.add("#!/usr/bin/python");
        nlIndent();
        nlIndent();

        Set<Enum<?>> usedMethods = new HashSet<>();
        usedMethods.add(NaoSimMethods.MOTIONS);
        usedMethods.add(NaoSimMethods.TEMPLATE);
        String template = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodDefinitions(usedMethods);
        this.src.add(template);
        nlIndent();

        if ( !this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().isEmpty() ) {
            nlIndent();
            this.src.add("class BreakOutOfALoop(Exception): pass");
            nlIndent();
            this.src.add("class ContinueLoop(Exception): pass");
            nlIndent();
            nlIndent();
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        nlIndent();
        this.src.add("wait(robot, 3000)"); // give the simulation time to render all
        nlIndent();
        this.src.add("print(\"finished\")");
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
        this.src.add("raise");
        decrIndentation();
        nlIndent();
        decrIndentation();
        nlIndent();
        super.generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add(colorConst.getHexIntAsString());
        return null;
    }

    @Override
    public Void visitWalkAsync(WalkAsync walkAsync) {
        return null;
    }

    @Override
    public Void visitSetMode(SetMode setMode) {
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture applyPosture) {
        this.src.add("perform(robot, \"", applyPosture.posture, "\")");
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness setStiffness) {
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous autonomous) {
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo walkTo) {
        return null;
    }

    @Override
    public Void visitStop(Stop stop) {
        return null;
    }

    @Override
    public Void visitAnimation(Animation animation) {
        this.src.add("perform(robot, \"", animation.move, "\")");
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt pointLookAt) {
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume setVolume) {
        this.src.add("setVolume(robot, ");
        setVolume.volume.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume getVolume) {
        this.src.add("getVolume(robot)");
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        this.src.add("setLanguage(robot, \"", getLanguageString(setLanguageAction.language), "\")");
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage getLanguage) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        src.add("say(robot, str(");
        sayTextAction.msg.accept(this);
        src.add("))");
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        src.add("say(robot, str(");
        sayTextAction.msg.accept(this);
        src.add("), ");
        sayTextAction.speed.accept(this);
        this.src.add(", ");
        sayTextAction.pitch.accept(this);
        src.add(")");
        return null;
    }

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "de-DE";
            case ENGLISH:
                return "en-US";
            case FRENCH:
                return "fr-FR";
            case SPANISH:
                return "es-ES";
            case ITALIAN:
                return "it-IT";
            case DUTCH:
                return "nl-NL";
            case POLISH:
                return "pl-PL";
            case RUSSIAN:
                return "ru-RU";
            case PORTUGUESE:
                return "pt-BR";
            case JAPANESE:
                return "ja-JP";
            case CHINESE:
                return "zh-CN";
            case CZECH:
                return "cs-CZ";
            case TURKISH:
                return "te-TR";
            case FINNISH:
                return "fi-FI";
            case DANISH:
                return "da-DK";
            case KOREAN:
                return "ko-KR";
            case ARABIC:
                return "ar-SA";
            case BRAZILIAN:
                return "ptR";
            case SWEDISH:
                return "sv-SE";
            case NORWEGIAN:
                return "no-NO";
            case GREEK:
                return "el-GR";
            default:
                return "";
        }
    }

    @Override
    public Void visitPlayFile(PlayFile playFile) {
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration randomEyesDuration) {
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration rastaDuration) {
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectedMark) {
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture takePicture) {
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo recordVideo) {
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace learnFace) {
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace forgetFace) {
        return null;
    }

    @Override
    public Void visitDetectFaceSensor(DetectFaceSensor detectFace) {
        return null;
    }

    @Override
    public Void visitElectricCurrentSensor(ElectricCurrentSensor electricCurrent) {
        return null;
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord recognizeWord) {
        this.src.add("getRecognizedWord(robot, ");
        recognizeWord.vocabulary.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation naoMarkInformation) {
        return null;
    }

    @Override
    public Void visitDetectedFaceInformation(DetectedFaceInformation detectedFaceInformation) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
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