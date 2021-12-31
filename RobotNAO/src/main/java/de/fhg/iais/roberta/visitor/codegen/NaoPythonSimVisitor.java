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
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
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
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrentSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.NaoSimMethods;
import de.fhg.iais.roberta.visitor.INaoVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

public final class NaoPythonSimVisitor extends AbstractPythonVisitor implements INaoVisitor<Void> {

    protected ILanguage language;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public NaoPythonSimVisitor(List<List<Phrase<Void>>> programPhrases, ILanguage language, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);

        this.language = language;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        this.sb.append("int(\"{:02x}{:02x}{:02x}\".format(min(max(");
        rgbColor.getR().accept(this);
        this.sb.append(", 0), 255), min(max(");
        rgbColor.getG().accept(this);
        this.sb.append(", 0), 255), min(max(");
        rgbColor.getB().accept(this);
        this.sb.append(", 0), 255), 16))");
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while robot.step(robot.timeStep) != -1:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("wait(robot, 1)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("wait(robot, ");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        this.sb.append("robot = Nao()");
        nlIndent();
        this.sb.append("robot.load_motion_files()");
        StmtList<Void> variables = mainTask.getVariables();
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        nlIndent();
        this.sb.append("robot.step(robot.timeStep)");

        List<Stmt<Void>> variableList = variables.get();
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
            for ( Stmt<Void> s : variables.get() ) {
                ExprStmt<Void> es = (ExprStmt<Void>) s;
                VarDeclaration<Void> vd = (VarDeclaration<Void>) es.getExpr();
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
    public Void visitHand(Hand<Void> hand) {
        this.sb.append("move_hand_joint(robot, ");
        switch ( hand.getTurnDirection() ) {
            case LEFT:
                this.sb.append("BodySide.LEFT, ");
                break;
            case RIGHT:
                this.sb.append("BodySide.RIGHT, ");
                break;
        }
        switch ( hand.getModus() ) {
            case ACTIVE:
                this.sb.append("1)");
                break;
            case REST:
                this.sb.append("0)");
                break;
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        this.sb.append("move_joint(robot, ");
        switch ( moveJoint.getJoint() ) {
            case HEADYAW:
                this.sb.append("\"HeadYaw\"");
                break;
            case HEADPITCH:
                this.sb.append("\"HeadPitch\"");
                break;
            case LSHOULDERPITCH:
                this.sb.append("\"LShoulderPitch\"");
                break;
            case LSHOULDERROLL:
                this.sb.append("\"LShoulderRoll\"");
                break;
            case LELBOWYAW:
                this.sb.append("\"LElbowYaw\"");
                break;
            case LELBOWROLL:
                this.sb.append("\"LElbowRoll\"");
                break;
            case LWRISTYAW:
                this.sb.append("\"LWristYaw\"");
                break;
            case LHAND:
                this.sb.append("\"LHand\"");
                break;
            case LHIPYAWPITCH:
                this.sb.append("\"LHipYawPitch\"");
                break;
            case LHIPROLL:
                this.sb.append("\"LHipRoll\"");
                break;
            case LHIPPITCH:
                this.sb.append("\"LHipPitch\"");
                break;
            case LKNEEPITCH:
                this.sb.append("\"LKneePitch\"");
                break;
            case LANKLEPITCH:
                this.sb.append("\"LAnklePitch\"");
                break;
            case RANKLEROLL:
                this.sb.append("\"RAnkleRoll\"");
                break;
            case RHIPYAWPITCH:
                this.sb.append("\"RHipYawPitch\"");
                break;
            case RHIPROLL:
                this.sb.append("\"RHipRoll\"");
                break;
            case RHIPPITCH:
                this.sb.append("\"RHipPitch\"");
                break;
            case RKNEEPITCH:
                this.sb.append("\"RKneePitch\"");
                break;
            case RANKLEPITCH:
                this.sb.append("\"RAnklePitch\"");
                break;
            case RSHOULDERPITCH:
                this.sb.append("\"RShoulderPitch\"");
                break;
            case RSHOULDERROLL:
                this.sb.append("\"RShoulderRoll\"");
                break;
            case RELBOWYAW:
                this.sb.append("\"RElbowYaw\"");
                break;
            case RELBOWROLL:
                this.sb.append("\"RElbowRoll\"");
                break;
            case RWRISTYAW:
                this.sb.append("\"RWristYaw\"");
                break;
            case RHAND:
                this.sb.append("\"RHand\"");
                break;
            case LANKLEROLL:
                this.sb.append("\"LAnkleRoll\"");
                break;
            default:
                break;
        }
        this.sb.append(", ");
        moveJoint.getDegrees().accept(this);
        this.sb.append(", ");
        switch ( moveJoint.getRelativeAbsolute() ) {
            case ABSOLUTE:
                this.sb.append("JointMovement.ABSOLUTE");
                break;
            case RELATIVE:
                this.sb.append("JointMovement.RELATIVE");
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        this.sb.append("walk(robot, ");
        if ( walkDistance.getWalkDirection() == DriveDirection.BACKWARD ) {
            this.sb.append("-");
        }
        walkDistance.getDistanceToWalk().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        this.sb.append("turn(robot, ");
        if ( turnDegrees.getTurnDirection() == TurnDirection.RIGHT ) {
            this.sb.append("-");
        }
        turnDegrees.getDegreesToTurn().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        this.sb.append("set_led(robot, Led.");
        this.sb.append(setLeds.getLed().toString());
        this.sb.append(", ");
        setLeds.getColor().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        this.sb.append("set_intensity(robot, Led.");
        this.sb.append(setIntensity.getLed().toString());
        this.sb.append(", ");
        setIntensity.getIntensity().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedOff(LedOff<Void> ledOff) {
        this.sb.append("set_led(robot, Led.");
        this.sb.append(ledOff.getLed().toString());
        this.sb.append(", 0)");
        return null;
    }

    @Override
    public Void visitLedReset(LedReset<Void> ledReset) {
        this.sb.append("set_led(robot, Led.");
        this.sb.append(ledReset.getLed().toString());
        this.sb.append(", 0)");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("is_touched(robot, ");
        this.sb.append(getEnumCode(touchSensor.getUserDefinedPort()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(touchSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> sonar) {
        this.sb.append("get_ultrasonic(robot)");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.sb.append("get_gyro_");
        this.sb.append(gyroSensor.getSlot().toLowerCase());
        this.sb.append("(robot)");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometer) {
        this.sb.append("get_accelerometer_");
        this.sb.append(accelerometer.getUserDefinedPort().toLowerCase());
        this.sb.append("(robot)");
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor<Void> fsr) {
        this.sb.append("get_force(robot, BodySide.");
        this.sb.append(fsr.getUserDefinedPort().toUpperCase());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while robot.step(robot.timeStep) != -1 and", repeatStmt.getExpr());
                appendTry();
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                appendTry();
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                appendTry();
                break;
            default:
                throw new DbcException("Invalid Repeat Statement!");
        }
        incrIndentation();
        appendPassIfEmptyBody(repeatStmt);
        repeatStmt.getList().accept(this);
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
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append(colorConst.getHexIntAsString());
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return sensorGetSample.getSensor().accept(this);
    }

    @Override
    public Void visitWalkAsync(WalkAsync<Void> walkAsync) {
        return null;
    }

    @Override
    public Void visitSetMode(SetMode<Void> setMode) {
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        this.sb.append("perform(robot, \"").append(applyPosture.getPosture()).append("\")");
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness<Void> setStiffness) {
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous<Void> autonomous) {
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        return null;
    }

    @Override
    public Void visitStop(Stop<Void> stop) {
        return null;
    }

    @Override
    public Void visitAnimation(Animation<Void> animation) {
        this.sb.append("perform(robot, \"").append(animation.getMove()).append("\")");
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        this.sb.append("setVolume(robot, ");
        setVolume.getVolume().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {
        this.sb.append("getVolume(robot)");
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        this.sb.append("setLanguage(robot, \"").append(getLanguageString(setLanguageAction.getLanguage())).append("\")");
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        this.sb.append("say(robot, ");
        this.sb.append("str(");
        sayTextAction.getMsg().accept(this);
        this.sb.append(")");

        BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        if ( !(sayTextAction.getSpeed().getKind().equals(emptyBlock) && sayTextAction.getPitch().getKind().equals(emptyBlock)) ) {
            this.sb.append(",");
            sayTextAction.getSpeed().accept(this);
            this.sb.append(",");
            sayTextAction.getPitch().accept(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
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
    public Void visitPlayFile(PlayFile<Void> playFile) {
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor<Void> detectedMark) {
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        return null;
    }

    @Override
    public Void visitDetectFaceSensor(DetectFaceSensor<Void> detectFace) {
        return null;
    }

    @Override
    public Void visitElectricCurrentSensor(ElectricCurrentSensor<Void> electricCurrent) {
        return null;
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord<Void> recognizeWord) {
        this.sb.append("getRecognizedWord(robot, ");
        recognizeWord.vocabulary.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation<Void> naoMarkInformation) {
        return null;
    }

    @Override
    public Void visitDetectedFaceInformation(DetectedFaceInformation<Void> detectedFaceInformation) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
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