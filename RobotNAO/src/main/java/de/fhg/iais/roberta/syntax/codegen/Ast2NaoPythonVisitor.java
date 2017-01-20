package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.action.nao.BodyPart;
import de.fhg.iais.roberta.mode.action.nao.Camera;
import de.fhg.iais.roberta.mode.action.nao.Color;
import de.fhg.iais.roberta.mode.action.nao.Frame;
import de.fhg.iais.roberta.mode.action.nao.Joint;
import de.fhg.iais.roberta.mode.action.nao.Language;
import de.fhg.iais.roberta.mode.action.nao.Led;
import de.fhg.iais.roberta.mode.action.nao.Modus;
import de.fhg.iais.roberta.mode.action.nao.Move;
import de.fhg.iais.roberta.mode.action.nao.OnOff;
import de.fhg.iais.roberta.mode.action.nao.PointLook;
import de.fhg.iais.roberta.mode.action.nao.Posture;
import de.fhg.iais.roberta.mode.action.nao.RelativeAbsolute;
import de.fhg.iais.roberta.mode.action.nao.Resolution;
import de.fhg.iais.roberta.mode.action.nao.TurnDirection;
import de.fhg.iais.roberta.mode.action.nao.WalkDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.nao.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.nao.Coordinates;
import de.fhg.iais.roberta.mode.sensor.nao.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.nao.Sensor;
import de.fhg.iais.roberta.mode.sensor.nao.Side;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.CurveAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.generic.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.generic.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.ToneAction;
import de.fhg.iais.roberta.syntax.action.generic.TurnAction;
import de.fhg.iais.roberta.syntax.action.generic.VolumeAction;
import de.fhg.iais.roberta.syntax.action.nao.Animation;
import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.Dialog;
import de.fhg.iais.roberta.syntax.action.nao.GetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.GetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Hand;
import de.fhg.iais.roberta.syntax.action.nao.LedOff;
import de.fhg.iais.roberta.syntax.action.nao.LedReset;
import de.fhg.iais.roberta.syntax.action.nao.MoveJoint;
import de.fhg.iais.roberta.syntax.action.nao.PlayFile;
import de.fhg.iais.roberta.syntax.action.nao.PointLookAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.RecognizeWord;
import de.fhg.iais.roberta.syntax.action.nao.RecordVideo;
import de.fhg.iais.roberta.syntax.action.nao.SayText;
import de.fhg.iais.roberta.syntax.action.nao.SetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.SetLeds;
import de.fhg.iais.roberta.syntax.action.nao.SetMode;
import de.fhg.iais.roberta.syntax.action.nao.SetStiffness;
import de.fhg.iais.roberta.syntax.action.nao.SetVolume;
import de.fhg.iais.roberta.syntax.action.nao.Stop;
import de.fhg.iais.roberta.syntax.action.nao.TakePicture;
import de.fhg.iais.roberta.syntax.action.nao.TurnDegrees;
import de.fhg.iais.roberta.syntax.action.nao.WalkDistance;
import de.fhg.iais.roberta.syntax.action.nao.WalkTo;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.EmptyList;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.hardwarecheck.nao.UsedHardwareVisitor;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.nao.ForceSensor;
import de.fhg.iais.roberta.syntax.sensor.nao.ForgetFace;
import de.fhg.iais.roberta.syntax.sensor.nao.ForgetObject;
import de.fhg.iais.roberta.syntax.sensor.nao.Gyrometer;
import de.fhg.iais.roberta.syntax.sensor.nao.LearnFace;
import de.fhg.iais.roberta.syntax.sensor.nao.LearnObject;
import de.fhg.iais.roberta.syntax.sensor.nao.NaoMark;
import de.fhg.iais.roberta.syntax.sensor.nao.Sonar;
import de.fhg.iais.roberta.syntax.sensor.nao.Touchsensors;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.stmt.Stmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
@SuppressWarnings("rawtypes")
public class Ast2NaoPythonVisitor implements NaoAstVisitor<Void> {
    public static final String INDENT = "    ";

    private final Configuration brickConfiguration;
    private final String programName;
    private final StringBuilder sb = new StringBuilder();
    private final UsedHardwareVisitor usedHardware;

    private int indentation;
    private final StringBuilder indent = new StringBuilder();

