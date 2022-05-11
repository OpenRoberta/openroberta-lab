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
import de.fhg.iais.roberta.syntax.action.nao.Animation;
import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.Autonomous;
import de.fhg.iais.roberta.syntax.action.nao.ForgetFace;
import de.fhg.iais.roberta.syntax.action.nao.GetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.GetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Hand;
import de.fhg.iais.roberta.syntax.action.nao.LearnFace;
import de.fhg.iais.roberta.syntax.action.nao.LedOff;
import de.fhg.iais.roberta.syntax.action.nao.LedReset;
import de.fhg.iais.roberta.syntax.action.nao.MoveJoint;
import de.fhg.iais.roberta.syntax.action.nao.PlayFile;
import de.fhg.iais.roberta.syntax.action.nao.PointLookAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.RecordVideo;
import de.fhg.iais.roberta.syntax.action.nao.SetIntensity;
import de.fhg.iais.roberta.syntax.action.nao.SetLeds;
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
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
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
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
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
        this.sb.append("int(\"{:02x}{:02x}{:02x}\".format(min(max(");
        rgbColor.R.accept(this);
        this.sb.append(", 0), 255), min(max(");
        rgbColor.G.accept(this);
        this.sb.append(", 0), 255), min(max(");
        rgbColor.B.accept(this);
        this.sb.append(", 0), 255)), 16)");
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.sb.append("while robot.step(robot.timeStep) != -1:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.sb.append("wait(robot, 1)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("wait(robot, ");
        waitTimeStmt.time.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        this.sb.append("robot = Nao()");
        nlIndent();
        this.sb.append("robot.load_motion_files()");
        StmtList variables = mainTask.variables;
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        nlIndent();
        this.sb.append("robot.step(robot.timeStep)");

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
            this.sb.append("global ");
            boolean first = true;
            for ( Stmt s : variables.get() ) {
                ExprStmt es = (ExprStmt) s;
                VarDeclaration vd = (VarDeclaration) es.expr;
                if ( first ) {
                    first = false;
                } else {
                    this.sb.append(", ");
                }
                this.sb.append(vd.getCodeSafeName());
            }
        }
        return null;
    }

    @Override
    public Void visitHand(Hand hand) {
        this.sb.append("move_hand_joint(robot, ");
        switch ( hand.turnDirection ) {
            case LEFT:
                this.sb.append("BodySide.LEFT, ");
                break;
            case RIGHT:
                this.sb.append("BodySide.RIGHT, ");
                break;
        }
        switch ( hand.modus ) {
            case "OPEN":
                this.sb.append("1)");
                break;
            case "CLOSE":
                this.sb.append("0)");
                break;
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint moveJoint) {
        this.sb.append("move_joint(robot, ");
        switch ( moveJoint.joint ) {
            case "HEADYAW":
                this.sb.append("\"HeadYaw\"");
                break;
            case "HEADPITCH":
                this.sb.append("\"HeadPitch\"");
                break;
            case "LSHOULDERPITCH":
                this.sb.append("\"LShoulderPitch\"");
                break;
            case "LSHOULDERROLL":
                this.sb.append("\"LShoulderRoll\"");
                break;
            case "LELBOWYAW":
                this.sb.append("\"LElbowYaw\"");
                break;
            case "LELBOWROLL":
                this.sb.append("\"LElbowRoll\"");
                break;
            case "LWRISTYAW":
                this.sb.append("\"LWristYaw\"");
                break;
            case "LHAND":
                this.sb.append("\"LHand\"");
                break;
            case "LHIPYAWPITCH":
                this.sb.append("\"LHipYawPitch\"");
                break;
            case "LHIPROLL":
                this.sb.append("\"LHipRoll\"");
                break;
            case "LHIPPITCH":
                this.sb.append("\"LHipPitch\"");
                break;
            case "LKNEEPITCH":
                this.sb.append("\"LKneePitch\"");
                break;
            case "LANKLEPITCH":
                this.sb.append("\"LAnklePitch\"");
                break;
            case "RANKLEROLL":
                this.sb.append("\"RAnkleRoll\"");
                break;
            case "RHIPYAWPITCH":
                this.sb.append("\"RHipYawPitch\"");
                break;
            case "RHIPROLL":
                this.sb.append("\"RHipRoll\"");
                break;
            case "RHIPPITCH":
                this.sb.append("\"RHipPitch\"");
                break;
            case "RKNEEPITCH":
                this.sb.append("\"RKneePitch\"");
                break;
            case "RANKLEPITCH":
                this.sb.append("\"RAnklePitch\"");
                break;
            case "RSHOULDERPITCH":
                this.sb.append("\"RShoulderPitch\"");
                break;
            case "RSHOULDERROLL":
                this.sb.append("\"RShoulderRoll\"");
                break;
            case "RELBOWYAW":
                this.sb.append("\"RElbowYaw\"");
                break;
            case "RELBOWROLL":
                this.sb.append("\"RElbowRoll\"");
                break;
            case "RWRISTYAW":
                this.sb.append("\"RWristYaw\"");
                break;
            case "RHAND":
                this.sb.append("\"RHand\"");
                break;
            case "LANKLEROLL":
                this.sb.append("\"LAnkleRoll\"");
                break;
            default:
                throw new DbcException("Invalid MoveJoint JOINT...");
        }
        this.sb.append(", ");
        moveJoint.degrees.accept(this);
        this.sb.append(", ");
        switch ( moveJoint.relativeAbsolute ) {
            case "ABSOLUTE":
                this.sb.append("JointMovement.ABSOLUTE");
                break;
            case "RELATIVE":
                this.sb.append("JointMovement.RELATIVE");
                break;
            default:
                throw new DbcException("Invalid MoveJoint MODE...");
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance walkDistance) {
        this.sb.append("walk(robot, ");
        if ( walkDistance.walkDirection == DriveDirection.BACKWARD ) {
            this.sb.append("-");
        }
        walkDistance.distanceToWalk.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees turnDegrees) {
        this.sb.append("turn(robot, ");
        if ( turnDegrees.turnDirection == TurnDirection.RIGHT ) {
            this.sb.append("-");
        }
        turnDegrees.degreesToTurn.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds setLeds) {
        this.sb.append("set_led(robot, Led.");
        this.sb.append(setLeds.led.toString());
        this.sb.append(", ");
        setLeds.Color.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity setIntensity) {
        this.sb.append("set_intensity(robot, Led.");
        this.sb.append(setIntensity.led.toString());
        this.sb.append(", ");
        setIntensity.Intensity.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedOff(LedOff ledOff) {
        this.sb.append("set_led(robot, Led.");
        this.sb.append(ledOff.led.toString());
        this.sb.append(", 0)");
        return null;
    }

    @Override
    public Void visitLedReset(LedReset ledReset) {
        this.sb.append("set_led(robot, Led.");
        this.sb.append(ledReset.led.toString());
        this.sb.append(", 0)");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.sb.append("is_touched(robot, ");
        this.sb.append(getEnumCode(touchSensor.getUserDefinedPort()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(touchSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor sonar) {
        this.sb.append("get_ultrasonic(robot)");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        this.sb.append("get_gyro_");
        this.sb.append(gyroSensor.getSlot().toLowerCase());
        this.sb.append("(robot)");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometer) {
        this.sb.append("get_accelerometer_");
        this.sb.append(accelerometer.getUserDefinedPort().toLowerCase());
        this.sb.append("(robot)");
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor fsr) {
        this.sb.append("get_force(robot, BodySide.");
        this.sb.append(fsr.getUserDefinedPort().toUpperCase());
        this.sb.append(")");
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
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();

        Set<Enum<?>> usedMethods = new HashSet<>();
        usedMethods.add(NaoSimMethods.MOTIONS);
        usedMethods.add(NaoSimMethods.TEMPLATE);
        String template = this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodDefinitions(usedMethods);
        this.sb.append(template);
        nlIndent();

        if ( !this.getBean(UsedHardwareBean.class).getLoopsLabelContainer().isEmpty() ) {
            nlIndent();
            this.sb.append("class BreakOutOfALoop(Exception): pass");
            nlIndent();
            this.sb.append("class ContinueLoop(Exception): pass");
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
        this.sb.append("wait(robot, 3000)"); // give the simulation time to render all
        nlIndent();
        this.sb.append("print(\"finished\")");
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.sb.append("def main():");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("run()");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("raise");
        decrIndentation();
        nlIndent();
        decrIndentation();
        nlIndent();
        super.generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.sb.append(colorConst.getHexIntAsString());
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
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
        this.sb.append("perform(robot, \"").append(applyPosture.posture).append("\")");
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
        this.sb.append("perform(robot, \"").append(animation.move).append("\")");
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt pointLookAt) {
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume setVolume) {
        this.sb.append("setVolume(robot, ");
        setVolume.volume.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume getVolume) {
        this.sb.append("getVolume(robot)");
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        this.sb.append("setLanguage(robot, \"").append(getLanguageString(setLanguageAction.language)).append("\")");
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
        this.sb.append(", ");
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
        this.sb.append("getRecognizedWord(robot, ");
        recognizeWord.vocabulary.accept(this);
        this.sb.append(")");
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