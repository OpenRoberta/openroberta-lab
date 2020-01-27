package de.fhg.iais.roberta.syntax.lang.expr.eval;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import de.fhg.iais.roberta.exprly.generated.ExprlyLexer;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser;
import de.fhg.iais.roberta.exprly.generated.ExprlyParser.ExpressionContext;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;

public class ExprlyUnParser<T> {
    private String se;
    private final Phrase<T> e;
    private final static Map<FunctionNames, String> fnames;
    private final static Map<MathConst.Const, String> cnames;
    private final static Map<Binary.Op, String> bnames;
    private final static Map<Unary.Op, String> unames;
    private final static Map<IMode, String> imodes;
    private final static Map<ListElementOperations, String> listOperations;

    /**
     * Class constructor, creates an instance of {@link ExprlyUnParser} for the phrase pased as parameter
     *
     * @param Phrase that will be UnParsed
     **/
    public ExprlyUnParser(Phrase<T> ast) {
        this.se = "";
        this.e = ast;
    }

    /**
     * @return textual representation of expression
     */
    public String getString() {
        return this.se;
    }

    /**
     * Method to UnParse the expression in the class and remove redundant parenthese
     *
     * @return UnParsed expression
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public String fullUnParse() {
        this.UnParse();
        try {
            return this.removeRedundantParenthese();
        } catch ( Exception e ) {
            return getString();
        }
    }

    /**
     * Method to UnParse the expression in the class Note: The expression will have redundant parenthesis Note: It only works reliably on semantically correct
     * expressions, wrong expressions could lead to unwanted results
     *
     * @return UnParsed expression
     */
    public String UnParse() {
        this.se = "";
        this.se += visitAST(this.e);
        return getString();
    }

    /**
     * Method to remove redundant parenthese from the unparsed expression
     *
     * @return UnParsed expression
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private String removeRedundantParenthese() throws UnsupportedEncodingException, IOException {
        String red = this.se;
        Stack<Integer> stack = new Stack<>();
        HashSet<ArrayList<Integer>> pairs = new HashSet<>();
        for ( int k = 0; k < red.length(); k++ ) {
            if ( red.charAt(k) == '(' ) {
                stack.push(k);
            } else if ( red.charAt(k) == ')' ) {
                ArrayList<Integer> pair = new ArrayList<>();
                pair.add(stack.pop());
                pair.add(k);
                pairs.add(pair);
            }
        }
        for ( ArrayList<Integer> p : pairs ) {
            String test = red.substring(0, p.get(0)) + " " + red.substring(p.get(0) + 1, p.get(1)) + " " + red.substring(p.get(1) + 1, red.length());
            ExprlyParser parser = mkParser(test);
            parser.removeErrorListeners();
            ExprlyVisitor<T> eval = new ExprlyVisitor<>();
            ExpressionContext expression = parser.expression();
            if ( parser.getNumberOfSyntaxErrors() == 0 ) {
                ExprlyUnParser<T> unparser = new ExprlyUnParser<>(eval.visitExpression(expression));
                if ( unparser.UnParse().equals(this.se) ) {
                    red = test;
                }
            }
        }
        return red;
    }

    /**
     * @param NumberConst Expression
     * @return Textual representation of the NumberConst
     */
    public String visitNumConst(NumConst<T> numConst) {
        return numConst.getValue();
    }

    /**
     * @param MathConst Expression
     * @return Textual representation of the MathConst
     */
    public String visitMathConst(MathConst<T> mathConst) {
        return ExprlyUnParser.cnames.get(mathConst.getMathConst());
    }

    /**
     * @param BoolConst Expression
     * @return Textual representation of the BoolConst
     */
    public String visitBoolConst(BoolConst<T> boolConst) {
        return Boolean.toString(boolConst.getValue());
    }

    /**
     * @param StringConst Expression
     * @return Textual representation of the StringConst
     */
    public String visitStringConst(StringConst<T> stringConst) {
        return "\"" + stringConst.getValue() + "\"";
    }

