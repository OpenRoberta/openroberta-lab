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
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
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
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.sb.append("h.wait(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("h.wait(");
        waitTimeStmt.time.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        StmtList variables = mainTask.variables;
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        if ( mainTask.debug.equals("TRUE") ) {
            nlIndent();
            this.sb.append("h.setAutonomousLife('ON')");
        } else {
            nlIndent();
            this.sb.append("h.setAutonomousLife('OFF')");
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
    public Void visitSetMode(SetMode setMode) {
        this.sb.append("h.mode(");
        switch ( setMode.modus ) {
            case "OPEN":
                this.sb.append("1)");
                break;
            case "CLOSE":
                this.sb.append("2)");
                break;
            case "SIT":
                this.sb.append("3)");
                break;
            default:
                throw new DbcException("Invalid SetMode DIRECTION: " + setMode.modus);
        }
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture applyPosture) {
        this.sb.append("h.applyPosture(");
        switch ( applyPosture.posture ) {
            case "STAND":
                this.sb.append("\"Stand\")");
                break;
            case "STANDINIT":
                this.sb.append("\"StandInit\")");
                break;
            case "STANDZERO":
                this.sb.append("\"StandZero\")");
                break;
            case "SITRELAX":
                this.sb.append("\"SitRelax\")");
                break;
            case "SIT":
                this.sb.append("\"Sit\")");
                break;
            case "LYINGBELLY":
                this.sb.append("\"LyingBelly\")");
                break;
            case "LYINGBACK":
                this.sb.append("\"LyingBack\")");
                break;
            case "CROUCH":
                this.sb.append("\"Crouch\")");
                break;
            case "REST":
                this.sb.append("\"Rest\")");
                break;
            default:
                throw new DbcException("Invalid ApplyPosture DIRECTION: " + applyPosture.posture);
        }
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness setStiffness) {
        this.sb.append("h.stiffness(");
        switch ( setStiffness.bodyPart ) {
            case "BODY":
                this.sb.append("\"Body\"");
                break;
            case "HEAD":
                this.sb.append("\"Head\"");
                break;
            case "ARMS":
                this.sb.append("\"Arms\"");
                break;
            case "LEFTARM":
                this.sb.append("\"LArm\"");
                break;
            case "RIGHTARM":
                this.sb.append("\"RArm\"");
                break;
            case "LEGS":
                this.sb.append("\"Legs\"");
                break;
            case "LEFTLEG":
                this.sb.append("\"LLeg\"");
                break;
            case "RIHTLEG":
                this.sb.append("\"RLeg\"");
                break;
            default:
                throw new DbcException("Invalid SetStiffness PART: " + setStiffness.bodyPart);
        }

        switch ( setStiffness.onOff ) {
            case ON:
                this.sb.append(", 1)");
                break;
            case OFF:
                this.sb.append(", 2)");
                break;
            default:
                throw new DbcException("Invalid SetStiffness MODE: " + setStiffness.onOff);
        }
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous autonomous) {
        this.sb.append("h.setAutonomousLife(" + getEnumCode(autonomous.onOff).toUpperCase() + ")");
        return null;
    }

    @Override
    public Void visitHand(Hand hand) {
        this.sb.append("h.hand(");
        switch ( hand.turnDirection ) {
            case LEFT:
                this.sb.append("\"LHand\"");
                break;
            case RIGHT:
                this.sb.append("\"RHand\"");
                break;
            default:
                throw new DbcException("Invalid Hand SIDE: " + hand.turnDirection);
        }

        switch ( hand.modus ) {
            case "OPEN":
                this.sb.append(", 1)");
                break;
            case "CLOSE":
                this.sb.append(", 2)");
                break;
            case "SIT":
                this.sb.append(", 3)");
                break;
            default:
                throw new DbcException("Invalid Hand MODE: " + hand.modus);
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint moveJoint) {
        this.sb.append("h.moveJoint(");
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
                throw new DbcException("Invalid MoveJoint JOINT: " + moveJoint.joint);
        }
        this.sb.append(", ");
        moveJoint.degrees.accept(this);
        this.sb.append(", ");
        switch ( moveJoint.relativeAbsolute ) {
            case "ABSOLUTE":
                this.sb.append("1)");
                break;
            case "RELATIVE":
                this.sb.append("2)");
                break;
            default:
                throw new DbcException("Invalid MoveJoint MODE: " + moveJoint.relativeAbsolute);
        }
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance walkDistance) {
        this.sb.append("h.walk(");
        if ( walkDistance.walkDirection == DriveDirection.BACKWARD ) {
            this.sb.append("-");
        }
        walkDistance.distanceToWalk.accept(this);
        this.sb.append(", 0, 0)");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees turnDegrees) {
        this.sb.append("h.walk(0, 0,");
        if ( turnDegrees.turnDirection == TurnDirection.RIGHT ) {
            this.sb.append("-");
        }
        turnDegrees.degreesToTurn.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo walkTo) {
        this.sb.append("h.walk(");
        walkTo.walkToX.accept(this);
        this.sb.append(",");
        walkTo.walkToY.accept(this);
        this.sb.append(",");
        walkTo.walkToTheta.accept(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitStop(Stop stop) {
        this.sb.append("h.stop()");

        return null;
    }

    @Override
    public Void visitAnimation(Animation animation) {
        switch ( animation.move ) {
            case "TAICHI":
                this.sb.append("h.taiChi()");
                break;
            case "BLINK":
                this.sb.append("h.blink()");
                break;
            case "WAVE":
                this.sb.append("h.wave()");
                break;
            case "WIPEFOREHEAD":
                this.sb.append("h.wipeForehead()");
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
                this.sb.append("h.pointLookAt('point'");
                break;
            case "1":
                this.sb.append("h.pointLookAt('look'");
                break;
            default:
                throw new DbcException("Invalid PointLookAt MODE: " + pointLookAt.pointLook);
        }
        this.sb.append(", " + pointLookAt.frame + ", ");

        pointLookAt.pointX.accept(this);
        this.sb.append(", ");
        pointLookAt.pointY.accept(this);
        this.sb.append(", ");
        pointLookAt.pointZ.accept(this);
        this.sb.append(", ");
        pointLookAt.speed.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume setVolume) {
        this.sb.append("h.setVolume(");
        setVolume.volume.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume getVolume) {
        this.sb.append("h.getVolume()");
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
        this.sb.append("h.setLanguage(\"");
        this.sb.append(getLanguageString(setLanguageAction.language));
        this.sb.append("\")");
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage getLanguage) {
        this.sb.append("h.getLanguage()");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        this.sb.append("h.say(");
        if ( !sayTextAction.msg.getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            sayTextAction.msg.accept(this);
            this.sb.append(")");
        } else {
            sayTextAction.msg.accept(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        this.sb.append("h.say(");
        if ( !sayTextAction.msg.getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            sayTextAction.msg.accept(this);
            this.sb.append(")");
        } else {
            sayTextAction.msg.accept(this);
        }
        this.sb.append(",");
        sayTextAction.speed.accept(this);
        this.sb.append(",");
        sayTextAction.pitch.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile playFile) {
        this.sb.append("h.playFile(");
        playFile.msg.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds setLeds) {
        this.sb.append("h.setLeds(");
        switch ( setLeds.led ) {
            case "ALL":
                this.sb.append("\"AllLeds\", ");
                break;
            case "CHEST":
                this.sb.append("\"ChestLeds\", ");
                break;
            case "EARS":
                this.sb.append("\"EarLeds\", ");
                break;
            case "EYES":
                this.sb.append("\"FaceLeds\", ");
                break;
            case "HEAD":
                this.sb.append("\"BrainLeds\", ");
                break;
            case "LEFTEAR":
                this.sb.append("\"LeftEarLeds\", ");
                break;
            case "LEFTEYE":
                this.sb.append("\"LeftFaceLeds\", ");
                break;
            case "LEFTFOOT":
                this.sb.append("\"LeftFootLeds\", ");
                break;
            case "RIGHTEAR":
                this.sb.append("\"RightEarLeds\", ");
                break;
            case "RIGHTEYE":
                this.sb.append("\"RightFaceLeds\", ");
                break;
            case "RIGHTFOOT":
                this.sb.append("\"RightFootLeds\", ");
                break;
            default:
                throw new DbcException("Invalid SetLeds LED: " + setLeds.led);
        }
        setLeds.Color.accept(this);
        this.sb.append(", 0.1)");
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity setIntensity) {
        this.sb.append("h.setIntensity(");
        switch ( setIntensity.led ) {
            case "ALL":
                this.sb.append("\"AllLeds\", ");
                break;
            case "CHEST":
                this.sb.append("\"ChestLeds\", ");
                break;
            case "EARS":
                this.sb.append("\"EarLeds\", ");
                break;
            case "EYES":
                this.sb.append("\"FaceLeds\", ");
                break;
            case "HEAD":
                this.sb.append("\"BrainLeds\", ");
                break;
            case "LEFTEAR":
                this.sb.append("\"LeftEarLeds\", ");
                break;
            case "LEFTEYE":
                this.sb.append("\"LeftFaceLeds\", ");
                break;
            case "LEFTFOOT":
                this.sb.append("\"LeftFootLeds\", ");
                break;
            case "RIGHTEAR":
                this.sb.append("\"RightEarLeds\", ");
                break;
            case "RIGHTEYE":
                this.sb.append("\"RightFaceLeds\", ");
                break;
            case "RIGHTFOOT":
                this.sb.append("\"RightFootLeds\", ");
                break;
            default:
                throw new DbcException("Invalid SetIntensity LED: " + setIntensity.led);
        }
        setIntensity.Intensity.accept(this);
        this.sb.append(")");
        return null;
    }

    /*@Override
    public Void visitLedColor(LedColor ledColor) {
        this.sb.append(ledColor.getRedChannel() + ", " + ledColor.getGreenChannel() + ", " + ledColor.getBlueChannel() + ", 255");
        return null;
    }*/

    @Override
    public Void visitLedOff(LedOff ledOff) {
        this.sb.append("h.ledOff(");
        switch ( ledOff.led ) {
            case "ALL":
                this.sb.append("\"AllLeds\"");
                break;
            case "CHEST":
                this.sb.append("\"ChestLeds\"");
                break;
            case "EARS":
                this.sb.append("\"EarLeds\"");
                break;
            case "EYES":
                this.sb.append("\"FaceLeds\"");
                break;
            case "HEAD":
                this.sb.append("\"BrainLeds\"");
                break;
            case "LEFTEAR":
                this.sb.append("\"LeftEarLeds\"");
                break;
            case "LEFTEYE":
                this.sb.append("\"LeftFaceLeds\"");
                break;
            case "LEFTFOOT":
                this.sb.append("\"LeftFootLeds\"");
                break;
            case "RIGHTEAR":
                this.sb.append("\"RightEarLeds\"");
                break;
            case "RIGHTEYE":
                this.sb.append("\"RightFaceLeds\"");
                break;
            case "RIGHTFOOT":
                this.sb.append("\"RightFootLeds\"");
                break;
            default:
                throw new DbcException("Invalid LedOff LED: " + ledOff.led);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedReset(LedReset ledReset) {
        this.sb.append("h.ledReset(");
        switch ( ledReset.led ) {
            case "ALL":
                this.sb.append("\"AllLeds\"");
                break;
            case "CHEST":
                this.sb.append("\"ChestLeds\"");
                break;
            case "EARS":
                this.sb.append("\"EarLeds\"");
                break;
            case "EYES":
                this.sb.append("\"FaceLeds\"");
                break;
            case "HEAD":
                this.sb.append("\"BrainLeds\"");
                break;
            case "LEFTEAR":
                this.sb.append("\"LeftEarLeds\"");
                break;
            case "LEFTEYE":
                this.sb.append("\"LeftFaceLeds\"");
                break;
            case "LEFTFOOT":
                this.sb.append("\"LeftFootLeds\"");
                break;
            case "RIGHTEAR":
                this.sb.append("\"RightEarLeds\"");
                break;
            case "RIGHTEYE":
                this.sb.append("\"RightFaceLeds\"");
                break;
            case "RIGHTFOOT":
                this.sb.append("\"RightFootLeds\"");
                break;
            default:
                throw new DbcException("Invalid LedReset LED:" + ledReset.led);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration randomEyesDuration) {
        this.sb.append("h.randomEyes(");
        randomEyesDuration.duration.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration rastaDuration) {
        this.sb.append("h.rasta(");
        rastaDuration.duration.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.sb.append("h.touchsensors(");
        this.sb.append(getEnumCode(touchSensor.getUserDefinedPort()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(touchSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor sonar) {
        this.sb.append("h.ultrasonic()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyrometer) {
        this.sb.append("h.gyrometer(");
        this.sb.append(getEnumCode(gyrometer.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometer) {
        this.sb.append("h.accelerometer(");
        this.sb.append(getEnumCode(accelerometer.getUserDefinedPort()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor fsr) {
        this.sb.append("h.fsr(");
        this.sb.append(getEnumCode(fsr.getUserDefinedPort()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectedMark) {
        this.sb.append("h.getDetectedMark");
        if ( detectedMark.getMode().equals(SC.IDALL) ) {
            this.sb.append("s");
        }
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture takePicture) {
        this.sb.append("h.takePicture(");
        switch ( takePicture.camera ) {
            case "0":
                this.sb.append("\"Top\", ");
                break;
            case "1":
                this.sb.append("\"Bottom\", ");
                break;
            default:
                throw new DbcException("Invalid TakePicture MODE: " + takePicture.camera);
        }
        takePicture.pictureName.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo recordVideo) {
        this.sb.append("h.recordVideo(").append(recordVideo.resolution).append(", ");
        switch ( recordVideo.camera ) {
            case "0":
                this.sb.append("\"Top\", ");
                break;
            case "1":
                this.sb.append("\"Bottom\", ");
                break;
            default:
                throw new DbcException("Invalid RecordVideo CAMERA: " + recordVideo.camera);
        }
        recordVideo.duration.accept(this);
        this.sb.append(", ");
        recordVideo.videoName.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace learnFace) {
        this.sb.append("faceRecognitionModule.learnFace(");
        learnFace.faceName.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace forgetFace) {
        this.sb.append("faceRecognitionModule.forgetFace(");
        forgetFace.faceName.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDetectFaceSensor(DetectFaceSensor detectFace) {
        this.sb.append("faceRecognitionModule.detectFace");
        if ( detectFace.getMode().equals(SC.NAMEALL) ) {
            this.sb.append("s");
        }
        this.sb.append("()");
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

        this.sb.append("h.getElectricCurrent('");
        this.sb.append(jointActuator);
        this.sb.append("')");
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
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        this.sb.append("import time");
        nlIndent();
        this.sb.append("import random");
        nlIndent();
        this.sb.append("from roberta import Hal");
        nlIndent();
        this.sb.append("h = Hal()");
        nlIndent();
        generateSensors();

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
        this.sb.append("h.say(\"Error!\" + str(e))");
        decrIndentation();
        nlIndent();
        this.sb.append("finally:");
        incrIndentation();
        nlIndent();
        removeSensors();
        this.sb.append("h.myBroker.shutdown()");
        decrIndentation();
        decrIndentation();
        nlIndent();

        super.generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitConnectConst(ConnectConst connectConst) {
        return null;
    }

    @Override
    public Void visitWalkAsync(WalkAsync walkAsync) {
        this.sb.append("h.walkAsync(");
        walkAsync.XSpeed.accept(this);
        this.sb.append(", ");
        walkAsync.YSpeed.accept(this);
        this.sb.append(", ");
        walkAsync.ZSpeed.accept(this);
        this.sb.append(")");
        return null;
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            switch ( usedSensor.getType() ) {
                case SC.ULTRASONIC:
                    this.sb.append("h.sonar.subscribe(\"OpenRobertaApp\")");
                    nlIndent();
                    break;
                case SC.DETECT_MARK:
                    this.sb.append("h.mark.subscribe(\"RobertaLab\", 500, 0.0)");
                    nlIndent();
                    break;
                case SC.NAO_FACE:
                    nlIndent();
                    this.sb.append("from roberta import FaceRecognitionModule");
                    nlIndent();
                    this.sb.append("faceRecognitionModule = FaceRecognitionModule(\"faceRecognitionModule\")");
                    nlIndent();
                    break;
                case SC.NAO_SPEECH:
                    nlIndent();
                    this.sb.append("from roberta import SpeechRecognitionModule");
                    nlIndent();
                    this.sb.append("speechRecognitionModule = SpeechRecognitionModule(\"speechRecognitionModule\")");
                    nlIndent();
                    this.sb.append("speechRecognitionModule.pauseASR()");
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
                    this.sb.append("h.sonar.unsubscribe(\"OpenRobertaApp\")");
                    nlIndent();
                    break;
                case BlocklyConstants.DETECT_MARK:
                    this.sb.append("h.mark.unsubscribe(\"RobertaLab\")");
                    nlIndent();
                    break;
                case BlocklyConstants.NAO_FACE:
                    this.sb.append("faceRecognitionModule.unsubscribe()");
                    nlIndent();
                    break;
                case BlocklyConstants.NAO_SPEECH:
                    this.sb.append("speechRecognitionModule.unsubscribe()");
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
        this.sb.append("speechRecognitionModule.recognizeWordFromDictionary(");
        recognizeWord.vocabulary.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation naoMarkInformation) {
        this.sb.append("h.getNaoMarkInformation(");
        naoMarkInformation.naoMarkId.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDetectedFaceInformation(DetectedFaceInformation detectedFaceInformation) {
        this.sb.append("faceRecognitionModule.getFaceInformation(");
        detectedFaceInformation.faceName.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.sb.append(colorConst.getHexIntAsString());
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