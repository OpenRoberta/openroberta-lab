package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.UsedSensor;
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
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
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
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.INaoVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class NaoPythonVisitor extends AbstractPythonVisitor implements INaoVisitor<Void> {

    protected ILanguage language;

    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     */
    public NaoPythonVisitor(List<List<Phrase>> programPhrases, ILanguage language, ClassToInstanceMap<IProjectBean> beans) {
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
        this.src.add("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.src.add("h.wait(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("h.wait(");
        waitTimeStmt.time.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        nlIndent();
        this.src.add("def run():");
        incrIndentation();
        if ( mainTask.debug.equals("TRUE") ) {
            nlIndent();
            this.src.add("h.setAutonomousLife('ON')");
        } else {
            nlIndent();
            this.src.add("h.setAutonomousLife('OFF')");
        }
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
    public Void visitSetMode(SetMode setMode) {
        this.src.add("h.mode(");
        switch ( setMode.modus ) {
            case "OPEN":
                this.src.add("1)");
                break;
            case "CLOSE":
                this.src.add("2)");
                break;
            case "SIT":
                this.src.add("3)");
                break;
            default:
                throw new DbcException("Invalid SetMode DIRECTION: " + setMode.modus);
        }
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture applyPosture) {
        this.src.add("h.applyPosture(");
        switch ( applyPosture.posture ) {
            case "STAND":
                this.src.add("\"Stand\")");
                break;
            case "STANDINIT":
                this.src.add("\"StandInit\")");
                break;
            case "STANDZERO":
                this.src.add("\"StandZero\")");
                break;
            case "SITRELAX":
                this.src.add("\"SitRelax\")");
                break;
            case "SIT":
                this.src.add("\"Sit\")");
                break;
            case "LYINGBELLY":
                this.src.add("\"LyingBelly\")");
                break;
            case "LYINGBACK":
                this.src.add("\"LyingBack\")");
                break;
            case "CROUCH":
                this.src.add("\"Crouch\")");
                break;
            case "REST":
                this.src.add("\"Rest\")");
                break;
            default:
                throw new DbcException("Invalid ApplyPosture DIRECTION: " + applyPosture.posture);
        }
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness setStiffness) {
        this.src.add("h.stiffness(");
        switch ( setStiffness.bodyPart ) {
            case "BODY":
                this.src.add("\"Body\"");
                break;
            case "HEAD":
                this.src.add("\"Head\"");
                break;
            case "ARMS":
                this.src.add("\"Arms\"");
                break;
            case "LEFTARM":
                this.src.add("\"LArm\"");
                break;
            case "RIGHTARM":
                this.src.add("\"RArm\"");
                break;
            case "LEGS":
                this.src.add("\"Legs\"");
                break;
            case "LEFTLEG":
                this.src.add("\"LLeg\"");
                break;
            case "RIHTLEG":
                this.src.add("\"RLeg\"");
                break;
            default:
                throw new DbcException("Invalid SetStiffness PART: " + setStiffness.bodyPart);
        }

        switch ( setStiffness.onOff ) {
            case ON:
                this.src.add(", 1)");
                break;
            case OFF:
                this.src.add(", 2)");
                break;
            default:
                throw new DbcException("Invalid SetStiffness MODE: " + setStiffness.onOff);
        }
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous autonomous) {
        this.src.add("h.setAutonomousLife(", getEnumCode(autonomous.onOff).toUpperCase(), ")");
        return null;
    }

    @Override
    public Void visitHand(Hand hand) {
        this.src.add("h.hand(");
        switch ( hand.turnDirection ) {
            case LEFT:
                this.src.add("\"LHand\"");
                break;
            case RIGHT:
                this.src.add("\"RHand\"");
                break;
            default:
                throw new DbcException("Invalid Hand SIDE: " + hand.turnDirection);
        }

        switch ( hand.modus ) {
            case "OPEN":
                this.src.add(", 1)");
                break;
            case "CLOSE":
                this.src.add(", 2)");
                break;
            case "SIT":
                this.src.add(", 3)");
                break;
            default:
                throw new DbcException("Invalid Hand MODE: " + hand.modus);
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint moveJoint) {
        this.src.add("h.moveJoint(");
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
                throw new DbcException("Invalid MoveJoint JOINT: " + moveJoint.joint);
        }
        this.src.add(", ");
        moveJoint.degrees.accept(this);
        this.src.add(", ");
        switch ( moveJoint.relativeAbsolute ) {
            case "ABSOLUTE":
                this.src.add("1)");
                break;
            case "RELATIVE":
                this.src.add("2)");
                break;
            default:
                throw new DbcException("Invalid MoveJoint MODE: " + moveJoint.relativeAbsolute);
        }
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance walkDistance) {
        this.src.add("h.walk(");
        if ( walkDistance.walkDirection == DriveDirection.BACKWARD ) {
            this.src.add("-");
        }
        walkDistance.distanceToWalk.accept(this);
        this.src.add(", 0, 0)");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees turnDegrees) {
        this.src.add("h.walk(0, 0,");
        if ( turnDegrees.turnDirection == TurnDirection.RIGHT ) {
            this.src.add("-");
        }
        turnDegrees.degreesToTurn.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo walkTo) {
        this.src.add("h.walk(");
        walkTo.walkToX.accept(this);
        this.src.add(",");
        walkTo.walkToY.accept(this);
        this.src.add(",");
        walkTo.walkToTheta.accept(this);
        this.src.add(")");

        return null;
    }

    @Override
    public Void visitStop(Stop stop) {
        this.src.add("h.stop()");

        return null;
    }

    @Override
    public Void visitAnimation(Animation animation) {
        switch ( animation.move ) {
            case "TAICHI":
                this.src.add("h.taiChi()");
                break;
            case "BLINK":
                this.src.add("h.blink()");
                break;
            case "WAVE":
                this.src.add("h.wave()");
                break;
            case "WIPEFOREHEAD":
                this.src.add("h.wipeForehead()");
                break;
            default:
                throw new DbcException("Invalid Animation MOVE: " + animation.move);
        }
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt pointLookAt) {
        switch ( pointLookAt.pointLook ) {
            case "0":
                this.src.add("h.pointLookAt('point'");
                break;
            case "1":
                this.src.add("h.pointLookAt('look'");
                break;
            default:
                throw new DbcException("Invalid PointLookAt MODE: " + pointLookAt.pointLook);
        }
        this.src.add(", ", pointLookAt.frame, ", ");

        pointLookAt.pointX.accept(this);
        this.src.add(", ");
        pointLookAt.pointY.accept(this);
        this.src.add(", ");
        pointLookAt.pointZ.accept(this);
        this.src.add(", ");
        pointLookAt.speed.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume setVolume) {
        this.src.add("h.setVolume(");
        setVolume.volume.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume getVolume) {
        this.src.add("h.getVolume()");
        return null;
    }

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "German";
            case ENGLISH:
                return "English";
            case FRENCH:
                return "French";
            case JAPANESE:
                return "Japanese";
            case CHINESE:
                return "Chinese";
            case SPANISH:
                return "Spanish";
            case KOREAN:
                return "Korean";
            case ITALIAN:
                return "Italian";
            case DUTCH:
                return "Dutch";
            case FINNISH:
                return "Finnish";
            case POLISH:
                return "Polish";
            case RUSSIAN:
                return "Russian";
            case TURKISH:
                return "Turkish";
            case ARABIC:
                return "Arabic";
            case CZECH:
                return "Czech";
            case PORTUGUESE:
                return "Portuguese";
            case BRAZILIAN:
                return "Brazilian";
            case SWEDISH:
                return "Swedish";
            case DANISH:
                return "Danish";
            case NORWEGIAN:
                return "Norwegian";
            case GREEK:
                return "Greek";
            default:
                return "English";
        }
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        this.src.add("h.setLanguage(\"");
        this.src.add(getLanguageString(setLanguageAction.language));
        this.src.add("\")");
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage getLanguage) {
        this.src.add("h.getLanguage()");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        this.src.add("h.say(");
        if ( !sayTextAction.msg.getKind().hasName("STRING_CONST") ) {
            this.src.add("str(");
            sayTextAction.msg.accept(this);
            this.src.add(")");
        } else {
            sayTextAction.msg.accept(this);
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        this.src.add("h.say(");
        if ( !sayTextAction.msg.getKind().hasName("STRING_CONST") ) {
            this.src.add("str(");
            sayTextAction.msg.accept(this);
            this.src.add(")");
        } else {
            sayTextAction.msg.accept(this);
        }
        this.src.add(",");
        sayTextAction.speed.accept(this);
        this.src.add(",");
        sayTextAction.pitch.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile playFile) {
        this.src.add("h.playFile(");
        playFile.msg.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds setLeds) {
        this.src.add("h.setLeds(");
        switch ( setLeds.led ) {
            case "ALL":
                this.src.add("\"AllLeds\", ");
                break;
            case "CHEST":
                this.src.add("\"ChestLeds\", ");
                break;
            case "EARS":
                this.src.add("\"EarLeds\", ");
                break;
            case "EYES":
                this.src.add("\"FaceLeds\", ");
                break;
            case "HEAD":
                this.src.add("\"BrainLeds\", ");
                break;
            case "LEFTEAR":
                this.src.add("\"LeftEarLeds\", ");
                break;
            case "LEFTEYE":
                this.src.add("\"LeftFaceLeds\", ");
                break;
            case "LEFTFOOT":
                this.src.add("\"LeftFootLeds\", ");
                break;
            case "RIGHTEAR":
                this.src.add("\"RightEarLeds\", ");
                break;
            case "RIGHTEYE":
                this.src.add("\"RightFaceLeds\", ");
                break;
            case "RIGHTFOOT":
                this.src.add("\"RightFootLeds\", ");
                break;
            default:
                throw new DbcException("Invalid SetLeds LED: " + setLeds.led);
        }
        setLeds.Color.accept(this);
        this.src.add(", 0.1)");
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity setIntensity) {
        this.src.add("h.setIntensity(");
        switch ( setIntensity.led ) {
            case "ALL":
                this.src.add("\"AllLeds\", ");
                break;
            case "CHEST":
                this.src.add("\"ChestLeds\", ");
                break;
            case "EARS":
                this.src.add("\"EarLeds\", ");
                break;
            case "EYES":
                this.src.add("\"FaceLeds\", ");
                break;
            case "HEAD":
                this.src.add("\"BrainLeds\", ");
                break;
            case "LEFTEAR":
                this.src.add("\"LeftEarLeds\", ");
                break;
            case "LEFTEYE":
                this.src.add("\"LeftFaceLeds\", ");
                break;
            case "LEFTFOOT":
                this.src.add("\"LeftFootLeds\", ");
                break;
            case "RIGHTEAR":
                this.src.add("\"RightEarLeds\", ");
                break;
            case "RIGHTEYE":
                this.src.add("\"RightFaceLeds\", ");
                break;
            case "RIGHTFOOT":
                this.src.add("\"RightFootLeds\", ");
                break;
            default:
                throw new DbcException("Invalid SetIntensity LED: " + setIntensity.led);
        }
        setIntensity.Intensity.accept(this);
        this.src.add(")");
        return null;
    }

    /*@Override
    public Void visitLedColor(LedColor ledColor) {
        this.src.add(ledColor.getRedChannel(), ", ", ledColor.getGreenChannel(), ", ", ledColor.getBlueChannel(), ", 255");
        return null;
    }*/

    @Override
    public Void visitLedOff(LedOff ledOff) {
        this.src.add("h.ledOff(");
        switch ( ledOff.led ) {
            case "ALL":
                this.src.add("\"AllLeds\"");
                break;
            case "CHEST":
                this.src.add("\"ChestLeds\"");
                break;
            case "EARS":
                this.src.add("\"EarLeds\"");
                break;
            case "EYES":
                this.src.add("\"FaceLeds\"");
                break;
            case "HEAD":
                this.src.add("\"BrainLeds\"");
                break;
            case "LEFTEAR":
                this.src.add("\"LeftEarLeds\"");
                break;
            case "LEFTEYE":
                this.src.add("\"LeftFaceLeds\"");
                break;
            case "LEFTFOOT":
                this.src.add("\"LeftFootLeds\"");
                break;
            case "RIGHTEAR":
                this.src.add("\"RightEarLeds\"");
                break;
            case "RIGHTEYE":
                this.src.add("\"RightFaceLeds\"");
                break;
            case "RIGHTFOOT":
                this.src.add("\"RightFootLeds\"");
                break;
            default:
                throw new DbcException("Invalid LedOff LED: " + ledOff.led);
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitLedReset(LedReset ledReset) {
        this.src.add("h.ledReset(");
        switch ( ledReset.led ) {
            case "ALL":
                this.src.add("\"AllLeds\"");
                break;
            case "CHEST":
                this.src.add("\"ChestLeds\"");
                break;
            case "EARS":
                this.src.add("\"EarLeds\"");
                break;
            case "EYES":
                this.src.add("\"FaceLeds\"");
                break;
            case "HEAD":
                this.src.add("\"BrainLeds\"");
                break;
            case "LEFTEAR":
                this.src.add("\"LeftEarLeds\"");
                break;
            case "LEFTEYE":
                this.src.add("\"LeftFaceLeds\"");
                break;
            case "LEFTFOOT":
                this.src.add("\"LeftFootLeds\"");
                break;
            case "RIGHTEAR":
                this.src.add("\"RightEarLeds\"");
                break;
            case "RIGHTEYE":
                this.src.add("\"RightFaceLeds\"");
                break;
            case "RIGHTFOOT":
                this.src.add("\"RightFootLeds\"");
                break;
            default:
                throw new DbcException("Invalid LedReset LED:" + ledReset.led);
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration randomEyesDuration) {
        this.src.add("h.randomEyes(");
        randomEyesDuration.duration.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration rastaDuration) {
        this.src.add("h.rasta(");
        rastaDuration.duration.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.src.add("h.touchsensors(");
        this.src.add(getEnumCode(touchSensor.getUserDefinedPort()));
        this.src.add(", ");
        this.src.add(getEnumCode(touchSensor.getSlot()));
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor sonar) {
        this.src.add("h.ultrasonic()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyrometer) {
        this.src.add("h.gyrometer(");
        this.src.add(getEnumCode(gyrometer.getSlot()));
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometer) {
        this.src.add("h.accelerometer(");
        this.src.add(getEnumCode(accelerometer.getUserDefinedPort()));
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor fsr) {
        this.src.add("h.fsr(");
        this.src.add(getEnumCode(fsr.getUserDefinedPort()));
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectedMark) {
        this.src.add("h.getDetectedMark");
        if ( detectedMark.getMode().equals(SC.IDALL) ) {
            this.src.add("s");
        }
        this.src.add("()");
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture takePicture) {
        this.src.add("h.takePicture(");
        switch ( takePicture.camera ) {
            case "0":
                this.src.add("\"Top\", ");
                break;
            case "1":
                this.src.add("\"Bottom\", ");
                break;
            default:
                throw new DbcException("Invalid TakePicture MODE: " + takePicture.camera);
        }
        takePicture.pictureName.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo recordVideo) {
        this.src.add("h.recordVideo(", recordVideo.resolution, ", ");
        switch ( recordVideo.camera ) {
            case "0":
                this.src.add("\"Top\", ");
                break;
            case "1":
                this.src.add("\"Bottom\", ");
                break;
            default:
                throw new DbcException("Invalid RecordVideo CAMERA: " + recordVideo.camera);
        }
        recordVideo.duration.accept(this);
        this.src.add(", ");
        recordVideo.videoName.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace learnFace) {
        this.src.add("faceRecognitionModule.learnFace(");
        learnFace.faceName.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace forgetFace) {
        this.src.add("faceRecognitionModule.forgetFace(");
        forgetFace.faceName.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDetectFaceSensor(DetectFaceSensor detectFace) {
        this.src.add("faceRecognitionModule.detectFace");
        if ( detectFace.getMode().equals(SC.NAMEALL) ) {
            this.src.add("s");
        }
        this.src.add("()");
        return null;
    }

    @Override
    public Void visitElectricCurrentSensor(ElectricCurrentSensor electricCurrent) {
        String side_axis = electricCurrent.getSlot().toString().toLowerCase();
        String[] slots = side_axis.split("_");

        String portType = electricCurrent.getUserDefinedPort();
        String port = portType.toString().toLowerCase();
        port = StringUtils.capitalize(port);

        String side = extractSideFromSlot(slots, portType);
        String axis1 = extractAxis1FromSlot(slots);
        String axis2 = extractAxis2FromSlot(slots);
        String jointActuator = constructJointActuator(portType, port, side, axis1, axis2);

        this.src.add("h.getElectricCurrent('");
        this.src.add(jointActuator);
        this.src.add("')");
        return null;
    }

    private String constructJointActuator(String portType, String port, String side, String axis1, String axis2) {
        if ( portType.equals("HEAD") ) {
            return port + side;
        } else {
            return side + port + axis1 + axis2;
        }
    }

    private String extractSideFromSlot(String[] slots, String portType) {
        String side;
        side = Character.toString(slots[0].toUpperCase().charAt(0));
        if ( portType.equals("HEAD") ) {
            side = StringUtils.capitalize(slots[0].toLowerCase());
        }
        return side;
    }

    private String extractAxis1FromSlot(String[] slots) {
        if ( slots.length > 1 ) {
            return StringUtils.capitalize(slots[1].toLowerCase());
        }
        return "";
    }

    private String extractAxis2FromSlot(String[] slots) {
        if ( slots.length == 3 ) {
            return StringUtils.capitalize(slots[2].toLowerCase());
        }
        return "";
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.src.add("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.src.add("import math");
        nlIndent();
        this.src.add("import time");
        nlIndent();
        this.src.add("import random");
        nlIndent();
        this.src.add("from roberta import Hal");
        nlIndent();
        this.src.add("h = Hal()");
        nlIndent();
        generateSensors();

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
        this.src.add("h.say(\"Error!\" + str(e))");
        decrIndentation();
        nlIndent();
        this.src.add("finally:");
        incrIndentation();
        nlIndent();
        removeSensors();
        this.src.add("h.myBroker.shutdown()");
        decrIndentation();
        decrIndentation();
        nlIndent();

        super.generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitWalkAsync(WalkAsync walkAsync) {
        this.src.add("h.walkAsync(");
        walkAsync.XSpeed.accept(this);
        this.src.add(", ");
        walkAsync.YSpeed.accept(this);
        this.src.add(", ");
        walkAsync.ZSpeed.accept(this);
        this.src.add(")");
        return null;
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            switch ( usedSensor.getType() ) {
                case SC.ULTRASONIC:
                    this.src.add("h.sonar.subscribe(\"OpenRobertaApp\")");
                    nlIndent();
                    break;
                case SC.DETECT_MARK:
                    this.src.add("h.mark.subscribe(\"RobertaLab\", 500, 0.0)");
                    nlIndent();
                    break;
                case SC.NAO_FACE:
                    nlIndent();
                    this.src.add("from roberta import FaceRecognitionModule");
                    nlIndent();
                    this.src.add("faceRecognitionModule = FaceRecognitionModule(\"faceRecognitionModule\")");
                    nlIndent();
                    break;
                case SC.NAO_SPEECH:
                    nlIndent();
                    this.src.add("from roberta import SpeechRecognitionModule");
                    nlIndent();
                    this.src.add("speechRecognitionModule = SpeechRecognitionModule(\"speechRecognitionModule\")");
                    nlIndent();
                    this.src.add("speechRecognitionModule.pauseASR()");
                    nlIndent();
                    break;
                case SC.COLOR:
                case SC.INFRARED:
                case SC.LIGHT:
                case SC.COMPASS:
                case SC.SOUND:
                case SC.TOUCH:
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    break;
                default:
                    throw new DbcException("Sensor is not supported!" + usedSensor.getType().toString());
            }
        }
    }

    private void removeSensors() {
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            String sensorType = usedSensor.getType();
            switch ( sensorType ) {
                case BlocklyConstants.COLOR:
                    break;
                case BlocklyConstants.INFRARED:
                    break;
                case BlocklyConstants.ULTRASONIC:
                    this.src.add("h.sonar.unsubscribe(\"OpenRobertaApp\")");
                    nlIndent();
                    break;
                case BlocklyConstants.DETECT_MARK:
                    this.src.add("h.mark.unsubscribe(\"RobertaLab\")");
                    nlIndent();
                    break;
                case BlocklyConstants.NAO_FACE:
                    this.src.add("faceRecognitionModule.unsubscribe()");
                    nlIndent();
                    break;
                case BlocklyConstants.NAO_SPEECH:
                    this.src.add("speechRecognitionModule.unsubscribe()");
                    nlIndent();
                    break;
                case BlocklyConstants.LIGHT:
                case BlocklyConstants.COMPASS:
                case BlocklyConstants.SOUND:
                case BlocklyConstants.TOUCH:
                case BlocklyConstants.GYRO:
                case BlocklyConstants.ACCELEROMETER:
                    break;
                default:
                    throw new DbcException("Sensor is not supported " + sensorType);
            }
        }
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord recognizeWord) {
        this.src.add("speechRecognitionModule.recognizeWordFromDictionary(");
        recognizeWord.vocabulary.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation naoMarkInformation) {
        this.src.add("h.getNaoMarkInformation(");
        naoMarkInformation.naoMarkId.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDetectedFaceInformation(DetectedFaceInformation detectedFaceInformation) {
        this.src.add("faceRecognitionModule.getFaceInformation(");
        detectedFaceInformation.faceName.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add(colorConst.getHexIntAsString());
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