package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.nao.Camera;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
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
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFaceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedFaceInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrentSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMarkInformation;
import de.fhg.iais.roberta.syntax.sensor.nao.RecognizeWord;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.collect.NaoUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class NaoPythonVisitor extends AbstractPythonVisitor implements INaoVisitor<Void> {

    protected Set<UsedSensor> usedSensors;

    protected ILanguage language;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private NaoPythonVisitor(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation, ILanguage language) {
        super(programPhrases, indentation);

        NaoUsedHardwareCollectorVisitor checker = new NaoUsedHardwareCollectorVisitor(programPhrases, brickConfiguration);
        this.usedSensors = checker.getUsedSensors();
        this.usedGlobalVarInFunctions = checker.getMarkedVariablesAsGlobal();
        this.isProgramEmpty = checker.isProgramEmpty();
        this.loopsLabels = checker.getloopsLabelContainer();

        this.language = language;
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping, ILanguage language) {
        Assert.notNull(brickConfiguration);

        NaoPythonVisitor astVisitor = new NaoPythonVisitor(brickConfiguration, phrasesSet, 0, language);
        astVisitor.generateCode(withWrapping);

        return astVisitor.sb.toString();
    }

    public static String generate(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        NaoPythonVisitor astVisitor = new NaoPythonVisitor(null, phrasesSet, 0, null);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        this.sb.append("BlocklyMethods.rgb2hex(");
        rgbColor.getR().visit(this);
        this.sb.append(", ");
        rgbColor.getG().visit(this);
        this.sb.append(", ");
        rgbColor.getB().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case ARRAY_STRING:
                this.sb.append("BlocklyMethods.createListWith(\"\")");
                break;
            case STRING:
                this.sb.append("\"\"");
                break;
            case BOOLEAN:
                this.sb.append("True");
                break;
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            case COLOR:
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("h.wait(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("h.wait(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        generateUserDefinedMethods();
        this.sb.append("\n\ndef run():");
        incrIndentation();
        if ( mainTask.getDebug().equals("TRUE") ) {
            nlIndent();
            this.sb.append("h.setAutonomousLife('ON')");
        } else {
            nlIndent();
            this.sb.append("h.setAutonomousLife('OFF')");
        }
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
                this.sb.append(vd.getName());
            }
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        this.sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(getEnumCode(where1));
        if ( (where1 == IndexLocation.FROM_START) || (where1 == IndexLocation.FROM_END) ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(getEnumCode(where2));
        if ( (where2 == IndexLocation.FROM_START) || (where2 == IndexLocation.FROM_END) ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                this.sb.append("BlocklyMethods.findFirst( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("BlocklyMethods.findLast( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LIST_LENGTH:
                this.sb.append("BlocklyMethods.length( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("BlocklyMethods.isEmpty( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb.append("[]");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("BlocklyMethods.createListWith(");
        listCreate.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        this.sb.append("BlocklyMethods.listsGetIndex(");
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getElementOperation()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getLocation()));
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        this.sb.append("BlocklyMethods.listsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getElementOperation()));
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getLocation()));
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("BlocklyMethods.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case ODD:
                this.sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case PRIME:
                this.sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case NEGATIVE:
                this.sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case DIVISIBLE_BY:
                this.sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                this.sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                this.sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                this.sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                this.sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                this.sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                this.sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                this.sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("BlocklyMethods.textJoin(");
        textJoinFunct.getParam().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetMode(SetMode<Void> setMode) {
        this.sb.append("h.mode(");
        switch ( setMode.getModus() ) {
            case ACTIVE:
                this.sb.append("1)");
                break;
            case REST:
                this.sb.append("2)");
                break;
            case SIT:
                this.sb.append("3)");
                break;
        }
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        this.sb.append("h.applyPosture(");
        switch ( applyPosture.getPosture() ) {
            case STAND:
                this.sb.append("\"Stand\")");
                break;
            case STANDINIT:
                this.sb.append("\"StandInit\")");
                break;
            case STANDZERO:
                this.sb.append("\"StandZero\")");
                break;
            case SITRELAX:
                this.sb.append("\"SitRelax\")");
                break;
            case SIT:
                this.sb.append("\"Sit\")");
                break;
            case LYINGBELLY:
                this.sb.append("\"LyingBelly\")");
                break;
            case LYINGBACK:
                this.sb.append("\"LyingBack\")");
                break;
            case CROUCH:
                this.sb.append("\"Crouch\")");
                break;
            case REST:
                this.sb.append("\"Rest\")");
                break;
        }
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness<Void> setStiffness) {
        this.sb.append("h.stiffness(");
        switch ( setStiffness.getBodyPart() ) {
            case BODY:
                this.sb.append("\"Body\"");
                break;
            case HEAD:
                this.sb.append("\"Head\"");
                break;
            case ARMS:
                this.sb.append("\"Arms\"");
                break;
            case LEFTARM:
                this.sb.append("\"LArm\"");
                break;
            case RIGHTARM:
                this.sb.append("\"RArm\"");
                break;
            case LEGS:
                this.sb.append("\"Legs\"");
                break;
            case LEFTLEG:
                this.sb.append("\"LLeg\"");
                break;
            case RIHTLEG:
                this.sb.append("\"RLeg\"");
                break;
        }

        switch ( setStiffness.getOnOff() ) {
            case ON:
                this.sb.append(", 1)");
                break;
            case OFF:
                this.sb.append(", 2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitAutonomous(Autonomous<Void> autonomous) {
        this.sb.append("h.setAutonomousLife(" + getEnumCode(autonomous.getOnOff()).toUpperCase() + ")");
        return null;
    }

    @Override
    public Void visitHand(Hand<Void> hand) {
        this.sb.append("h.hand(");
        switch ( hand.getTurnDirection() ) {
            case LEFT:
                this.sb.append("\"LHand\"");
                break;
            case RIGHT:
                this.sb.append("\"RHand\"");
                break;
        }

        switch ( hand.getModus() ) {
            case ACTIVE:
                this.sb.append(", 1)");
                break;
            case REST:
                this.sb.append(", 2)");
                break;
            case SIT:
                this.sb.append(", 3)");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        this.sb.append("h.moveJoint(");
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
        moveJoint.getDegrees().visit(this);
        this.sb.append(", ");
        switch ( moveJoint.getRelativeAbsolute() ) {
            case ABSOLUTE:
                this.sb.append("1)");
                break;
            case RELATIVE:
                this.sb.append("2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        this.sb.append("h.walk(");
        if ( walkDistance.getWalkDirection() == DriveDirection.BACKWARD ) {
            this.sb.append("-");
        }
        walkDistance.getDistanceToWalk().visit(this);
        this.sb.append(", 0, 0)");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        this.sb.append("h.walk(0, 0,");
        if ( turnDegrees.getTurnDirection() == TurnDirection.RIGHT ) {
            this.sb.append("-");
        }
        turnDegrees.getDegreesToTurn().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitWalkTo(WalkTo<Void> walkTo) {
        this.sb.append("h.walk(");
        walkTo.getWalkToX().visit(this);
        this.sb.append(",");
        walkTo.getWalkToY().visit(this);
        this.sb.append(",");
        walkTo.getWalkToTheta().visit(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitStop(Stop<Void> stop) {
        this.sb.append("h.stop()");

        return null;
    }

    @Override
    public Void visitAnimation(Animation<Void> animation) {
        switch ( animation.getMove() ) {
            case TAICHI:
                this.sb.append("h.taiChi()");
                break;
            case BLINK:
                this.sb.append("h.blink()");
                break;
            case WAVE:
                this.sb.append("h.wave()");
                break;
            case WIPEFOREHEAD:
                this.sb.append("h.wipeForehead()");
                break;
        }
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        this.sb.append("h.pointLookAt(" + getEnumCode(pointLookAt.getPointLook()));
        this.sb.append(", " + pointLookAt.getFrame().getValues()[0] + ", ");

        pointLookAt.getpointX().visit(this);
        this.sb.append(", ");
        pointLookAt.getpointY().visit(this);
        this.sb.append(", ");
        pointLookAt.getpointZ().visit(this);
        this.sb.append(", ");
        pointLookAt.getSpeed().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetVolume(SetVolume<Void> setVolume) {
        this.sb.append("h.setVolume(");
        setVolume.getVolume().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitGetVolume(GetVolume<Void> getVolume) {
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
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        this.sb.append("h.setLanguage(\"");
        this.sb.append(getLanguageString(setLanguageAction.getLanguage()));
        this.sb.append("\")");
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        this.sb.append("h.getLanguage()");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        this.sb.append("h.say(");
        if ( !sayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            sayTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            sayTextAction.getMsg().visit(this);
        }
        BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        if ( !(sayTextAction.getSpeed().getKind().equals(emptyBlock) && sayTextAction.getPitch().getKind().equals(emptyBlock)) ) {
            this.sb.append(",");
            sayTextAction.getSpeed().visit(this);
            this.sb.append(",");
            sayTextAction.getPitch().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayFile(PlayFile<Void> playFile) {
        this.sb.append("h.playFile(");
        playFile.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        this.sb.append("h.setLeds(");
        switch ( setLeds.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\", ");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\", ");
                break;
            case EARS:
                this.sb.append("\"EarLeds\", ");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\", ");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\", ");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\", ");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\", ");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\", ");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\", ");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\", ");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\", ");
                break;
        }
        setLeds.getColor().visit(this);
        this.sb.append(", 0.1)");
        return null;
    }

    @Override
    public Void visitSetIntensity(SetIntensity<Void> setIntensity) {
        this.sb.append("h.setIntensity(");
        switch ( setIntensity.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\", ");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\", ");
                break;
            case EARS:
                this.sb.append("\"EarLeds\", ");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\", ");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\", ");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\", ");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\", ");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\", ");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\", ");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\", ");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\", ");
                break;
        }
        setIntensity.getIntensity().visit(this);
        this.sb.append(")");
        return null;
    }

    /*@Override
    public Void visitLedColor(LedColor<Void> ledColor) {
        this.sb.append(ledColor.getRedChannel() + ", " + ledColor.getGreenChannel() + ", " + ledColor.getBlueChannel() + ", 255");
        return null;
    }*/

    @Override
    public Void visitLedOff(LedOff<Void> ledOff) {
        this.sb.append("h.ledOff(");
        switch ( ledOff.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\"");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\"");
                break;
            case EARS:
                this.sb.append("\"EarLeds\"");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\"");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\"");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\"");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\"");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\"");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\"");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\"");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\"");
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedReset(LedReset<Void> ledReset) {
        this.sb.append("h.ledReset(");
        switch ( ledReset.getLed() ) {
            case ALL:
                this.sb.append("\"AllLeds\"");
                break;
            case CHEST:
                this.sb.append("\"ChestLeds\"");
                break;
            case EARS:
                this.sb.append("\"EarLeds\"");
                break;
            case EYES:
                this.sb.append("\"FaceLeds\"");
                break;
            case HEAD:
                this.sb.append("\"BrainLeds\"");
                break;
            case LEFTEAR:
                this.sb.append("\"LeftEarLeds\"");
                break;
            case LEFTEYE:
                this.sb.append("\"LeftFaceLeds\"");
                break;
            case LEFTFOOT:
                this.sb.append("\"LeftFootLeds\"");
                break;
            case RIGHTEAR:
                this.sb.append("\"RightEarLeds\"");
                break;
            case RIGHTEYE:
                this.sb.append("\"RightFaceLeds\"");
                break;
            case RIGHTFOOT:
                this.sb.append("\"RightFootLeds\"");
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRandomEyesDuration(RandomEyesDuration<Void> randomEyesDuration) {
        this.sb.append("h.randomEyes(");
        randomEyesDuration.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRastaDuration(RastaDuration<Void> rastaDuration) {
        this.sb.append("h.rasta(");
        rastaDuration.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("h.touchsensors(");
        this.sb.append(getEnumCode(touchSensor.getPort()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(touchSensor.getSlot()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> sonar) {
        this.sb.append("h.ultrasonic()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyrometer) {
        this.sb.append("h.gyrometer(");
        this.sb.append(getEnumCode((gyrometer.getPort())));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometer) {
        this.sb.append("h.accelerometer(");
        this.sb.append(getEnumCode(accelerometer.getPort()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitFsrSensor(FsrSensor<Void> fsr) {
        this.sb.append("h.fsr(");
        this.sb.append(getEnumCode(fsr.getPort()));
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitNaoMark(DetectMarkSensor<Void> detectedMark) {
        this.sb.append("h.getDetectedMark");
        if ( detectedMark.getMode().equals(SC.IDALL) ) {
            this.sb.append("s");
        }
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitTakePicture(TakePicture<Void> takePicture) {
        this.sb.append("h.takePicture(");
        if ( takePicture.getCamera() == Camera.TOP ) {
            this.sb.append("\"Top\", ");
        } else if ( takePicture.getCamera() == Camera.BOTTOM ) {
            this.sb.append("\"Bottom\", ");
        }
        takePicture.getPictureName().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        this.sb.append("h.recordVideo(");
        switch ( recordVideo.getResolution() ) {
            case LOW:
                this.sb.append("0, ");
                break;
            case MED:
                this.sb.append("1, ");
                break;
            case HIGH:
                this.sb.append("2, ");
                break;
        }
        switch ( recordVideo.getCamera() ) {
            case TOP:
                this.sb.append("\"Top\", ");
                break;
            case BOTTOM:
                this.sb.append("\"Bottom\", ");
                break;
        }
        recordVideo.getDuration().visit(this);
        this.sb.append(", ");
        recordVideo.getVideoName().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        this.sb.append("faceRecognitionModule.learnFace(");
        learnFace.getFaceName().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        this.sb.append("faceRecognitionModule.forgetFace(");
        forgetFace.getFaceName().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDetectFace(DetectFaceSensor<Void> detectFace) {
        this.sb.append("faceRecognitionModule.detectFace");
        if ( detectFace.getMode().equals(SC.NAMEALL) ) {
            this.sb.append("s");
        }
        this.sb.append("()");
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return sensorGetSample.getSensor().visit(this);
    }

    @Override
    public Void visitElectricCurrent(ElectricCurrentSensor<Void> electricCurrent) {
        String side_axis = electricCurrent.getSlot().toString().toLowerCase();
        String[] slots = side_axis.split("_");

        String portType = electricCurrent.getPort();
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
        this.sb.append("#!/usr/bin/python\n\n");
        this.sb.append("import math\n");
        this.sb.append("import time\n");
        this.sb.append("from roberta import Hal\n");
        this.sb.append("from roberta import BlocklyMethods\n");
        this.sb.append("h = Hal()\n");
        generateSensors();

        if ( !this.loopsLabels.isEmpty() ) {
            nlIndent();
            this.sb.append("class BreakOutOfALoop(Exception): pass\n");
            this.sb.append("class ContinueLoop(Exception): pass\n\n");
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("\n\n");
        this.sb.append("def main():\n");
        this.sb.append(this.INDENT).append("try:\n");
        this.sb.append(this.INDENT).append(this.INDENT).append("run()\n");
        this.sb.append(this.INDENT).append("except Exception as e:\n");
        this.sb.append(this.INDENT).append(this.INDENT).append("h.say(\"Error!\" + str(e))\n");
        this.sb.append(this.INDENT).append("finally:\n");
        removeSensors();

        this.sb.append(this.INDENT).append(this.INDENT).append("h.myBroker.shutdown()");

        this.sb.append("\n\n");
        this.sb.append("if __name__ == \"__main__\":\n");
        this.sb.append(this.INDENT).append("main()");
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitWalkAsync(WalkAsync<Void> walkAsync) {
        this.sb.append("h.walkAsync(");
        walkAsync.getXSpeed().visit(this);
        this.sb.append(", ");
        walkAsync.getYSpeed().visit(this);
        this.sb.append(", ");
        walkAsync.getZSpeed().visit(this);
        this.sb.append(")");
        return null;
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.usedSensors ) {

            switch ( usedSensor.getType() ) {
                case SC.ULTRASONIC:
                    this.sb.append("h.sonar.subscribe(\"OpenRobertaApp\")\n");
                    break;
                case SC.DETECT_MARK:
                    this.sb.append("h.mark.subscribe(\"RobertaLab\", 500, 0.0)\n");
                    break;
                case SC.NAO_FACE:
                    this.sb.append("\nfrom roberta import FaceRecognitionModule\n");
                    this.sb.append("faceRecognitionModule = FaceRecognitionModule(\"faceRecognitionModule\")\n");
                    break;
                case SC.NAO_SPEECH:
                    this.sb.append("\nfrom roberta import SpeechRecognitionModule\n");
                    this.sb.append("speechRecognitionModule = SpeechRecognitionModule(\"speechRecognitionModule\")\n");
                    this.sb.append("speechRecognitionModule.pauseASR()\n");
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
        for ( UsedSensor usedSensor : this.usedSensors ) {
            String sensorType = usedSensor.getType();
            switch ( sensorType ) {
                case BlocklyConstants.COLOR:
                    break;
                case BlocklyConstants.INFRARED:
                    break;
                case BlocklyConstants.ULTRASONIC:
                    this.sb.append(this.INDENT).append(this.INDENT).append("h.sonar.unsubscribe(\"OpenRobertaApp\")\n");
                    break;
                case BlocklyConstants.DETECT_MARK:
                    this.sb.append(this.INDENT).append(this.INDENT).append("h.mark.unsubscribe(\"RobertaLab\")\n");
                    break;
                case BlocklyConstants.NAO_FACE:
                    this.sb.append(this.INDENT).append(this.INDENT).append("faceRecognitionModule.unsubscribe()\n");
                    break;
                case BlocklyConstants.NAO_SPEECH:
                    this.sb.append(this.INDENT).append(this.INDENT).append("speechRecognitionModule.unsubscribe()\n");
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
    public Void visitRecognizeWord(RecognizeWord<Void> recognizeWord) {
        this.sb.append("speechRecognitionModule.recognizeWordFromDictionary(");
        recognizeWord.getVocabulary().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitNaoMarkInformation(NaoMarkInformation<Void> naoMarkInformation) {
        this.sb.append("h.getNaoMarkInformation(");
        naoMarkInformation.getNaoMarkId().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDetecedFaceInformation(DetectedFaceInformation<Void> detectedFaceInformation) {
        this.sb.append("faceRecognitionModule.getFaceInformation(");
        detectedFaceInformation.getFaceName().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append(colorConst.getHexIntAsString());
        return null;
    }

}