    /**
     * initialize the Python code generator visitor.
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param usedHardware in the current program
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    Ast2NaoPythonVisitor(String programName, Configuration brickConfiguration, UsedHardwareVisitor usedHardwareVisitor, int indentation) {
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.indentation = indentation;
        this.usedHardware = usedHardwareVisitor;
        for ( int i = 0; i < indentation; i++ ) {
            this.indent.append(INDENT);
        }
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(String programName, Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) //
    {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrasesSet.size() >= 1);

        UsedHardwareVisitor checkVisitor = new UsedHardwareVisitor(phrasesSet);
        Ast2NaoPythonVisitor astVisitor = new Ast2NaoPythonVisitor(programName, brickConfiguration, checkVisitor, 0);
        astVisitor.generatePrefix(withWrapping);

        generateCodeFromPhrases(phrasesSet, withWrapping, astVisitor);

        astVisitor.generateSuffix(withWrapping);

        return astVisitor.sb.toString();
    }

    private static void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping, Ast2NaoPythonVisitor astVisitor) {
        boolean mainBlock = false;
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                mainBlock = handleMainBlocks(astVisitor, mainBlock, phrase);
                phrase.visit(astVisitor);
            }
            if ( mainBlock ) {
                mainBlock = false;
            }
        }
    }

    private static boolean handleMainBlocks(Ast2NaoPythonVisitor astVisitor, boolean mainBlock, Phrase<Void> phrase) {
        if ( phrase.getKind().getCategory() != Category.TASK ) {
            astVisitor.nlIndent();
        } else if ( !phrase.getKind().getName().equals("LOCATION") ) {
            mainBlock = true;
        }
        return mainBlock;
    }

    /**
     * Get the current indentation of the visitor. Meaningful for tests only.
     *
     * @return indentation value of the visitor.
     */
    int getIndentation() {
        return this.indentation;
    }

    /**
     * Get the string builder of the visitor. Meaningful for tests only.
     *
     * @return (current state of) the string builder
     */
    public StringBuilder getSb() {
        return this.sb;
    }

    private void incrIndentation() {
        this.indentation += 1;
        this.indent.append(INDENT);
    }

    private void decrIndentation() {
        this.indentation -= 1;
        this.indent.delete(0, INDENT.length());
    }

    private void nlIndent() {
        this.sb.append("\n").append(this.indent);
    }

    private static String getEnumCode(IMode value) {
        return "'" + value.toString().toLowerCase() + "'";
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        if ( isInteger(numConst.getValue()) ) {
            this.sb.append(numConst.getValue());
        } else {
            this.sb.append("float(");
            this.sb.append(numConst.getValue());
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append(boolConst.isValue() ? "True" : "False");
        return null;
    };

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("math.pi");
                break;
            case E:
                this.sb.append("math.e");
                break;
            case GOLDEN_RATIO:
                this.sb.append("BlocklyMethods.GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("math.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("math.sqrt(1.0/2.0)");
                break;
            case INFINITY:
                this.sb.append("float('inf')");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append(getEnumCode(colorConst.getValue()));
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("None");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        this.sb.append(var.getValue());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append(var.getName());
        if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(" = ");
            if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
                ExprList<Void> list = (ExprList<Void>) var.getValue();
                if ( list.get().size() == 2 ) {
                    list.get().get(1).visit(this);
                } else {
                    list.get().get(0).visit(this);
                }
            } else {
                var.getValue().visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        Unary.Op op = unary.getOp();
        String sym = op.getOpSymbol();
        // fixup language specific symbols
        if ( op == Unary.Op.NOT ) {
            sym = "not ";
        }
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, this.sb);
            this.sb.append(sym);
        } else {
            this.sb.append(sym);
            generateExprCode(unary, this.sb);
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        Binary.Op op = binary.getOp();
        String sym = op.getOpSymbol();
        // fixup language specific symbols
        switch ( op ) {
            case OR:
                sym = "or";
                break;
            case AND:
                sym = "and";
                break;
            case IN:
                sym = "in";
                break;
            default:
                break;
        }
        this.sb.append(' ').append(sym).append(' ');
        generateCodeRightExpression(binary, op);
        return null;
    }

    private void generateCodeRightExpression(Binary<Void> binary, Binary.Op op) {
        switch ( op ) {
            case TEXT_APPEND:
                this.sb.append("str(");
                generateSubExpr(this.sb, false, binary.getRight(), binary);
                this.sb.append(")");
                break;
            case DIVIDE:
                this.sb.append("float(");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                break;
        }
    }