    /**
     * @param ColorConst Expression
     * @return Textual representation of the ColorConst
     */
    public String visitColorConst(ColorConst<T> colorConst) {
        return colorConst.getHexValueAsString();
    }

    /**
     * @param RgbColor Expression
     * @return Textual representation of the RGB Color
     */
    public String visitRgbColor(RgbColor<T> rgbColor) {
        if ( rgbColor.getA() instanceof EmptyExpr<?> ) {
            return "getRGB(" + visitAST(rgbColor.getR()) + "," + visitAST(rgbColor.getG()) + "," + visitAST(rgbColor.getB()) + ")";
        } else {
            return "getRGB("
                + visitAST(rgbColor.getR())
                + ","
                + visitAST(rgbColor.getG())
                + ","
                + visitAST(rgbColor.getB())
                + ","
                + visitAST(rgbColor.getA())
                + ")";
        }
    }

    /**
     * @param ConnectConst Expression
     * @return Textual representation of the ConnectConst
     */
    public String visitConnectConst(ConnectConst<T> connectConst) {
        return "connect " + connectConst.getValue() + "," + connectConst.getValue(); // TODO: inspect generation. Was: connectConst.getDataValue()
    }

    /**
     * @param Var Expression
     * @return Textual representation of The Var value
     */
    public String visitVar(Var<T> var) {

        return var.getValue();
    }

    /**
     * @param Empty Expression
     * @return Empty String
     */
    private String visitEmptyExpr(EmptyExpr<T> emptyExpr) {
        return "";
    }

    /**
     * @param Null Expression
     * @return Textual representation of null
     */
    private String visitNullConst(NullConst<T> nullConst) {
        return "null";
    }

    /**
     * Method to UnParse Unary expressions
     *
     * @param Unary Expression
     * @return Textual representation of the operation
     */
    public String visitUnary(Unary<T> unary) throws UnsupportedOperationException {
        return ExprlyUnParser.unames.get(unary.getOp()) + "(" + visitAST(unary.getExpr()) + ")";
    }

    /**
     * Method to UnParse Binary expressions
     *
     * @param Binary Expression
     * @return Textual representation of the operation
     */
    public String visitBinary(Binary<T> binary) throws UnsupportedOperationException {
        return "(" + visitAST(binary.getLeft()) + ")" + ExprlyUnParser.bnames.get(binary.getOp()) + "(" + visitAST(binary.getRight()) + ")";
    }

    /**
     * Method to UnParse List expressions
     *
     * @param List Expression
     * @return Textual representation of List
     */
    public String visitExprList(ExprList<T> list) {
        // Get list of expressions in the list
        List<Expr<T>> eList = list.get();
        String t = "[";
        for ( Expr<T> e : eList ) {
            t = t + visitAST(e) + ",";
        }
        if ( t.length() == 1 ) {
            return "[]";
        }
        return t.substring(0, t.length() - 1) + "]";
    }

    /**
     * @param Function Expression
     * @return Textual representation of function
     */
    public String visitFunctionExpr(FunctionExpr<T> funct) {
        return visitAST(funct.getFunction());
    }

    /**
     * Method to UnParse MathNumPropFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public String visitMathNumPropFunct(MathNumPropFunct<T> mathNumPropFunct) {
        return ExprlyUnParser.fnames.get(mathNumPropFunct.getFunctName()) + "(" + paramList(mathNumPropFunct.getParam()) + ")";

    }

    /**
     * Method to UnParse MathOnListFunctions.
     *
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathOnListFunct(MathOnListFunct<T> mathOnListFunct) {
        return ExprlyUnParser.fnames.get(mathOnListFunct.getFunctName()) + "(" + paramList(mathOnListFunct.getParam()) + ")";
    }

    /**
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathRandomFloatFunct(MathRandomFloatFunct<T> mathRandomFloatFunct) {
        return "randFloat()";
    }

    /**
     * Method to UnParse MathRandomIntFunctions
     *
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathRandomIntFunct(MathRandomIntFunct<T> mathRandomIntFunct) {
        return "randInt" + "(" + paramList(mathRandomIntFunct.getParam()) + ")";
    }

    /**
     * Method to UnParse MathSingleFunctions
     *
     * @param Function
     * @return Textual representation of function
     */
    public String visitMathSingleFunct(MathSingleFunct<T> mathSingleFunct) {
        // Get parameters
        List<Expr<T>> args = mathSingleFunct.getParam();
        String fname = ExprlyUnParser.fnames.get(mathSingleFunct.getFunctName());
        if ( fname.equals("pow") ) {
            return "(" + visitAST(args.get(0)) + ")" + "^" + "(" + visitAST(args.get(1)) + ")";
        } else {
            fname += "(";
            for ( int i = 0; i < args.size(); i++ ) {
                fname += visitAST(args.get(i));
                if ( i != args.size() - 1 ) {
                    fname += ",";
                }
            }
            fname += ")";
            return fname;
        }
    }

