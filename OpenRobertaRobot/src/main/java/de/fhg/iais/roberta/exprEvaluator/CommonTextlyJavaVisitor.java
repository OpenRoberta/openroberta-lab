package de.fhg.iais.roberta.exprEvaluator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IsListEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MathChangeStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.textly.generated.TextlyJavaBaseVisitor;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser;
import de.fhg.iais.roberta.textly.generated.TextlyJavaParser.ExpressionContext;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.BlocklyRegion;
import de.fhg.iais.roberta.util.ast.TextRegion;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

public abstract class CommonTextlyJavaVisitor<T> extends TextlyJavaBaseVisitor<T> {
    private static final Pattern VAR_NAME = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*");
    private static final Pattern FUNCTION_NAME = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*");
    private static final Pattern BUTTONPORT_NAME = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]*");
    private static final Pattern EV3_PORT_NUMBER = Pattern.compile("[1-4]");
    private static final Pattern EV3_PORT_NAME = Pattern.compile("[A-Da-d]");
    private final Map<String, List<ParameterInfo>> functionSignatures = new HashMap<>();
    private final Set<String> declaredVariableNames = new HashSet<>();
    private final Set<String> userDefinedFunctionNames = new HashSet<>();

    private static class ParameterInfo {
        String name;
        BlocklyType type;

        ParameterInfo(String name, BlocklyType type) {
            this.name = name;
            this.type = type;
        }
    }

    public void collectFunctionSignatures(TextlyJavaParser.ProgramContext programContext) {
        for ( TextlyJavaParser.UserFuncContext userFunc : programContext.userFunc() ) {
            if ( userFunc instanceof TextlyJavaParser.FuncUserContext ) {
                TextlyJavaParser.FuncUserContext funcUser = (TextlyJavaParser.FuncUserContext) userFunc;
                String functionName = funcUser.NAME().getText();
                List<ParameterInfo> parametersInfo = new ArrayList<>();

                for ( TextlyJavaParser.NameDeclContext paramCtx : funcUser.nameDecl() ) {
                    String paramName = paramCtx.getToken(TextlyJavaParser.NAME, 0).getText();
                    String typeText = paramCtx.getToken(TextlyJavaParser.PRIMITIVETYPE, 0).getText();
                    BlocklyType paramType = BlocklyType.getByBlocklyName(textlyBlocklyType.getBlocklyType(typeText));
                    parametersInfo.add(new ParameterInfo(paramName, paramType));
                }

                functionSignatures.put(functionName, parametersInfo);
            }
        }
    }

    /**
     * @return AST instance for the whole expression
     */
    @Override
    public T visitExpression(ExpressionContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * This method handles the Microbitv2 expression in the CommonTextlyJavaVisitor.
     * When this specific expression is encountered, it generates an EmptyExpr to add an error.
     * If the Microbitv2 expression corresponds to the correct robot, the AST (Abstract Syntax Tree)
     * element will be generated in the @Microbitv2TextlyJavaVisitor.
     *
     * @param ctx the context of the Microbitv2 expression in the @TextlyJavaParser
     * @return an EmptyExpr for the specific Microbitv2 expression
     */
    @Override
    public T visitRobotMicrobitv2Expression(TextlyJavaParser.RobotMicrobitv2ExpressionContext ctx) throws UnsupportedOperationException {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("the expression " + ctx.getText() + "is only for Microbitv2 Robot ", true);
        return (T) result;
    }

    /**
     * This method handles the RobotWeDo expression in the CommonTextlyJavaVisitor.
     * When this specific expression is encountered, it generates an EmptyExpr to add an error.
     * If the RobotWeDo expression corresponds to the correct robot, the AST (Abstract Syntax Tree)
     * element will be generated in the @WedoTextlyJavaVisitor.
     *
     * @param ctx the context of the RobotWeDo expression in the @TextlyJavaParser
     * @return an EmptyExpr for the specific RobotWeDo expression
     */
    @Override
    public T visitRobotWeDoExpression(TextlyJavaParser.RobotWeDoExpressionContext ctx) throws UnsupportedOperationException {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("this expression is only for Wedo Robot " + ctx.getText(), true);
        return (T) result;
    }

    /**
     * This method handles the Ev3 expression in the CommonTextlyJavaVisitor.
     * When this specific expression is encountered, it generates an EmptyExpr to add an error.
     * If the RobotEv3 expression corresponds to the correct robot, the AST (Abstract Syntax Tree)
     * element will be generated in the @Ev3JavaVisitor.
     *
     * @param ctx the context of the Ev3 expression in the @TextlyJavaParser
     * @return an EmptyExpr for the specific Ev3 expression
     */
    @Override
    public T visitRobotEv3Expression(TextlyJavaParser.RobotEv3ExpressionContext ctx) throws UnsupportedOperationException {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("this expression is only for Ev3 Robot " + ctx.getText(), true);
        return (T) result;
    }

    /**
     * @return AST instance for the Robot expression
     */
    @Override
    public T visitRobotExpression(TextlyJavaParser.RobotExpressionContext ctx) {
        return visit(ctx.robotExpr());
    }

    /**
     * @return AST instance for Statement List
     */
    @Override
    public T visitStatementList(TextlyJavaParser.StatementListContext ctx) {

        StmtList stmtList = new StmtList();

        for ( TextlyJavaParser.StmtContext stmt : ctx.stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            stmtList.addStmt(statement);
        }

        return (T) stmtList;
    }

    /**
     * @return AST instance Null Constants
     */
    @Override
    public T visitNullConst(TextlyJavaParser.NullConstContext ctx) {
        return (T) new NullConst(mkPropertyFromClass(ctx, NullConst.class));
    }

    /**
     * @return AST instance of a color
     */
    @Override
    public T visitCol(TextlyJavaParser.ColContext ctx) {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("Colors are not supported in this robot", true);
        return (T) result;
    }

    /**
     * @return AST instance of a binary boolean operation
     */
    @Override
    public T visitBinaryB(TextlyJavaParser.BinaryBContext ctx) throws UnsupportedOperationException {
        Expr p = (Expr) visit(ctx.expr(0));
        Expr q = (Expr) visit(ctx.expr(1));
        p.setReadOnly();
        q.setReadOnly();
        if ( p instanceof ExprList ) {
            p.setReadOnly();
            p = new ListCreate(BlocklyType.VOID, (ExprList) p, mkInlineProperty(ctx, "robLists_create_with"));
        }
        if ( q instanceof ExprList ) {
            q.setReadOnly();
            q = new ListCreate(BlocklyType.VOID, (ExprList) q, mkInlineProperty(ctx, "robLists_create_with"));
        }

        if ( ctx.op.getType() == TextlyJavaParser.AND ) {
            return (T) new Binary(Binary.Op.AND, p, q, "", mkInlineProperty(ctx, "logic_operation"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.OR ) {
            return (T) new Binary(Binary.Op.OR, p, q, "", mkInlineProperty(ctx, "logic_operation"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.EQUAL ) {
            return (T) new Binary(Binary.Op.EQ, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.NEQUAL ) {
            return (T) new Binary(Binary.Op.NEQ, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.GET ) {
            return (T) new Binary(Binary.Op.GT, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.LET ) {
            return (T) new Binary(Binary.Op.LT, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.GEQ ) {
            return (T) new Binary(Binary.Op.GTE, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.LEQ ) {
            return (T) new Binary(Binary.Op.LTE, p, q, "", mkInlineProperty(ctx, "logic_compare"));
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a binary number operation
     */
    @Override
    public T visitBinaryN(TextlyJavaParser.BinaryNContext ctx) throws UnsupportedOperationException {
        Expr n0 = (Expr) visit(ctx.expr(0));
        Expr n1 = (Expr) visit(ctx.expr(1));
        n0.setReadOnly();
        n1.setReadOnly();
        if ( ctx.op.getType() == TextlyJavaParser.POW ) {
            List<Expr> args = new LinkedList();
            args.add(n0);
            args.add(n1);
            return (T) new MathPowerFunct(mkInlineProperty(ctx, "math_arithmetic"), FunctionNames.POWER, args);
        }
        if ( ctx.op.getType() == TextlyJavaParser.ADD ) {
            return (T) new Binary(Binary.Op.ADD, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.SUB ) {
            return (T) new Binary(Binary.Op.MINUS, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.MUL ) {
            return (T) new Binary(Binary.Op.MULTIPLY, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.DIV ) {
            return (T) new Binary(Binary.Op.DIVIDE, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        if ( ctx.op.getType() == TextlyJavaParser.MOD ) {
            return (T) new Binary(Binary.Op.MOD, n0, n1, "", mkInlineProperty(ctx, "math_arithmetic"));
        }
        throw new UnsupportedOperationException("Invalid binary operation");

    }

    /**
     * @return AST instance of a bool const
     */
    @Override
    public T visitBoolConstB(TextlyJavaParser.BoolConstBContext ctx) {
        return (T) new BoolConst(mkInlineProperty(ctx, "logic_boolean"), Boolean.parseBoolean(ctx.BOOL().getText().toLowerCase()));
    }

    /**
     * @return AST instance of a string const
     */
    @Override
    public T visitConstStr(TextlyJavaParser.ConstStrContext ctx) {
        String s = "";
        int c = ctx.getChildCount();
        for ( int i = 1; i < c - 1; i++ ) {
            s = s + ctx.getChild(i).toString();
            if ( i != c - 2 ) {
                s += " ";
            }
        }
        return (T) new StringConst(mkPropertyFromClass(ctx, StringConst.class), s);
    }

    /**
     * @return AST instance of a math const
     */
    @Override
    public T visitMathConst(TextlyJavaParser.MathConstContext ctx) {
        String c = ctx.CONST().getText();
        if ( c.equals("phi") ) {
            c = "golden_ratio";
        }
        if ( c.equals("inf") ) {
            c = "infinity";
        }
        if ( c.equals("sqrt_1_2") ) {
            c = "sqrt1_2";
        }
        return (T) new MathConst(mkPropertyFromClass(ctx, MathConst.class), MathConst.Const.get(c));
    }

    /**
     * @return AST instance of a num const
     */
    @Override
    public T visitIntConst(TextlyJavaParser.IntConstContext ctx) {
        return (T) new NumConst(null, ctx.INT().getText());
    }

    /**
     * @return AST instance of Image defined by the user, the method will be this method will be overridden in the Specific Robot textlyVisitor
     */
    @Override
    public T visitUserDefinedImage(TextlyJavaParser.UserDefinedImageContext ctx) {

        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("The current robot doesn't support the use of Images", true);
        return (T) result;
    }

    /**
     * @return AST instance of a Predefined Image, the method will be this method will be overridden in the Specific Robot textlyVisitor
     */
    @Override
    public T visitPredefinedImage(TextlyJavaParser.PredefinedImageContext ctx) {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("The current robot doesn't support the use of Images", true);
        return (T) result;

    }

    /**
     * @return AST instance of a Function for shift the Image, the method will be this method will be overridden in the Specific Robot textlyVisitor
     */
    @Override
    public T visitImageShift(TextlyJavaParser.ImageShiftContext ctx) {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("The current robot doesn't support the use of Images", true);
        return (T) result;

    }

    /**
     * @return AST instance of a Function for invert the Image, the method will be this method will be overridden in the Specific Robot textlyVisitor
     */
    @Override
    public T visitImageInvert(TextlyJavaParser.ImageInvertContext ctx) {
        Expr result = new EmptyExpr(BlocklyType.NUMBER);
        result.addTextlyError("The current robot doesn't support the use of Images", true);
        return (T) result;

    }

    /**
     * @return AST instance of a function
     */
    @Override
    public T visitFunc(TextlyJavaParser.FuncContext ctx) throws UnsupportedOperationException {
        String f = ctx.FNAME().getText();
        List<Expr> args = new LinkedList();

        for ( TextlyJavaParser.ExprContext expr : ctx.expr() ) {
            Expr ast = (Expr) visit(expr);
            ast.setReadOnly();
            args.add(ast);
        }
        for ( int i = 0; i < args.size(); i++ ) {
            if ( args.get(i) instanceof ExprList ) {
                ExprList e = (ExprList) args.get(i);
                e.setReadOnly();
                args.set(i, new ListCreate(BlocklyType.ARRAY, e, mkInlineProperty(ctx, "robLists_create_with")));
            }
        }
        return (T) mkExpr(f, args, ctx);
    }

    /**
     * @return AST instance for Function Expression
     */
    public T mkExpr(String f, List<Expr> args, TextlyJavaParser.FuncContext ctx) {
        ExprList list = new ExprList();
        for ( Expr e : args ) {
            e.setReadOnly();
            list.addExpr(e);
        }
        list.setReadOnly();
        Sig signature = FunctionNames.get(f).signature;
        int numberParams = signature.paramTypes.length;
        if ( signature.varargParamType == null && args.size() == numberParams ) {
            switch ( f ) {
                case "sin":
                case "cos":
                case "tan":
                case "asin":
                case "acos":
                case "atan":
                    return (T) new FunctionExpr(new MathSingleFunct(FunctionNames.get(f), args, mkInlineProperty(ctx, "math_trig")));
                case "exp":
                case "sqrt":
                case "abs":
                case "log10":
                case "log":
                case "square":
                case "pow10":
                    return (T) new FunctionExpr(new MathSingleFunct(FunctionNames.get(f), args, mkInlineProperty(ctx, "math_single")));
                case "round":
                case "ceil":
                case "floor":
                    return (T) new FunctionExpr(new MathSingleFunct(FunctionNames.get(f), args, mkInlineProperty(ctx, "math_round")));
                case "randomInt":
                    return (T) new FunctionExpr(new MathRandomIntFunct(mkExternalProperty(ctx, "math_random_int"), args.get(0), args.get(1)));
                case "randomFloat":
                    return (T) new FunctionExpr(new MathRandomFloatFunct(mkExternalProperty(ctx, "math_random_float")));
                case "isEven":
                case "isOdd":
                case "isPrime":
                case "isWhole":
                case "isPositive":
                case "isNegative":
                    return (T) new FunctionExpr(new MathNumPropFunct(FunctionNames.get(f), args, mkPropertyFromClass(ctx, MathNumPropFunct.class)));
                case "isDivisibleBy":
                    return (T) new FunctionExpr(new MathNumPropFunct(FunctionNames.DIVISIBLE_BY, args, mkPropertyFromClass(ctx, MathNumPropFunct.class)));
                case "average":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.AVERAGE, args.get(0)));
                case "stddev":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.get(f), args.get(0)));
                case "randomItem":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.get(f), args.get(0)));
                case "sum":
                case "min":
                case "max":
                case "median":
                    return (T) new FunctionExpr(new MathOnListFunct(mkPropertyFromClass(ctx, MathOnListFunct.class), null, FunctionNames.get(f), args.get(0)));
                case "size":
                    return (T) new FunctionExpr(new LengthOfListFunct(mkExternalProperty(ctx, "robLists_length"), args.get(0)));
                case "indexOfFirst":
                    return (T) new FunctionExpr(new IndexOfFunct(mkInlineProperty(ctx, "robLists_indexOf"), IndexLocation.FIRST, args.get(0), args.get(1)));
                case "indexOfLast":
                    return (T) new FunctionExpr(new IndexOfFunct(mkInlineProperty(ctx, "robLists_indexOf"), IndexLocation.LAST, args.get(0), args.get(1)));
                case "get":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.FROM_START, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "getFromEnd":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.FROM_END, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "getFirst":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.FIRST, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "getLast":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET, IndexLocation.LAST, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "getAndRemove":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.FROM_START, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "getAndRemoveFromEnd":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.FROM_END, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "getAndRemoveFirst":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.FIRST, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "getAndRemoveLast":
                    return (T) new FunctionExpr(new ListGetIndex(ListElementOperations.GET_REMOVE, IndexLocation.LAST, args, null, mkInlineProperty(ctx, "robLists_getIndex")));
                case "createListWith":
                    return (T) new FunctionExpr(new ListRepeat(BlocklyType.VOID, args, mkExternalProperty(ctx, "robLists_repeat")));
                case "createEmptyList":
                    ExprList emptyList = new ExprList();
                    emptyList.setReadOnly();
                    String listType = ctx.PRIMITIVETYPE().getText();
                    return (T) new ListCreate(BlocklyType.get(listType.toUpperCase()), emptyList, mkExternalProperty(ctx, "robLists_create_with"));
                case "subList":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_START)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromIndexToLast":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.LAST)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromIndexToEnd":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_START, IndexLocation.FROM_END)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromFirstToIndex":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_START)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromFirstToLast":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.LAST)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromFirstToEnd":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FIRST, IndexLocation.FROM_END)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromEndToIndex":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_START)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromEndToLast":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.LAST)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "subListFromEndToEnd":
                    return (T) new FunctionExpr(new GetSubFunct(FunctionNames.GET_SUBLIST, new ArrayList<IMode>(Arrays.asList(IndexLocation.FROM_END, IndexLocation.FROM_END)), args, mkInlineProperty(ctx, "robLists_getSublist")));
                case "print":
                    return (T) new FunctionExpr(new TextPrintFunct(args, mkPropertyFromClass(ctx, TextPrintFunct.class)));
                case "createTextWith":
                    return (T) new FunctionExpr(new TextJoinFunct(list, mkInlineProperty(ctx, "robText_join")));
                case "constrain":
                    return (T) new FunctionExpr(new MathConstrainFunct(mkPropertyFromClass(ctx, MathConstrainFunct.class), args.get(0), args.get(1), args.get(2)));
                case "isEmpty":
                    return (T) new FunctionExpr(new IsListEmptyFunct(mkInlineProperty(ctx, "robLists_isEmpty"), args.get(0)));
                case "castToString":
                    return (T) new FunctionExpr(new MathCastStringFunct(mkPropertyFromClass(ctx, MathCastStringFunct.class), args.get(0)));
                case "castToChar":
                    return (T) new FunctionExpr(new MathCastCharFunct(mkPropertyFromClass(ctx, MathCastStringFunct.class), args.get(0)));
                case "castToNumber":
                    return (T) new FunctionExpr(new TextStringCastNumberFunct(mkPropertyFromClass(ctx, TextStringCastNumberFunct.class), args.get(0)));
                case "castStringToNumber":
                    return (T) new FunctionExpr(new TextCharCastNumberFunct(mkPropertyFromClass(ctx, TextCharCastNumberFunct.class), args.get(0), args.get(1)));
                default:
                    Expr result = new EmptyExpr(BlocklyType.NOTHING);
                    result.addTextlyError("Invalid function name " + f, true);
                    return (T) result;
            }
        } else if ( "getRGB".equals(f) ) {
            Expr empty = new EmptyExpr(BlocklyType.NUMBER_INT);
            if ( args.size() == 3 ) {
                return (T) new RgbColor(mkInlineProperty(ctx, "robColour_rgb"), args.get(0), args.get(1), args.get(2), empty);
            } else if ( args.size() == 4 ) {
                return (T) new RgbColor(mkInlineProperty(ctx, "robColour_rgb"), args.get(0), args.get(1), args.get(2), args.get(3));
            }
        } else if ( "createTextWith".equals(f) ) {
            return (T) new FunctionExpr(new TextJoinFunct(list, mkInlineProperty(ctx, "robText_join")));
        }
        Expr result = new EmptyExpr(BlocklyType.NOTHING);
        result.addTextlyError("Wrong number of arguments for this function", true);
        return (T) result;
    }

    /**
     * @return AST instance of a float const
     */
    @Override
    public T visitFloatConst(TextlyJavaParser.FloatConstContext ctx) {
        return (T) new NumConst(null, ctx.FLOAT().getText());
    }

    /**
     * @return AST instance of a unary boolean operation
     */
    @Override
    public T visitUnaryB(TextlyJavaParser.UnaryBContext ctx) throws UnsupportedOperationException {
        Expr e = (Expr) visit(ctx.expr());
        e.setReadOnly();
        if ( ctx.op.getType() == TextlyJavaParser.NOT ) {
            return (T) new Unary(Unary.Op.NOT, e, mkInlineProperty(ctx, "logic_negate"));
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a unary number operation
     */
    @Override
    public T visitUnaryN(TextlyJavaParser.UnaryNContext ctx) throws UnsupportedOperationException {
        Expr e = (Expr) visit(ctx.expr());
        e.setReadOnly();
        if ( ctx.op.getType() == TextlyJavaParser.ADD ) {
            return (T) new Unary(Unary.Op.PLUS, e, mkPropertyFromClass(ctx, Unary.class));
        }
        if ( ctx.op.getType() == TextlyJavaParser.SUB ) {
            return (T) new Unary((Unary.Op.NEG), e, mkInlineProperty(ctx, "math_single"));
        }
        throw new UnsupportedOperationException("Invalid unary operation");
    }

    /**
     * @return AST instance of a var
     */
    @Override
    public T visitVarName(TextlyJavaParser.VarNameContext ctx) {
        // By default we use VOID for the types of the variables, the type can be
        // checked later when compiling the program with the typechecker
        String nameVar = ctx.NAME().getText();
        Var var = new Var(BlocklyType.VOID, nameVar, mkPropertyFromClass(ctx, Var.class));
        return (T) checkValidationName(var, nameVar, NameType.VAR);

    }

    /**
     * @return AST instance of a list expression
     */
    @Override
    public T visitListExpr(TextlyJavaParser.ListExprContext ctx) {
        ExprList list = new ExprList();
        for ( TextlyJavaParser.ExprContext expr : ctx.expr() ) {
            Expr e = (Expr) visit(expr);
            e.setReadOnly();
            list.addExpr(e);
        }
        list.setReadOnly();
        return (T) list;
    }

    /**
     * @return AST instance of the expression within a set of parentheses
     */
    @Override
    public T visitParenthese(TextlyJavaParser.ParentheseContext ctx) {
        return visit(ctx.expr());
    }

    /**
     * @return AST instance of a connection const
     */
    @Override
    public T visitConn(TextlyJavaParser.ConnContext ctx) {
        return (T) new ConnectConst(mkPropertyFromClass(ctx, ConnectConst.class), ctx.op1.getText(), ctx.op0.getText());
    }

    /**
     * @return AST instance of the ternary op
     */
    @Override
    public T visitIfElseOp(TextlyJavaParser.IfElseOpContext ctx) {
        Expr q = (Expr) visit(ctx.expr(1));
        Expr r = (Expr) visit(ctx.expr(2));
        if ( q instanceof ExprList ) {
            q.setReadOnly();
            q = new ListCreate(BlocklyType.VOID, (ExprList) q, mkPropertyFromClass(ctx, ListCreate.class));
        }
        if ( r instanceof ExprList ) {
            r.setReadOnly();
            r = new ListCreate(BlocklyType.VOID, (ExprList) r, mkPropertyFromClass(ctx, ListCreate.class));
        }
        q.setReadOnly();
        r.setReadOnly();
        StmtList thenList = new StmtList();
        StmtList elseList = new StmtList();
        thenList.addStmt(new ExprStmt(q));
        elseList.addStmt(new ExprStmt(r));
        thenList.setReadOnly();
        elseList.setReadOnly();
        TernaryExpr ternaryExpr = new TernaryExpr(mkPropertyFromClass(ctx, TernaryExpr.class), (Expr) visit(ctx.expr(0)), (Expr) visit(ctx.expr(1)), (Expr) visit(ctx.expr(2)));
        return (T) ternaryExpr;
    }

    /**
     * @return AST instance Statement Functions
     */
    @Override
    public T visitStmtFunc(TextlyJavaParser.StmtFuncContext ctx) throws UnsupportedOperationException {
        String f = ctx.FNAMESTMT().getText();
        List<Expr> args = new LinkedList();

        for ( TextlyJavaParser.ExprContext expr : ctx.expr() ) {
            Expr ast = (Expr) visit(expr);
            ast.setReadOnly();
            args.add(ast);
        }

        return (T) mkStmtExpr(f, args, ctx);
    }

    /**
     * @return AST instance for Function statements
     */
    private T mkStmtExpr(String f, List<Expr> args, TextlyJavaParser.StmtFuncContext ctx) {
        List<Expr> argsStatements = new LinkedList();

        Sig signature = FunctionNames.get(f).signature;
        int numberParams = signature.paramTypes.length;

        if ( signature.varargParamType == null && args.size() == numberParams ) {
            switch ( f ) {
                case "set":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FROM_START, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "setFromEnd":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FROM_END, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "setFirst":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.FIRST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "setLast":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.SET, IndexLocation.LAST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insert":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FROM_START, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insertFromEnd":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FROM_END, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insertFirst":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.FIRST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "insertLast":
                    return (T) new FunctionStmt(new ListSetIndex(ListElementOperations.INSERT, IndexLocation.LAST, args, mkExternalProperty(ctx, "robLists_setIndex")));
                case "remove":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FROM_START, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeFromEnd":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FROM_END, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeFirst":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.FIRST, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "removeLast":
                    return (T) new FunctionStmt(new ListGetIndex(ListElementOperations.REMOVE, IndexLocation.LAST, args, "VOID", mkExternalProperty(ctx, "robLists_getIndex")));
                case "changeBy":
                    if ( args.get(0) instanceof Var ) {
                        return (T) new MathChangeStmt(mkInlineProperty(ctx, "robMath_change"), args.get(0), args.get(1));
                    } else {
                        StmtList statementList = new StmtList(ctx);
                        statementList.setReadOnly();
                        statementList.addTextlyError("This function use a variable for the first parameter ", true);
                        return (T) statementList;
                    }
                case "appendText":
                    if ( args.get(0) instanceof Var ) {
                        return (T) new TextAppendStmt(mkInlineProperty(ctx, "robText_append"), args.get(0), args.get(1));
                    } else {
                        StmtList statementList = new StmtList(ctx);
                        statementList.setReadOnly();
                        statementList.addTextlyError("This function use a variable for the first parameter ", true);
                        return (T) statementList;
                    }


                default:
                    StmtList statementList = new StmtList(ctx);
                    statementList.setReadOnly();
                    statementList.addTextlyError("Invalid function name " + f, true);
                    return (T) statementList;
            }
        } else {
            StmtList statementList = new StmtList(ctx);
            statementList.setReadOnly();
            statementList.addTextlyError("Wrong number of arguments for this function", true);
            return (T) statementList;
        }
    }

    /**
     * @return AST instance for AssigStmt
     */
    @Override
    public T visitBinaryVarAssign(TextlyJavaParser.BinaryVarAssignContext ctx) throws UnsupportedOperationException {
        String nameVar = ctx.NAME().getText();
        Var n0 = new Var(BlocklyType.VOID, nameVar, mkPropertyFromClass(ctx, Var.class));
        Expr n1 = (Expr) visit(ctx.expr());

        if ( ctx.op.getText().equals("SET") ) {
            AssignStmt assignStmt = new AssignStmt(mkInlineProperty(ctx, "variables_set"), n0, n1);
            return (T) checkValidationName(assignStmt, nameVar, NameType.VAR);
        }

        AssignStmt assignStmt = new AssignStmt(mkInlineProperty(ctx, "variables_set"), n0, n1);
        return (T) checkValidationName(assignStmt, nameVar, NameType.VAR);
    }

    /**
     * @return AST instance for the If statement
     */
    @Override
    public T visitConditionStatementBlock(TextlyJavaParser.ConditionStatementBlockContext ctx) throws UnsupportedOperationException {
        List<Expr> conditionsList = new ArrayList<>();
        List<StmtList> listOfStatementList = new ArrayList<>();
        StmtList statementElseList = new StmtList();
        statementElseList.setReadOnly();

        for ( TextlyJavaParser.StatementListContext stmt : ctx.statementList() ) {
            StmtList statement = (StmtList) visit(stmt);
            statement.setReadOnly();
            listOfStatementList.add(statement);
        }

        for ( TextlyJavaParser.ExprContext expr : ctx.expr() ) {
            Expr condition = (Expr) visit(expr);
            condition.setReadOnly();
            conditionsList.add(condition);
        }


        if ( ctx.op != null && ctx.op.getType() == TextlyJavaParser.ELSE ) {

            statementElseList = (StmtList) visit(ctx.statementList(ctx.statementList().size() - 1)); // Get the last statementList context
            statementElseList.setReadOnly();
            listOfStatementList.remove(ctx.statementList().size() - 1);
        }
        return (T) new IfStmt(mknullProperty(ctx, statementElseList.sl.size() > 0 ? "robControls_ifElse" : "robControls_if"), conditionsList, listOfStatementList, statementElseList, statementElseList.sl.size(), listOfStatementList.size() - 1);

    }

    /**
     * @return AST instance for Repeat statement
     */
    @Override
    public T visitRepeatStatement(TextlyJavaParser.RepeatStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList();
        for ( TextlyJavaParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();

        Expr exprBoolean = (Expr) visit(ctx.expr());
        exprBoolean.setReadOnly();
        if ( exprBoolean instanceof BoolConst ) {
            if ( ((BoolConst) exprBoolean).value == true ) {
                Expr trueExpr = new BoolConst(mkInlineProperty(ctx, "logic_boolean"), true);
                trueExpr.setReadOnly();
                statementList.setReadOnly();
                return (T) new RepeatStmt(RepeatStmt.Mode.FOREVER, trueExpr, statementList, mkExternalProperty(ctx, "robControls_loopForever"));
            }
        } else {
            return (T) new RepeatStmt(RepeatStmt.Mode.WHILE, exprBoolean, statementList, mkExternalProperty(ctx, "controls_whileUntil"));
        }
        return null;
    }

    /**
     * @return AST instance for "for each" control statement
     */
    @Override
    public T visitRepeatForEach(TextlyJavaParser.RepeatForEachContext ctx) throws UnsupportedOperationException {
        String typeAsString = ctx.nameDecl().start.getText();
        BlocklyType type = BlocklyType.get(typeAsString);

        Phrase emptyExpression = new EmptyExpr(type);
        emptyExpression.setReadOnly();

        String nameVar = ctx.nameDecl().stop.getText();
        VarDeclaration var = checkValidationName(new VarDeclaration(type, nameVar, emptyExpression, false, false, mkExternalProperty(ctx, "robControls_forEach")), nameVar, NameType.VAR);
        var.setReadOnly();

        Expr expr = (Expr) visit(ctx.expr());
        expr.setReadOnly();

        StmtList statementList = new StmtList();
        for ( TextlyJavaParser.StmtContext stmt : ctx.statementList().stmt() ) {
            Stmt statement = (Stmt) visit(stmt);
            statement.setReadOnly();
            statementList.addStmt(statement);
        }
        statementList.setReadOnly();

        Binary exprBinary = new Binary(Binary.Op.IN, var, expr, "", mkExternalProperty(ctx, "robControls_forEach"));
        exprBinary.setReadOnly();

        return (T) new RepeatStmt(RepeatStmt.Mode.FOR_EACH, exprBinary, statementList, mkExternalProperty(ctx, "robControls_forEach"));

    }

    /**
     * @return AST instance for "wait until" control statement
     */
    @Override
    public T visitWaitStatement(TextlyJavaParser.WaitStatementContext ctx) throws UnsupportedOperationException {
        ExprList conditionsList = new ExprList();
        StmtList waitStatementList = new StmtList();

        Expr conditionWait = (Expr) visit(ctx.expr(0));
        conditionWait.setReadOnly();
        StmtList statementListWait = (StmtList) visit(ctx.statementList(0));
        statementListWait.setReadOnly();
        waitStatementList.addStmt(new RepeatStmt(RepeatStmt.Mode.WAIT, conditionWait, statementListWait, mkExternalProperty(ctx, "robControls_wait")));

        if ( ctx.op != null && ctx.op.getType() == TextlyJavaParser.ORWAITFOR ) {
            int numberOrWaitFor = ctx.expr().size() - 1;

            for ( int i = 0; i < numberOrWaitFor; i++ ) {
                Expr conditionOrWait = (Expr) visit(ctx.expr(i + 1));
                conditionOrWait.setReadOnly();
                StmtList statementListOrWait = new StmtList();
                statementListOrWait = (StmtList) visit(ctx.statementList(i + 1));
                statementListOrWait.setReadOnly();
                waitStatementList.addStmt(new RepeatStmt(RepeatStmt.Mode.WAIT, conditionOrWait, statementListOrWait, mkExternalProperty(ctx, "robControls_wait")));
            }
        }

        conditionsList.setReadOnly();
        waitStatementList.setReadOnly();
        return (T) new WaitStmt(mkExternalProperty(ctx, "robControls_wait"), waitStatementList);
    }

    /**
     * @return AST instance for "repeant n times" and "repeant indefinitely" control statement
     */
    @Override
    public T visitRepeatFor(TextlyJavaParser.RepeatForContext ctx) throws UnsupportedOperationException {
        String typeAsString = ctx.nameDecl().start.getText();
        BlocklyType type = BlocklyType.get(typeAsString);

        if ( type.equalAsTypes(BlocklyType.NUMBER) ) {
            StmtList statementList = new StmtList();

            for ( TextlyJavaParser.StmtContext stmt : ctx.statementList().stmt() ) {
                Stmt statement = (Stmt) visit(stmt);
                statement.setReadOnly();
                statementList.addStmt(statement);
            }
            statementList.setReadOnly();

            ExprList el = new ExprList();
            String nameVar = ctx.nameDecl().stop.getText();
            Var i = checkValidationName(new Var(BlocklyType.NUMBER_INT, nameVar, mkPropertyFromClass(ctx, Var.class)), nameVar, NameType.VAR);
            el.addExpr(i);
            for ( TextlyJavaParser.ExprContext expr : ctx.expr() ) {
                Expr condition = (Expr) visit(expr);
                condition.setReadOnly();
                el.addExpr(condition);
            }

            el.setReadOnly();
            if ( el.el.get(1) instanceof NumConst && el.el.get(3) instanceof NumConst ) {
                if ( ((NumConst) el.el.get(1)).value.equals("0") && ((NumConst) el.el.get(3)).value.equals("1") ) {
                    return (T) new RepeatStmt(RepeatStmt.Mode.TIMES, el, statementList, mkInlineProperty(ctx, "controls_repeat_ext"));
                }
            }
            if ( ctx.op != null && ctx.op.getType() == TextlyJavaParser.STEP && el.el.get(3) instanceof Var ) {
                if ( ((Var) el.el.get(3)).name.equals(nameVar) ) {
                    el.el.set(3, new NumConst(null, "1"));
                    return (T) new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkInlineProperty(ctx, "robControls_for"));
                } else {
                    Stmt resultError = new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkInlineProperty(ctx, "robControls_for"));
                    resultError.addTextlyError("Variable name should be the same for ++ notation", true);
                    return (T) resultError;
                }

            } else if ( ctx.op != null && ctx.op.getType() == TextlyJavaParser.STEP && !(el.el.get(3) instanceof Var) ) {
                Stmt resultError = new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkExternalProperty(ctx, "robControls_for"));
                resultError.addTextlyError("For ++ notation only variable is allowed", true);
                return (T) resultError;
            } else if ( ctx.op != null && ctx.op.getType() == TextlyJavaParser.SUB && el.el.get(3) instanceof NumConst ) {
                el.el.set(3, new Unary((Unary.Op.NEG), el.el.get(3), mkInlineProperty(ctx, "math_single")));
                return (T) new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkInlineProperty(ctx, "robControls_for"));
            } else if ( ctx.op != null && ctx.op.getType() == TextlyJavaParser.ADD ) {
                String nameVariable = ctx.NAME(2).getText();
                Var var = checkValidationName(new Var(BlocklyType.NUMBER_INT, nameVariable, mkPropertyFromClass(ctx, Var.class)), nameVar, NameType.VAR);
                return (T) new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkInlineProperty(ctx, "robControls_for"));
            } else {
                return (T) new RepeatStmt(RepeatStmt.Mode.FOR, el, statementList, mkInlineProperty(ctx, "robControls_for"));
            }
        } else {
            StmtList statementList = new StmtList(ctx);
            statementList.setReadOnly();
            statementList.addTextlyError("Invalid type for " + ctx.NAME(), true);
            return (T) statementList;
        }

    }

    /**
     * @return AST instance for Flow control Statements, break and continue
     */
    @Override
    public T visitFlowControl(TextlyJavaParser.FlowControlContext ctx) throws UnsupportedOperationException {
        switch ( ctx.op.getType() ) {
            case TextlyJavaParser.BREAK:
                return (T) new StmtFlowCon(mkPropertyFromClass(ctx, StmtFlowCon.class), StmtFlowCon.Flow.BREAK);
            case TextlyJavaParser.CONTINUE:
                return (T) new StmtFlowCon(mkExternalProperty(ctx, "controls_flow_statements"), StmtFlowCon.Flow.CONTINUE);
            default:
                StmtList statementList = new StmtList(ctx);
                statementList.setReadOnly();
                statementList.addTextlyError("Invalid flow control statement. Expected 'break' or 'continue' ", true);
                return (T) statementList;
        }
    }

    /**
     * @return AST instance for "wait ms"
     */
    @Override
    public T visitWaitTimeStatement(TextlyJavaParser.WaitTimeStatementContext ctx) throws UnsupportedOperationException {
        return (T) new WaitTimeStmt(mkExternalProperty(ctx, "robControls_wait_time"), (Expr) visit(ctx.expr()));
    }

    /**
     * @return AST instance for Parameters declaration
     */
    @Override
    public T visitParamsMethod(TextlyJavaParser.ParamsMethodContext ctx) throws UnsupportedOperationException {

        String typeAsString = ctx.PRIMITIVETYPE().getText();
        BlocklyType type = BlocklyType.getByBlocklyName(textlyBlocklyType.getBlocklyType(typeAsString));
        Phrase emptyExpression = new EmptyExpr(type);
        emptyExpression.setReadOnly();
        String nameVar = ctx.NAME().getText();
        boolean isLastParam = isLastParamsMethodContext(ctx);
        VarDeclaration var = checkValidationName(new VarDeclaration(type, nameVar, emptyExpression, isLastParam, false, mkExternalProperty(ctx, "robLocalVariables_declare")), nameVar, NameType.VAR);
        var.setReadOnly();
        if ( declaredVariableNames.contains(nameVar) ) {
            var.addTextlyError("The variable name is duplicated", true);
        }

        declaredVariableNames.add(nameVar);
        return (T) var;
    }

    private boolean isLastParamsMethodContext(TextlyJavaParser.ParamsMethodContext ctx) {

        ParserRuleContext parentCtx = ctx.getParent();
        List<ParseTree> children = parentCtx.children;

        int ctxIndex = children.indexOf(ctx);
        for ( int i = ctxIndex + 1; i < children.size(); i++ ) {
            if ( children.get(i) instanceof TextlyJavaParser.ParamsMethodContext ) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return AST instance for Functions defined by user
     */
    @Override
    public T visitFuncUser(TextlyJavaParser.FuncUserContext ctx) throws UnsupportedOperationException {

        String methodName = ctx.NAME().getText();
        if ( validatePattern(methodName, NameType.FUNCTIONNAME) ) {
            ExprList parameters = new ExprList();
            for ( TextlyJavaParser.NameDeclContext nameDeclContext : ctx.nameDecl() ) {
                Expr param = (Expr) visit(nameDeclContext);
                parameters.addExpr(param);
                param.setReadOnly();
            }
            parameters.setReadOnly();

            StmtList statementList = new StmtList();
            for ( TextlyJavaParser.StmtContext stmt : ctx.statementList().stmt() ) {
                Stmt statement = (Stmt) visit(stmt);
                statement.setReadOnly();
                statementList.addStmt(statement);
            }
            statementList.setReadOnly();

            if ( ctx.op != null && ctx.op.getType() == TextlyJavaParser.RETURN ) {
                BlocklyType returnType = BlocklyType.get(textlyBlocklyType.getBlocklyType(ctx.PRIMITIVETYPE().getText()));
                Expr returnExpr = (Expr) visit(ctx.expr());
                returnExpr.setReadOnly();
                MethodReturn methodReturn = new MethodReturn(methodName, parameters, statementList, returnType, returnExpr, mkExternalProperty(ctx, "robProcedures_defreturn"));
                if ( userDefinedFunctionNames.contains(methodName) ) {
                    methodReturn.addTextlyError("Function name '" + methodName + "' is already defined", true);
                    return (T) methodReturn;
                } else {
                    userDefinedFunctionNames.add(methodName);
                    return (T) methodReturn;
                }

            } else {
                MethodVoid methodVoid = new MethodVoid(methodName, parameters, statementList, mkExternalProperty(ctx, "robProcedures_defnoreturn"));
                if ( userDefinedFunctionNames.contains(methodName) ) {
                    methodVoid.addTextlyError("Function name '" + methodName + "' is already defined", true);
                    return (T) methodVoid;
                } else {
                    userDefinedFunctionNames.add(methodName);
                    return (T) methodVoid;
                }
            }

        } else {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid name for Function", true);
            return (T) result;
        }

    }

    /**
     * @return AST instance for Main part in the complete program representation
     */
    @Override
    public T visitMainFunc(TextlyJavaParser.MainFuncContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList();
        for ( ParseTree stmt : ctx.statementList().children ) {
            if ( stmt instanceof TextlyJavaParser.StmtContext ) {
                Stmt statement = (Stmt) visit(stmt);
                statement.setReadOnly();
                statementList.addStmt(statement);
            } else if ( stmt instanceof TextlyJavaParser.CommentStatementContext ) {
                Stmt statement = (Stmt) visit(stmt);
                statement.setReadOnly();
                statementList.addStmt(statement);
            }
        }
        return (T) statementList;
    }

    /**
     * @return AST instance for variable declarations in the complete program representation
     */
    @Override
    public T visitVariableDeclaration(TextlyJavaParser.VariableDeclarationContext ctx) throws UnsupportedOperationException {

        String typeAsString = ctx.nameDecl().start.getText();
        BlocklyType type = BlocklyType.getByBlocklyName(textlyBlocklyType.getBlocklyType(typeAsString));
        Expr expr = (Expr) visit(ctx.expr());
        if ( type.isArray() ) {
            expr.setReadOnly();
            expr = new ListCreate(type.getMatchingElementTypeForArrayType(), (ExprList) expr, mkExternalProperty(ctx, "robLists_create_with"));
        }
        String nameVar = ctx.nameDecl().stop.getText();
        VarDeclaration var = checkValidationName(new VarDeclaration(type, nameVar, expr, true, true, mkVariableDeclProperty(ctx, "robGlobalVariables_declare")), nameVar, NameType.VAR);

        if ( declaredVariableNames.contains(nameVar) ) {
            var.addTextlyError("The variable name is duplicated", true);
        }
        var.setReadOnly();
        declaredVariableNames.add(nameVar);
        return (T) var;
    }

    @Override
    public T visitCommentLine(TextlyJavaParser.CommentLineContext ctx) {
        String comment = ctx.getText().replace("//", "");
        StmtTextComment stmtTextComment = new StmtTextComment(mkInlineProperty(ctx, "text_comment"), comment);
        return (T) stmtTextComment;
    }

    /**
     * @return Phrase instance complete program representation
     */
    @Override
    public T visitProgram(TextlyJavaParser.ProgramContext ctx) {
        collectFunctionSignatures(ctx);
        List<Phrase> phrases = new ArrayList<>();
        StmtList variablesDec = new StmtList();

        for ( TextlyJavaParser.DeclarationContext decla : ctx.declaration() ) {
            ExprStmt statement = new ExprStmt((Expr) visit(decla));
            variablesDec.addStmt(statement);
        }
        variablesDec.setReadOnly();
        MainTask main = new MainTask(mkMainTaskProperty(ctx, "robControls_start"), variablesDec, "TRUE", null);
        phrases.add(main);

        StmtList statements = (StmtList) visitMainFunc((TextlyJavaParser.MainFuncContext) ctx.mainBlock());
        phrases.add(statements);

        for ( TextlyJavaParser.UserFuncContext funcUserContext : ctx.userFunc() ) {
            Method methodStmt = (Method) visit(funcUserContext);
            phrases.add(methodStmt);
        }
        return (T) phrases;
    }

    /**
     * @return the AST for call a function expression defined by the user
     */
    @Override
    public T visitUserDefCall(TextlyJavaParser.UserDefCallContext ctx) {

        String oraMethodName = ctx.NAME().getText();

        ExprList oraparameters = new ExprList();
        ExprList oraParametersValues = new ExprList();
        if ( validatePattern(oraMethodName, NameType.FUNCTIONNAME) ) {
            List<ParameterInfo> parameterInfoList = functionSignatures.get(oraMethodName);
            if ( parameterInfoList == null ) {
                Expr result = new EmptyExpr(BlocklyType.NOTHING);
                result.addTextlyError("Invalid function name '" + oraMethodName + "' - function not defined.", true);
                return (T) result;
            }
            if ( ctx.expr().size() != parameterInfoList.size() ) {
                Expr result = new EmptyExpr(BlocklyType.NOTHING);
                result.addTextlyError("Incorrect number of arguments for function '" + oraMethodName + "'. Expected "
                    + parameterInfoList.size() + " but got " + ctx.expr().size(), true);
                return (T) result;
            }
            int paramIndex = 0;
            for ( TextlyJavaParser.ExprContext expr : ctx.expr() ) {
                Expr param = (Expr) visit(expr);
                ParameterInfo paramInfo = parameterInfoList.get(paramIndex);
                Var parametar = new Var(paramInfo.type, paramInfo.name, BlocklyProperties.make("PARAMETER", "1", null));
                oraparameters.addExpr(parametar);
                oraParametersValues.addExpr(param);
                paramIndex++;
            }
            oraparameters.setReadOnly();
            oraParametersValues.setReadOnly();
            BlocklyType oraReturnType = BlocklyType.NUMBER;

            MethodCall methodCall = new MethodCall(oraMethodName, oraparameters, oraParametersValues, oraReturnType, mkInlineProperty(ctx, "robProcedures_callreturn"));
            MethodExpr methodExpr = new MethodExpr(methodCall);
            return (T) methodExpr;
        } else {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid name for Function", true);
            return (T) result;
        }
    }

    /**
     * @return the AST of IF-Return statement for methods defined by the user
     */
    @Override
    public T visitUserFuncIfStmt(TextlyJavaParser.UserFuncIfStmtContext ctx) {
        Expr oraCondition = (Expr) visit(ctx.expr(0));
        Expr oraReturnValue;
        BlocklyType oraReturnType;
        oraCondition.setReadOnly();
        BigInteger value = new BigInteger("1");
        if ( ctx.NAME() == null ) {
            oraReturnValue = (Expr) visit(ctx.expr(1));
            oraReturnValue.setReadOnly();
            oraReturnType = oraReturnValue.getBlocklyType();
            MethodIfReturn methodIfReturn = new MethodIfReturn(oraCondition, oraReturnType, oraReturnValue, value, mkExternalProperty(ctx, "robProcedures_ifreturn"));
            MethodStmt methodStmt = new MethodStmt(methodIfReturn);
            return (T) methodStmt;
        } else {
            String nameVar = ctx.NAME().getText();
            Var returnVar = checkValidationName(new Var(BlocklyType.VOID, nameVar, mkPropertyFromClass(ctx, Var.class)), nameVar, NameType.VAR);
            oraReturnType = returnVar.getBlocklyType();
            MethodIfReturn methodIfReturn = new MethodIfReturn(oraCondition, oraReturnType, returnVar, value, mkExternalProperty(ctx, "robProcedures_ifreturn"));
            MethodStmt methodStmt = new MethodStmt(methodIfReturn);
            return (T) methodStmt;
        }


    }

    /**
     * @return the AST for call a function statement defined by the user
     */
    @Override
    public T visitStmtUsedDefCall(TextlyJavaParser.StmtUsedDefCallContext ctx) {
        String oraMethodName = ctx.NAME().getText();
        ExprList oraparameters = new ExprList();
        ExprList oraParametersValues = new ExprList();
        List<ParameterInfo> parameterInfoList = functionSignatures.get(oraMethodName);
        if ( parameterInfoList == null ) {
            StmtList errorStmtList = new StmtList(ctx);
            errorStmtList.addTextlyError("Function '" + oraMethodName + "' is not defined", true);
            return (T) errorStmtList;
        }
        if ( ctx.expr().size() != parameterInfoList.size() ) {
            StmtList errorStmtList = new StmtList(ctx);
            errorStmtList.addTextlyError("Incorrect number of arguments for function '" + oraMethodName + "'. Expected "
                + parameterInfoList.size() + " but got " + ctx.expr().size(), true);
            return (T) errorStmtList;
        }
        if ( validatePattern(oraMethodName, NameType.FUNCTIONNAME) ) {
            for ( int paramIndex = 0; paramIndex < ctx.expr().size(); paramIndex++ ) {
                Expr paramValue = (Expr) visit(ctx.expr(paramIndex));
                ParameterInfo paramInfo = parameterInfoList.get(paramIndex);


                Var paramVar = new Var(paramInfo.type, paramInfo.name, BlocklyProperties.make("PARAMETER", "1", null));
                oraparameters.addExpr(paramVar);
                oraParametersValues.addExpr(paramValue);
            }
            oraparameters.setReadOnly();
            oraParametersValues.setReadOnly();
            BlocklyType oraReturnType = BlocklyType.VOID;

            MethodCall methodCall = new MethodCall(oraMethodName, oraparameters, oraParametersValues, oraReturnType, mkInlineProperty(ctx, "robProcedures_callnoreturn"));
            MethodStmt methodStmt = new MethodStmt(methodCall);
            return (T) methodStmt;
        } else {
            Expr result = new EmptyExpr(BlocklyType.NOTHING);
            result.addTextlyError("Invalid name for Function", true);
            return (T) result;
        }
    }

    /**
     * @return AST instance Robot Statement
     */
    @Override
    public T visitRobotStatement(TextlyJavaParser.RobotStatementContext ctx) {
        return visit(ctx.robotStmt());
    }

    /**
     * @return AST instance for Robot Statement Specific functions
     */
    @Override
    public T visitRobotStmt(TextlyJavaParser.RobotStmtContext ctx) {
        return visit(ctx.children.get(0));
    }

    /**
     * @return an empty StmtList for the specific  microbitv2 Sensor.
     *     If this sensor is used with a different robot, the method will return an empty list.
     *     However, if the sensor corresponds to the correct robot, this method will be overridden
     *     in @Microbitv2TextlyJavaVisitor and the corresponding AST will be generated.
     */
    @Override
    public T visitRobotMicrobitv2SensorStatement(TextlyJavaParser.RobotMicrobitv2SensorStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList(ctx);
        statementList.setReadOnly();
        statementList.addTextlyError("Specific sensor microbitv2 Statement", true);
        return (T) statementList;
    }

    /**
     * @return an empty StmtList for the specific  microbitv2 actuator.
     *     If this actuator is used with a different robot, the method will return an empty list.
     *     However, if the actuator corresponds to the correct robot, this method will be overridden
     *     in @Microbitv2TextlyJavaVisitor and the corresponding AST will be generated.
     */
    @Override
    public T visitRobotMicrobitv2ActuatorStatement(TextlyJavaParser.RobotMicrobitv2ActuatorStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList(ctx);
        statementList.setReadOnly();
        statementList.addTextlyError("This actuator is specific for MicrobitV2 robot", true);
        return (T) statementList;
    }

    /**
     * @return an empty StmtList for the specific WeDo Sensor
     *     If this sensor is used with a different robot, the method will return an empty list.
     *     However, if the sensor corresponds to the correct robot, this method will be overridden
     *     in @WeDoTextlyJavaVisitor and the corresponding AST will be generated.
     */
    @Override
    public T visitRobotWeDoSensorStatement(TextlyJavaParser.RobotWeDoSensorStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList(ctx);
        statementList.setReadOnly();
        statementList.addTextlyError("This Sensor is specific for WeDo robot", true);
        return (T) statementList;

    }

    /**
     * @return an empty StmtList for the specific WeDo actuator
     *     If this actuator is used with a different robot, the method will return an empty list.
     *     However, if the actuator corresponds to the correct robot, this method will be overridden
     *     in @WeDoTextlyJavaVisitor and the corresponding AST will be generated.
     */
    @Override
    public T visitRobotWedoActuatorStatement(TextlyJavaParser.RobotWedoActuatorStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList(ctx);
        statementList.setReadOnly();
        statementList.addTextlyError("This actuator is specific for WeDo robot", true);
        return (T) statementList;
    }

    /**
     * @return an empty StmtList for the specific  Ev3 Sensor.
     *     If this sensor is used with a different robot, the method will return an empty list.
     *     However, if the sensor corresponds to the correct robot, this method will be overridden
     *     in @Ev3TextlyJavaVisitor and the corresponding AST will be generated.
     */
    @Override
    public T visitRobotEv3SensorStatement(TextlyJavaParser.RobotEv3SensorStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList(ctx);
        statementList.setReadOnly();
        statementList.addTextlyError("Specific sensor Ev3 Statement", true);
        return (T) statementList;
    }

    /**
     * @return an empty StmtList for the specific  Ev3 actuator.
     *     If this actuator is used with a different robot, the method will return an empty list.
     *     However, if the actuator corresponds to the correct robot, this method will be overridden
     *     in @Ev3TextlyJavaVisitor and the corresponding AST will be generated.
     */
    @Override
    public T visitRobotEv3ActuatorStatement(TextlyJavaParser.RobotEv3ActuatorStatementContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList(ctx);
        statementList.setReadOnly();
        statementList.addTextlyError("This actuator is specific for Ev3 robot", true);
        return (T) statementList;
    }

    /**
     * @return an empty StmtList for the specific  xNN Neural network statements.
     *     If any NN function is used with a different robot, the method will return an empty list.
     *     However, if the xNN robot is used, this method will be overridden
     *     in @Ev3TextlyJavaVisitor and the corresponding AST will be generated.
     */
    @Override
    public T visitRobotEv3NeuralNetworks(TextlyJavaParser.RobotEv3NeuralNetworksContext ctx) throws UnsupportedOperationException {
        StmtList statementList = new StmtList(ctx);
        statementList.setReadOnly();
        statementList.addTextlyError("This neural function is specific for xNN robot", true);
        return (T) statementList;
    }

    private static BlocklyProperties mknullProperty(ParserRuleContext ctx, String type) {
        BlocklyRegion br = new BlocklyRegion(false, false, null, null, null, true, null, null, null);
        return new BlocklyProperties(type, "1", br, null);
    }

    private static BlocklyProperties mkMainTaskProperty(ParserRuleContext ctx, String type) {
        BlocklyRegion br = new BlocklyRegion(false, false, null, false, null, true, null, null, null);
        return new BlocklyProperties(type, "2", br, null);
    }

    private static BlocklyProperties mkVariableDeclProperty(ParserRuleContext ctx, String type) {
        TextRegion rg = ctx == null ? null : new TextRegion(ctx.start.getLine(), ctx.start.getStartIndex(), ctx.stop.getLine(), ctx.stop.getStopIndex());
        BlocklyRegion br = new BlocklyRegion(false, false, null, false, false, true, null, null, null);
        return new BlocklyProperties(type, "2", br, rg);
    }

    @Override
    public T visitChildren(RuleNode node) {
        Expr result = (Expr) super.visitChildren(node);
        result.setReadOnly();
        return (T) result;
    }

    /**
     * This validates the lexer rule NAME, for Variables, function name , button and port.
     */
    public enum NameType {
        VAR(VAR_NAME),
        FUNCTIONNAME(FUNCTION_NAME),
        BUTTON(BUTTONPORT_NAME),
        PORT(BUTTONPORT_NAME),
        EV3PORTNUMB(EV3_PORT_NUMBER),
        EV3PORTNAME(EV3_PORT_NAME);

        private final Pattern pattern;

        NameType(Pattern pattern) {
            this.pattern = pattern;
        }

        public Pattern getPattern() {
            return pattern;
        }
    }

    public boolean validatePattern(String nameText, NameType type) {
        if ( nameText == null ) {
            return false;
        } else {
            return type.getPattern().matcher(nameText).matches();
        }
    }

    public <E extends Phrase> E checkValidationName(E element, String name, NameType type) {

        if ( !validatePattern(name, type) ) {
            element.addTextlyError("Invalid name for " + customizeString(NameType.valueOf(type.name())) + " : " + name, true);
        }
        return element;
    }

    public String customizeString(NameType nameType) {
        String customizedString;

        switch ( nameType.name() ) {
            case "VAR":
                customizedString = "Variable";
                break;
            case "FUNCTIONNAME":
                customizedString = "Function name";
                break;
            case "BUTTON":
            case "PORT":
                customizedString = "Button/Port  ";
                break;
            case "EV3PORTNUMB":
                customizedString = "Port Number ";
            case "EV3PORTNAME":
                customizedString = "Port Name ";
            default:
                customizedString = nameType.name();
                break;
        }
        return customizedString;
    }

    private static BlocklyProperties mkInlineProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", true, ctx);
    }

    private static BlocklyProperties mkExternalProperty(ParserRuleContext ctx, String type) {
        return BlocklyProperties.make(type, "1", false, ctx);
    }

    private <T> BlocklyProperties mkPropertyFromClass(ParserRuleContext ctx, Class<T> clazz) {
        String[] blocklyNames = AnnotationHelper.getBlocklyNamesOfAstClass(clazz);
        if ( blocklyNames.length != 1 ) {
            throw new DbcException("rework that! Too many blockly names to generate an ast object, that can be regenerated as XML");
        }
        return mkExternalProperty(ctx, blocklyNames[0]);
    }

    private enum textlyBlocklyType {
        ARRAY_NUMBER("List<Number>"),
        ARRAY_BOOLEAN("List<Boolean>"),
        ARRAY_STRING("List<String>"),
        ARRAY_COLOUR("List<Colour>"),
        ARRAY_IMAGE("List<Image>"),
        ARRAY_CONNECTION("List<Connection>"),
        NUMBER("Number"),
        STRING("String"),
        BOOLEAN("Boolean"),
        COLOUR("Colour"),
        IMAGE("Image"),
        CONNECTION("Connection");

        private final String name;

        textlyBlocklyType(String name) {
            this.name = name;
        }

        /**
         * Given a textly type return the Blockly type needed in the AST class
         * if the textly name is wrong return null
         *
         * @param textlyName
         * @return the AST mode name
         */
        public static String getBlocklyType(String textlyName) {
            for ( textlyBlocklyType arrayType : values() ) {
                if ( arrayType.name.equalsIgnoreCase(textlyName) ) {
                    return arrayType.name();
                }
            }
            return null;
        }

    }
}