    @Override
    public Void visitActionExpr(ActionExpr<Void> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                this.sb.append("\"\"");
                break;
            case "java.lang.Boolean":
                this.sb.append("True");
                break;
            case "java.lang.Integer":
                this.sb.append("0");
                break;
            case "java.util.ArrayList":
                break;
            case "de.fhg.iais.roberta.syntax.expr.NullConst":
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitShadowExpr(ShadowExpr<Void> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        boolean first = true;
        for ( Expr<Void> expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                if ( first ) {
                    first = false;
                } else {
                    if ( expr.getKind().hasName("BINARY", "UNARY") ) {
                        this.sb.append("; "); // FIXME
                    } else {
                        this.sb.append(", ");
                    }
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("math.pow(");
        mathPowerFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathPowerFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getName().visit(this);
        this.sb.append(" = ");
        assignStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        exprStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.isTernary() ) {
            generateCodeFromTernary(ifStmt);
        } else {
            generateCodeFromIfElse(ifStmt);
            generateCodeFromElse(ifStmt);
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            default:
                break;
        }
        incrIndentation();
        Mode mode = repeatStmt.getMode();
        if ( repeatStmt.getList().get().isEmpty() ) {
            if ( mode != Mode.WAIT ) {
                nlIndent();
                this.sb.append("pass");
            }
        } else {
            repeatStmt.getList().visit(this);
        }
        if ( mode == Mode.WAIT ) {
            nlIndent();
            this.sb.append("break");
        }
        decrIndentation();
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        this.sb.append(stmtFlowCon.getFlow().toString().toLowerCase());
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        for ( Stmt<Void> stmt : stmtList.get() ) {
            nlIndent();
            stmt.visit(this);
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.waitFor(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("hal.waitFor(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        this.sb.append("\n").append("def run():");
        incrIndentation();
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
    public Void visitActivityTask(ActivityTask<Void> activityTask) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLocation(Location<Void> location) {
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return sensorGetSample.getSensor().visit(this);
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        this.sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        functionStmt.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(getEnumCode(where1));
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(getEnumCode(where2));
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
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
            case LISTS_LENGTH:
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
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ROOT:
                this.sb.append("math.sqrt(");
                break;
            case ABS:
                this.sb.append("math.fabs(");
                break;
            case LN:
                this.sb.append("math.log(");
                break;
            case LOG10:
                this.sb.append("math.log10(");
                break;
            case EXP:
                this.sb.append("math.exp(");
                break;
            case POW10:
                this.sb.append("math.pow(10, ");
                break;
            case SIN:
                this.sb.append("math.sin(");
                break;
            case COS:
                this.sb.append("math.cos(");
                break;
            case TAN:
                this.sb.append("math.tan(");
                break;
            case ASIN:
                this.sb.append("math.asin(");
                break;
            case ATAN:
                this.sb.append("math.atan(");
                break;
            case ACOS:
                this.sb.append("math.acos(");
                break;
            case ROUND:
                this.sb.append("round(");
                break;
            case ROUNDUP:
                this.sb.append("math.ceil(");
                break;
            case ROUNDDOWN:
                this.sb.append("math.floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
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
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.sb.append("\ndef ").append(methodVoid.getMethodName()).append('(');
        methodVoid.getParameters().visit(this);
        this.sb.append("):");
        methodVoid.getBody().visit(this);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.sb.append("\ndef ").append(methodReturn.getMethodName()).append('(');
        methodReturn.getParameters().visit(this);
        this.sb.append("):");
        methodReturn.getBody().visit(this);
        this.nlIndent();
        this.sb.append("return ");
        methodReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("if ");
        methodIfReturn.getCondition().visit(this);
        if ( !methodIfReturn.getReturnValue().getKind().hasName("EMPTY_EXPR") ) {
            this.sb.append(": return ");
            methodIfReturn.getReturnValue().visit(this);
        } else {
            this.sb.append(": return None");
        }
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        this.sb.append(methodCall.getMethodName() + "(");
        methodCall.getParametersValues().visit(this);
        this.sb.append(")");
        return null;
    }

    private boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Binary.Op.MINUS
            && binary.getRight().getKind().hasName("BINARY")
            && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption && !expr.getKind().hasName("BINARY") ) {
            // parentheses are omitted
            expr.visit(this);
        } else {
            sb.append("( ");
            expr.visit(this);
            sb.append(" )");
        }
    }

    private void generateExprCode(Unary<Void> unary, StringBuilder sb) {
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("(");
            unary.getExpr().visit(this);
            sb.append(")");
        } else {
            unary.getExpr().visit(this);
        }
    }

    private void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
        this.sb.append(" if ( ");
        ifStmt.getExpr().get(0).visit(this);
        this.sb.append(" ) else ");
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
    }

    private void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i));
            } else {
                nlIndent();
                generateCodeFromStmtCondition("elif", ifStmt.getExpr().get(i));
            }
            incrIndentation();
            StmtList<Void> then = ifStmt.getThenList().get(i);
            if ( then.get().isEmpty() ) {
                nlIndent();
                this.sb.append("pass");
            } else {
                then.visit(this);
            }
            decrIndentation();
        }
    }

    private void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            nlIndent();
            this.sb.append("else:");
            incrIndentation();
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(' ');
        expr.visit(this);
        this.sb.append(":");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(' ');
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        this.sb.append(" in xrange(");
        expressions.get().get(1).visit(this);
        this.sb.append(", ");
        expressions.get().get(2).visit(this);
        this.sb.append(", ");
        expressions.get().get(3).visit(this);
        this.sb.append("):");
    }

    private void generatePrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#!/usr/bin/python\n\n");
        this.sb.append("import math\n");
        this.sb.append("import time\n");
        this.sb.append("from hal import Hal\n");
        this.sb.append("h = Hal()\n");
    }

    private void generateSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("\n\n");
        this.sb.append("def main():\n");
        this.sb.append(INDENT).append("try:\n");
        this.sb.append(INDENT).append(INDENT).append("run()\n");
        this.sb.append(INDENT).append("except Exception as e:\n");
        this.sb.append(INDENT).append(INDENT).append("h.say(\"Error!\")\n");

        this.sb.append("\n");
        this.sb.append("if __name__ == \"__main__\":\n");
        this.sb.append(INDENT).append("main()");
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

    //****************************************************************NAO******************************************************

    @Override
    public Void visitSetMode(SetMode<Void> setMode) {
        this.sb.append("h.mode(");
        if ( setMode.getModus() == Modus.ACTIVE ) {
            this.sb.append("1)");
        } else if ( setMode.getModus() == Modus.REST ) {
            this.sb.append("2)");
        }
        return null;
    }

    @Override
    public Void visitApplyPosture(ApplyPosture<Void> applyPosture) {
        this.sb.append("h.applyPosture(");
        if ( applyPosture.getPosture() == Posture.STAND ) {
            this.sb.append("\"Stand\")");
        } else if ( applyPosture.getPosture() == Posture.STANDINIT ) {
            this.sb.append("\"StandInit\")");
        } else if ( applyPosture.getPosture() == Posture.STANDZERO ) {
            this.sb.append("\"StandZero\")");
        }
        return null;
    }

    @Override
    public Void visitSetStiffness(SetStiffness<Void> setStiffness) {
        this.sb.append("h.stiffness(");
        if ( setStiffness.getBodyPart() == BodyPart.BODY ) {
            this.sb.append("\"Body\"");
        } else if ( setStiffness.getBodyPart() == BodyPart.HEAD ) {
            this.sb.append("\"Head\"");
        } else if ( setStiffness.getBodyPart() == BodyPart.ARMS ) {
            this.sb.append("\"Arms\"");
        } else if ( setStiffness.getBodyPart() == BodyPart.LEFTARM ) {
            this.sb.append("\"LArm\"");
        } else if ( setStiffness.getBodyPart() == BodyPart.RIGHTARM ) {
            this.sb.append("\"RArm\"");
        } else if ( setStiffness.getBodyPart() == BodyPart.LEGS ) {
            this.sb.append("\"Legs\"");
        } else if ( setStiffness.getBodyPart() == BodyPart.LEFTLEG ) {
            this.sb.append("\"LLeg\"");
        } else if ( setStiffness.getBodyPart() == BodyPart.RIHTLEG ) {
            this.sb.append("\"RLeg\"");
        }

        if ( setStiffness.getOnOff() == OnOff.ON ) {
            this.sb.append(", 1)");
        } else if ( setStiffness.getOnOff() == OnOff.OFF ) {
            this.sb.append(", 2)");
        }
        return null;
    }

    @Override
    public Void visitHand(Hand<Void> hand) {
        this.sb.append("h.hand(");
        if ( hand.getTurnDirection() == TurnDirection.LEFT ) {
            this.sb.append("\"LHand\"");
        } else if ( hand.getTurnDirection() == TurnDirection.RIGHT ) {
            this.sb.append("\"RHand\"");
        }

        if ( hand.getModus() == Modus.ACTIVE ) {
            this.sb.append(", 1)");
        } else if ( hand.getModus() == Modus.REST ) {
            this.sb.append(", 2)");
        }
        return null;
    }

    @Override
    public Void visitMoveJoint(MoveJoint<Void> moveJoint) {
        this.sb.append("h.moveJoint(");
        if ( moveJoint.getJoint() == Joint.HEADYAW ) {
            this.sb.append("\"HeadYaw\"");
        } else if ( moveJoint.getJoint() == Joint.HEADPITCH ) {
            this.sb.append("\"HeadPitch\"");
        } else if ( moveJoint.getJoint() == Joint.LSHOULDERPITCH ) {
            this.sb.append("\"LShoulderPitch\"");
        } else if ( moveJoint.getJoint() == Joint.LSHOULDERROLL ) {
            this.sb.append("\"LShoulderRoll\"");
        } else if ( moveJoint.getJoint() == Joint.LELBOWYAW ) {
            this.sb.append("\"LElbowYaw\"");
        } else if ( moveJoint.getJoint() == Joint.LELBOWROLL ) {
            this.sb.append("\"LElbowRoll\"");
        } else if ( moveJoint.getJoint() == Joint.LWRISTYAW ) {
            this.sb.append("\"LWristYaw\"");
        } else if ( moveJoint.getJoint() == Joint.LHAND ) {
            this.sb.append("\"LHand\"");
        } else if ( moveJoint.getJoint() == Joint.LHIPYAWPITCH ) {
            this.sb.append("\"LHipYawPitch\"");
        } else if ( moveJoint.getJoint() == Joint.LHIPROLL ) {
            this.sb.append("\"LHipRoll\"");
        } else if ( moveJoint.getJoint() == Joint.LHIPPITCH ) {
            this.sb.append("\"LHipPitch\"");
        } else if ( moveJoint.getJoint() == Joint.LKNEEPITCH ) {
            this.sb.append("\"LKneePitch\"");
        } else if ( moveJoint.getJoint() == Joint.LANKLEPITCH ) {
            this.sb.append("\"LAnklePitch\"");
        } else if ( moveJoint.getJoint() == Joint.RANKLEROLL ) {
            this.sb.append("\"RAnkleRoll\"");
        } else if ( moveJoint.getJoint() == Joint.RHIPYAWPITCH ) {
            this.sb.append("\"RHipYawPitch\"");
        } else if ( moveJoint.getJoint() == Joint.RHIPROLL ) {
            this.sb.append("\"RHipRoll\"");
        } else if ( moveJoint.getJoint() == Joint.RHIPITCH ) {
            this.sb.append("\"RHipPitch\"");
        } else if ( moveJoint.getJoint() == Joint.RKNEEPITCH ) {
            this.sb.append("\"RKneePitch\"");
        } else if ( moveJoint.getJoint() == Joint.RANKLEPITCH ) {
            this.sb.append("\"RAnklePitch\"");
        } else if ( moveJoint.getJoint() == Joint.RSHOULDERPITCH ) {
            this.sb.append("\"RShoulderPitch\"");
        } else if ( moveJoint.getJoint() == Joint.RSHOULDERROLL ) {
            this.sb.append("\"RShoulderRoll\"");
        } else if ( moveJoint.getJoint() == Joint.RELBOWYAW ) {
            this.sb.append("\"RElbowYaw\"");
        } else if ( moveJoint.getJoint() == Joint.RELBOWROLL ) {
            this.sb.append("\"RElbowRoll\"");
        } else if ( moveJoint.getJoint() == Joint.RWRISTYAW ) {
            this.sb.append("\"RWristYaw\"");
        } else if ( moveJoint.getJoint() == Joint.RHAND ) {
            this.sb.append("\"RHand\"");
        }
        this.sb.append(", ");
        moveJoint.getDegrees().visit(this);
        this.sb.append(", ");
        if ( moveJoint.getRelativeAbsolute() == RelativeAbsolute.ABSOLUTE) {
        	this.sb.append("1)");
        } else if ( moveJoint.getRelativeAbsolute() == RelativeAbsolute.RELATIVE) {
        	this.sb.append("2)");
        }
        return null;
    }

    @Override
    public Void visitWalkDistance(WalkDistance<Void> walkDistance) {
        this.sb.append("h.walk(");
        if ( walkDistance.getWalkDirection() == WalkDirection.BACKWARD ) {
            this.sb.append("-");
        }
        walkDistance.getDistanceToWalk().visit(this);
        this.sb.append(",0,0)");
        return null;
    }

    @Override
    public Void visitTurnDegrees(TurnDegrees<Void> turnDegrees) {
        this.sb.append("h.walk(0,0,");
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
        if ( animation.getMove() == Move.TAICHI ) {
            this.sb.append("h.taiChi()");
        } else if ( animation.getMove() == Move.BLINK ) {
            this.sb.append("h.blink()");
        } else if ( animation.getMove() == Move.WAVE ) {
            this.sb.append("h.wave()");
        } else if ( animation.getMove() == Move.WIPEFOREHEAD ) {
            this.sb.append("h.wipeForehead()");
        }
        return null;
    }

    @Override
    public Void visitPointLookAt(PointLookAt<Void> pointLookAt) {
        this.sb.append("h.pointLookAt(");
        pointLookAt.getpointX().visit(this);
        this.sb.append(", ");
        pointLookAt.getpointY().visit(this);
        this.sb.append(", ");
        pointLookAt.getpointZ().visit(this);
        this.sb.append(", ");
        if ( pointLookAt.getFrame() == Frame.TORSO ) {
            this.sb.append("0, ");
        } else if ( pointLookAt.getFrame() == Frame.WORLD ) {
            this.sb.append("1, ");
        } else if ( pointLookAt.getFrame() == Frame.ROBOT ) {
            this.sb.append("2, ");
        }
        pointLookAt.getSpeed().visit(this);
        if ( pointLookAt.getPointLook() == PointLook.LOOK ) {
            this.sb.append(", 1)");
        } else if ( pointLookAt.getPointLook() == PointLook.POINT ) {
            this.sb.append(", 0)");
        }
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

    @Override
    public Void visitSetLanguage(SetLanguage<Void> setLanguage) {
        this.sb.append("h.setLanguage(");
        if ( setLanguage.getLanguage() == Language.GERMAN ) {
            this.sb.append("\"German\")");
        } else if ( setLanguage.getLanguage() == Language.ENGLISH ) {
            this.sb.append("\"English\")");
        } else if ( setLanguage.getLanguage() == Language.FRENCH ) {
            this.sb.append("\"French\")");
        } else if ( setLanguage.getLanguage() == Language.JAPANESE ) {
            this.sb.append("\"Japanese\")");
        } else if ( setLanguage.getLanguage() == Language.CHINESE ) {
            this.sb.append("\"Chinese\")");
        } else if ( setLanguage.getLanguage() == Language.SPANISH ) {
            this.sb.append("\"Spanish\")");
        } else if ( setLanguage.getLanguage() == Language.KOREAN ) {
            this.sb.append("\"Korean\")");
        } else if ( setLanguage.getLanguage() == Language.ITALIAN ) {
            this.sb.append("\"Italian\")");
        } else if ( setLanguage.getLanguage() == Language.DUTCH ) {
            this.sb.append("\"Dutch\")");
        } else if ( setLanguage.getLanguage() == Language.FINNISH ) {
            this.sb.append("\"Finnish\")");
        } else if ( setLanguage.getLanguage() == Language.POLISH ) {
            this.sb.append("\"Polish\")");
        } else if ( setLanguage.getLanguage() == Language.RUSSIAN ) {
            this.sb.append("\"Russian\")");
        } else if ( setLanguage.getLanguage() == Language.TURKISH ) {
            this.sb.append("\"Turkish\")");
        } else if ( setLanguage.getLanguage() == Language.ARABIC ) {
            this.sb.append("\"Arabic\")");
        } else if ( setLanguage.getLanguage() == Language.CZECH ) {
            this.sb.append("\"Czech\")");
        } else if ( setLanguage.getLanguage() == Language.PORTUGUESE ) {
            this.sb.append("\"Portuguese\")");
        } else if ( setLanguage.getLanguage() == Language.BRAZILIAN ) {
            this.sb.append("\"Brazilian\")");
        } else if ( setLanguage.getLanguage() == Language.SWEDISH ) {
            this.sb.append("\"Swedish\")");
        } else if ( setLanguage.getLanguage() == Language.DANISH ) {
            this.sb.append("\"Danish\")");
        } else if ( setLanguage.getLanguage() == Language.NORWEGIAN ) {
            this.sb.append("\"Norwegian\")");
        } else if ( setLanguage.getLanguage() == Language.GREEK ) {
            this.sb.append("\"Greek\")");
        }
        return null;
    }

    @Override
    public Void visitGetLanguage(GetLanguage<Void> getLanguage) {
        this.sb.append("h.getLanguage()");
        return null;
    }

    @Override
    public Void visitSayText(SayText<Void> sayText) {
        this.sb.append("h.say(");
        sayText.getMsg().visit(this);
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
    public Void visitDialog(Dialog<Void> dialog) {
        this.sb.append("h.dialog(");
        dialog.getPhrase().visit(this);
        this.sb.append(", ");
        dialog.getAnswer().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRecognizeWord(RecognizeWord<Void> recognizeWord) {
        this.sb.append("h.recognizeWord(");
        recognizeWord.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSetLeds(SetLeds<Void> setLeds) {
        this.sb.append("h.setLeds(");
        if ( setLeds.getLed() == Led.ALL ) {
            this.sb.append("\"AllLeds\", ");
        } else if ( setLeds.getLed() == Led.CHEST ) {
            this.sb.append("\"ChestLeds\", ");
        } else if ( setLeds.getLed() == Led.EARS ) {
            this.sb.append("\"RightEarLeds\", ");
        } else if ( setLeds.getLed() == Led.EYES ) {
            this.sb.append("\"FaceLeds\", ");
        } else if ( setLeds.getLed() == Led.HEAD ) {
            this.sb.append("\"BrainLeds\", ");
        } else if ( setLeds.getLed() == Led.LEFTEAR ) {
            this.sb.append("\"LeftEarLeds\", ");
        } else if ( setLeds.getLed() == Led.LEFTEYE ) {
            this.sb.append("\"LeftFaceLeds\", ");
        } else if ( setLeds.getLed() == Led.LEFTFOOT ) {
            this.sb.append("\"LeftFootLeds\", ");
        } else if ( setLeds.getLed() == Led.RIGHTEAR ) {
            this.sb.append("\"RightEar\", ");
        } else if ( setLeds.getLed() == Led.RIGHTEYE ) {
            this.sb.append("\"RightFaceLeds\", ");
        } else if ( setLeds.getLed() == Led.RIGHTFOOT ) {
            this.sb.append("\"RightFootLeds\", ");
        }
        if ( setLeds.getColor() == Color.GREEN ) {
            this.sb.append("\"green\", ");
        } else if ( setLeds.getColor() == Color.BLUE ) {
            this.sb.append("\"blue\", ");
        } else if ( setLeds.getColor() == Color.RED ) {
            this.sb.append("\"red\", ");
        } else if ( setLeds.getColor() == Color.WHITE ) {
            this.sb.append("\"white\", ");
        } else if ( setLeds.getColor() == Color.YELLOW ) {
            this.sb.append("\"yellow\", ");
        } else if ( setLeds.getColor() == Color.MAGENTA ) {
            this.sb.append("\"magenta\", ");
        } else if ( setLeds.getColor() == Color.CYAN ) {
            this.sb.append("\"cyan\", ");
        }
        setLeds.getIntensity().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLedOff(LedOff<Void> ledOff) {
        this.sb.append("h.ledOff()");
        return null;
    }

    @Override
    public Void visitLedReset(LedReset<Void> ledReset) {
        this.sb.append("h.ledReset()");
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
    public Void visitTouchsensors(Touchsensors<Void> touchsensors) {
        this.sb.append("h.touchsensors(");
        if ( touchsensors.getSensor() == Sensor.BUMPER ) {
            this.sb.append("\"Bumper\", ");
        } else if ( touchsensors.getSensor() == Sensor.HAND ) {
            this.sb.append("\"Hand\", ");
        } else if ( touchsensors.getSensor() == Sensor.HEAD ) {
            this.sb.append("\"Head\", ");
        }
        if ( touchsensors.getSide() == Side.FRONT ) {
            this.sb.append("\"Front\")");
        } else if ( touchsensors.getSide() == Side.LEFT ) {
            this.sb.append("\"Left\")");
        } else if ( touchsensors.getSide() == Side.MIDDLE ) {
            this.sb.append("\"Middle\")");
        } else if ( touchsensors.getSide() == Side.REAR ) {
            this.sb.append("\"Rear\")");
        } else if ( touchsensors.getSide() == Side.RIGHT ) {
            this.sb.append("\"Right\")");
        }
        return null;
    }

    @Override
    public Void visitSonar(Sonar<Void> sonar) {
        this.sb.append("h.sonar()");
        return null;
    }

    @Override
    public Void visitGyrometer(Gyrometer<Void> gyrometer) {
        this.sb.append("h.gyrometer(");
        if ( gyrometer.getCoordinate() == Coordinates.X ) {
            this.sb.append("\"X\")");
        } else if ( gyrometer.getCoordinate() == Coordinates.Y ) {
            this.sb.append("\"Y\")");
        } else if ( gyrometer.getCoordinate() == Coordinates.Z ) {
            this.sb.append("\"Z\")");
        }
        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {
        this.sb.append("h.accelerometer(");
        if ( accelerometer.getCoordinate() == Coordinates.X ) {
            this.sb.append("\"X\")");
        } else if ( accelerometer.getCoordinate() == Coordinates.Y ) {
            this.sb.append("\"Y\")");
        } else if ( accelerometer.getCoordinate() == Coordinates.Z ) {
            this.sb.append("\"Z\")");
        }
        return null;
    }

    @Override
    public Void visitForceSensor(ForceSensor<Void> forceSensor) {
        this.sb.append("h.fsr(");
        if ( forceSensor.getSide() == Side.LEFT ) {
            this.sb.append("\"Left\")");
        } else if ( forceSensor.getSide() == Side.RIGHT ) {
            this.sb.append("\"Right\")");
        }
        return null;
    }

    @Override
    public Void visitNaoMark(NaoMark<Void> naoMark) {
        this.sb.append("h.naoMark()");
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
        takePicture.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitRecordVideo(RecordVideo<Void> recordVideo) {
        this.sb.append("h.recordVideo(");
        if ( recordVideo.getResolution() == Resolution.LOW ) {
            this.sb.append("0, ");
        } else if ( recordVideo.getResolution() == Resolution.MED ) {
            this.sb.append("1, ");
        } else if ( recordVideo.getResolution() == Resolution.HIGH ) {
            this.sb.append("2, ");
        }
        if ( recordVideo.getCamera() == Camera.TOP ) {
            this.sb.append("\"Top\", ");
        } else if ( recordVideo.getCamera() == Camera.BOTTOM ) {
            this.sb.append("\"Bottom\", ");
        }
        recordVideo.getDuration().visit(this);
        this.sb.append(", ");
        recordVideo.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }
    
    @Override
    public Void visitLearnFace(LearnFace<Void> learnFace) {
        this.sb.append("h.learnFace(");
        learnFace.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }
    
    @Override
    public Void visitForgetFace(ForgetFace<Void> forgetFace) {
        this.sb.append("h.forgetFace(");
        forgetFace.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }
    
    public Void visitLearnObject(LearnObject<Void> learnObject) {
        this.sb.append("h.learnObject(");
        learnObject.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }
    
    @Override
    public Void visitForgetObject(ForgetObject<Void> forgetObject) {
        this.sb.append("h.forgetObject(");
        forgetObject.getMsg().visit(this);
        this.sb.append(")");
        return null;
    }

	@Override
	public Void visitDriveAction(DriveAction<Void> driveAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitCurveAction(CurveAction<Void> curveAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTurnAction(TurnAction<Void> turnAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLightAction(LightAction<Void> lightAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitToneAction(ToneAction<Void> toneAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitColorSensor(ColorSensor<Void> colorSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLightSensor(LightSensor<Void> lightSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothWaitForConnectionAction(
			BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitConnectConst(ConnectConst<Void> connectConst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
		// TODO Auto-generated method stub
		return null;
	}
}