    /**
     * Method to UnParse MathRandomIntFunctions
     *
     * @param Function
     * @return Textual representation of function
     */
    private String visitIndexOfFunct(IndexOfFunct<T> indexOfFunct) {
        List<Expr<T>> args = indexOfFunct.getParam();
        if ( indexOfFunct.getLocation().equals(IndexLocation.FIRST) ) {
            return "indexOfFirst(" + paramList(args) + ")";
        } else if ( indexOfFunct.getLocation().equals(IndexLocation.LAST) ) {
            return "indexOfLast(" + paramList(args) + ")";
        }
        throw new UnsupportedOperationException("Not supported Index mode for IndexOfFunct");
    }

    /**
     * Method to UnParse MathPowerFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitMathPowerFunct(MathPowerFunct<T> mathPowerFunct) {
        List<Expr<T>> args = mathPowerFunct.getParam();
        return "(" + visitAST(args.get(0)) + ")^(" + visitAST(args.get(1)) + ")";
    }

    /**
     * Method to UnParse MathConstrainFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitMathConstrainFunct(MathConstrainFunct<T> mathConstrainFunct) {
        return "constrain(" + paramList(mathConstrainFunct.getParam()) + ")";
    }

    /**
     * Method to UnParse TextJoinFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitTextJoinFunct(TextJoinFunct<T> textJoinFunct) {
        return "createTextWith(" + paramList(textJoinFunct.getParam().get()) + ")";
    }

    /**
     * Method to UnParse print
     *
     * @param Function
     * @return Textual representation of the method
     */
    private String visitTextPrintFunct(TextPrintFunct<T> textPrintFunct) {
        return "print(" + paramList(textPrintFunct.getParam()) + ")";
    }

    /**
     * Method to UnParse ListRepeatFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitListRepeat(ListRepeat<T> listRepeat) {
        return "repeatList(" + paramList(listRepeat.getParam()) + ")";
    }

    /**
     * Method to UnParse ListGetFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitListGetIndex(ListGetIndex<T> listGetIndex) {
        return listOperations.get(listGetIndex.getElementOperation())
            + "Index"
            + imodes.get(listGetIndex.getLocation())
            + "("
            + paramList(listGetIndex.getParam())
            + ")";
    }

    /**
     * Method to UnParse ListSetFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitListSetIndex(ListSetIndex<T> listSetIndex) {
        return listOperations.get(listSetIndex.getElementOperation())
            + "Index"
            + imodes.get(listSetIndex.getLocation())
            + "("
            + paramList(listSetIndex.getParam())
            + ")";
    }

    /**
     * Method to UnParse LengthOfIsEmptyFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<T> lengthOfIsEmptyFunct) {
        FunctionNames fname = lengthOfIsEmptyFunct.getFunctName();
        if ( fname.equals(FunctionNames.LIST_LENGTH) ) {
            return "lengthOf(" + paramList(lengthOfIsEmptyFunct.getParam()) + ")";
        } else if ( fname.equals(FunctionNames.LIST_IS_EMPTY) ) {
            return "isEmpty(" + paramList(lengthOfIsEmptyFunct.getParam()) + ")";
        }
        return null;
    }

    /**
     * Method to UnParse GetSubFunction
     *
     * @param Function
     * @return Textual representation of the function
     */
    private String visitGetSubFunct(GetSubFunct<T> getSubFunct) {
        List<IMode> mode = getSubFunct.getStrParam();
        String s = "subList";
        if ( !(mode.get(0).equals(IndexLocation.FROM_START) && mode.get(1).equals(IndexLocation.FROM_START)) ) {
            if ( mode.get(0).equals(IndexLocation.FROM_START) ) {
                s += "FromIndex";
            } else if ( mode.get(0).equals(IndexLocation.FIRST) ) {
                s += "FromFirst";
            } else if ( mode.get(0).equals(IndexLocation.FROM_END) ) {
                s += "FromEnd";
            }

            if ( mode.get(1).equals(IndexLocation.FROM_START) ) {
                s += "ToIndex";
            } else if ( mode.get(1).equals(IndexLocation.LAST) ) {
                s += "ToLast";
            } else if ( mode.get(1).equals(IndexLocation.FROM_END) ) {
                s += "ToEnd";
            }
        }
        return s += "(" + paramList(getSubFunct.getParam()) + ")";
    }

    /**
     * Method to unparse the ternaryOperator.
     *
     * @param operation
     * @return Return textual representation of the operation
     */
    private String visitIfStmt(IfStmt<T> ifStmt) {
        return "(("
            + visitAST(ifStmt.getExpr().get(0))
            + ")?("
            + visitAST(((ExprStmt<T>) ifStmt.getThenList().get(0).get().get(0)).getExpr())
            + "):("
            + visitAST(((ExprStmt<T>) ifStmt.getElseList().get().get(0)).getExpr())
            + "))";
    }

    /**
     * Method to unparse exprStmts.
     *
     * @param expression stmt
     * @return Return textual representation of expr
     */
    @SuppressWarnings("unchecked")
    private String visitStmtExpr(StmtExpr<?> stmtExpr) {
        return visitAST((Phrase<T>) stmtExpr.getStmt());
    }

    /**
     * Method to UnParse any expression
     *
     * @param ast, expression to unparse
     * @return Textual representation of the AST
     */
    public String visitAST(Phrase<T> ast) throws UnsupportedOperationException {
        if ( ast instanceof Binary<?> ) {
            return visitBinary((Binary<T>) ast);
        }
        if ( ast instanceof Unary<?> ) {
            return visitUnary((Unary<T>) ast);
        }
        if ( ast instanceof MathConst<?> ) {
            return visitMathConst((MathConst<T>) ast);
        }
        if ( ast instanceof NumConst<?> ) {
            return visitNumConst((NumConst<T>) ast);
        }
        if ( ast instanceof BoolConst<?> ) {
            return visitBoolConst((BoolConst<T>) ast);
        }
        if ( ast instanceof StringConst<?> ) {
            return visitStringConst((StringConst<T>) ast);
        }
        if ( ast instanceof ColorConst<?> ) {
            return visitColorConst((ColorConst<T>) ast);
        }
        if ( ast instanceof RgbColor<?> ) {
            return visitRgbColor((RgbColor<T>) ast);
        }
        if ( ast instanceof ConnectConst<?> ) {
            return visitConnectConst((ConnectConst<T>) ast);
        }
        if ( ast instanceof Var<?> ) {
            return visitVar((Var<T>) ast);
        }
        if ( ast instanceof ExprList<?> ) {
            return visitExprList((ExprList<T>) ast);
        }
        if ( ast instanceof FunctionExpr<?> ) {
            return visitFunctionExpr((FunctionExpr<T>) ast);
        }
        if ( ast instanceof MathNumPropFunct<?> ) {
            return visitMathNumPropFunct((MathNumPropFunct<T>) ast);
        }
        if ( ast instanceof MathOnListFunct<?> ) {
            return visitMathOnListFunct((MathOnListFunct<T>) ast);
        }
        if ( ast instanceof MathRandomFloatFunct<?> ) {
            return visitMathRandomFloatFunct((MathRandomFloatFunct<T>) ast);
        }
        if ( ast instanceof MathRandomIntFunct<?> ) {
            return visitMathRandomIntFunct((MathRandomIntFunct<T>) ast);
        }
        if ( ast instanceof MathSingleFunct<?> ) {
            return visitMathSingleFunct((MathSingleFunct<T>) ast);
        }
        if ( ast instanceof ListCreate<?> ) {
            return visitExprList(((ListCreate<T>) ast).getValue());
        }
        if ( ast instanceof LengthOfIsEmptyFunct<?> ) {
            return visitLengthOfIsEmptyFunct((LengthOfIsEmptyFunct<T>) ast);
        }
        if ( ast instanceof ListSetIndex<?> ) {
            return visitListSetIndex((ListSetIndex<T>) ast);
        }
        if ( ast instanceof ListGetIndex<?> ) {
            return visitListGetIndex((ListGetIndex<T>) ast);
        }
        if ( ast instanceof ListRepeat<?> ) {
            return visitListRepeat((ListRepeat<T>) ast);
        }
        if ( ast instanceof GetSubFunct<?> ) {
            return visitGetSubFunct((GetSubFunct<T>) ast);
        }
        if ( ast instanceof TextPrintFunct<?> ) {
            return visitTextPrintFunct((TextPrintFunct<T>) ast);
        }
        if ( ast instanceof TextJoinFunct<?> ) {
            return visitTextJoinFunct((TextJoinFunct<T>) ast);
        }
        if ( ast instanceof MathConstrainFunct<?> ) {
            return visitMathConstrainFunct((MathConstrainFunct<T>) ast);
        }
        if ( ast instanceof MathPowerFunct<?> ) {
            return visitMathPowerFunct((MathPowerFunct<T>) ast);
        }
        if ( ast instanceof NullConst<?> ) {
            return visitNullConst((NullConst<T>) ast);
        }
        if ( ast instanceof EmptyExpr<?> ) {
            return visitEmptyExpr((EmptyExpr<T>) ast);
        }
        if ( ast instanceof IndexOfFunct<?> ) {
            return visitIndexOfFunct((IndexOfFunct<T>) ast);
        }
        if ( ast instanceof StmtExpr<?> ) {
            return visitStmtExpr((StmtExpr<T>) ast);
        }
        if ( ast instanceof IfStmt<?> ) {
            return visitIfStmt((IfStmt<T>) ast);
        }
        throw new UnsupportedOperationException("Expression " + ast.toString() + "cannot be checked");
    }

    /**
     * Function that builds a string representation of the arguments of a function
     *
     * @param param list
     * @return textual representation of the args
     */
    public String paramList(List<Expr<T>> args) {
        String t = "";
        for ( Expr<T> e : args ) {
            t += visitAST(e) + ",";
        }
        return t.substring(0, t.length() - 1);
    }

    /**
     * Function to create the parser for the expression
     */
    private static ExprlyParser mkParser(String expr) throws UnsupportedEncodingException, IOException {
        InputStream inputStream = new ByteArrayInputStream(expr.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(inputStream);
        ExprlyLexer lexer = new ExprlyLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprlyParser parser = new ExprlyParser(tokens);
        return parser;
    }

    // Fill the HashMap of Function Names
    static {
        HashMap<FunctionNames, String> names = new HashMap<>();
        names.put(FunctionNames.MAX, "max");
        names.put(FunctionNames.MIN, "min");
        names.put(FunctionNames.EVEN, "isEven");
        names.put(FunctionNames.ODD, "isOdd");
        names.put(FunctionNames.PRIME, "isPrime");
        names.put(FunctionNames.WHOLE, "isWhole");
        names.put(FunctionNames.POSITIVE, "isPositive");
        names.put(FunctionNames.NEGATIVE, "isNegative");
        names.put(FunctionNames.DIVISIBLE_BY, "isDivisibleBy");
        names.put(FunctionNames.LIST_IS_EMPTY, "isEmpty");
        names.put(FunctionNames.SUM, "sum");
        names.put(FunctionNames.AVERAGE, "avg");
        names.put(FunctionNames.MEDIAN, "median");
        names.put(FunctionNames.STD_DEV, "sd");
        names.put(FunctionNames.SQUARE, "square");
        names.put(FunctionNames.ROOT, "sqrt");
        names.put(FunctionNames.ABS, "abs");
        names.put(FunctionNames.LN, "ln");
        names.put(FunctionNames.LOG10, "log10");
        names.put(FunctionNames.EXP, "exp");
        names.put(FunctionNames.SIN, "sin");
        names.put(FunctionNames.COS, "cos");
        names.put(FunctionNames.TAN, "tan");
        names.put(FunctionNames.ASIN, "asin");
        names.put(FunctionNames.ACOS, "acos");
        names.put(FunctionNames.ATAN, "atan");
        names.put(FunctionNames.POWER, "pow");
        names.put(FunctionNames.ROUNDUP, "roundUp");
        names.put(FunctionNames.ROUNDDOWN, "roundDown");
        names.put(FunctionNames.ROUND, "round");
        names.put(FunctionNames.RANDOM, "randItem");
        fnames = Collections.unmodifiableMap(names);
    }

    // Fill the HashMap of Math Const Names
    static {
        HashMap<MathConst.Const, String> names = new HashMap<>();
        names.put(MathConst.Const.GOLDEN_RATIO, "phi");
        names.put(MathConst.Const.PI, "pi");
        names.put(MathConst.Const.E, "e");
        names.put(MathConst.Const.SQRT2, "sqrt2");
        names.put(MathConst.Const.SQRT1_2, "sqrt_1_2");
        names.put(MathConst.Const.INFINITY, "inf");
        cnames = Collections.unmodifiableMap(names);
    }

    // Fill the HashMap of Binary Operation Names
    static {
        HashMap<Binary.Op, String> names = new HashMap<>();
        names.put(Binary.Op.ADD, "+");
        names.put(Binary.Op.MINUS, "-");
        names.put(Binary.Op.MULTIPLY, "*");
        names.put(Binary.Op.DIVIDE, "/");
        names.put(Binary.Op.MOD, "%");
        names.put(Binary.Op.AND, "&&");
        names.put(Binary.Op.OR, "||");
        names.put(Binary.Op.EQ, "==");
        names.put(Binary.Op.NEQ, "!=");
        names.put(Binary.Op.GT, ">");
        names.put(Binary.Op.LT, "<");
        names.put(Binary.Op.GTE, ">=");
        names.put(Binary.Op.LTE, "<=");
        bnames = Collections.unmodifiableMap(names);
    }

    // Fill the HashMap of Unary Operation Names
    static {
        HashMap<Unary.Op, String> names = new HashMap<>();
        names.put(Unary.Op.PLUS, "+");
        names.put(Unary.Op.NEG, "-");
        names.put(Unary.Op.NOT, "!");
        unames = Collections.unmodifiableMap(names);
    }

    // Fill the HashMap of Unary Operation Names
    static {
        HashMap<IMode, String> modes = new HashMap<>();
        modes.put(IndexLocation.FIRST, "First");
        modes.put(IndexLocation.LAST, "Last");
        modes.put(IndexLocation.FROM_START, "");
        modes.put(IndexLocation.FROM_END, "FromEnd");
        imodes = Collections.unmodifiableMap(modes);
    }

    // Fill the HashMap of Unary Operation Names
    static {
        HashMap<ListElementOperations, String> operations = new HashMap<>();
        operations.put(ListElementOperations.SET, "set");
        operations.put(ListElementOperations.INSERT, "insert");
        operations.put(ListElementOperations.GET, "get");
        operations.put(ListElementOperations.REMOVE, "remove");
        operations.put(ListElementOperations.GET_REMOVE, "getAndRemove");
        listOperations = Collections.unmodifiableMap(operations);
    }
}
