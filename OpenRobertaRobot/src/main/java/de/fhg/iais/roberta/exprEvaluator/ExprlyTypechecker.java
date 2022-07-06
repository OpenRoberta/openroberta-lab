package de.fhg.iais.roberta.exprEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
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
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

public class ExprlyTypechecker {

    private final LinkedList<TcError> errors;
    private final Phrase ast;
    private final String robotName;
    private BlocklyType resultType;
    private final BlocklyType expectedResultType;
    private final List<VarDeclaration> vars;
    private static Map<String, Set<String>> okMap; // This is a map of special restrictions for each robot

    /**
     * Class constructor, creates an instance of {@link ExprlyTypechecker} for the phrase passed as parameter
     *
     * @param Phrase that will be checked
     * @param type of expected return type
     * @param list of already declared variables
     * @param name of robot
     **/
    public ExprlyTypechecker(Phrase ast, BlocklyType rt, List<VarDeclaration> vars, String robotName) {
        this.errors = new LinkedList();
        this.expectedResultType = rt;
        this.ast = ast;
        this.vars = vars;
        this.robotName = robotName;
    }

    /**
     * Class constructor, creates an instance of {@link ExprlyTypechecker} for the phrase passed as parameter. This constructor is used for testing purposes
     *
     * @param Phrase that will be checked
     * @param Expected return type
     **/
    public ExprlyTypechecker(Phrase ast, BlocklyType rt) {
        this.errors = new LinkedList();
        this.expectedResultType = rt;
        this.ast = ast;
        this.vars = null;
        this.robotName = null;
    }

    /**
     * @return list of errors detected
     **/
    public List<TcError> getErrors() {
        return this.errors;
    }

    /**
     * @return list of errors detected
     **/
    public int getNumErrors() {
        return this.errors.size();
    }

    /**
     * @return BlocklyType of the whole expression
     */
    public BlocklyType getResultType() {
        return this.resultType;
    }

    /**
     * @return Expected BlocklyType for the whole expression
     */
    public BlocklyType getExpectedResultType() {
        return this.expectedResultType;
    }

    private void addError(TcError.TcErrorMsg key, String error, String value) {
        this.errors.add(TcError.makeError(key, error, value));
    }

    private void addError(TcError.TcErrorMsg key) {
        this.errors.add(TcError.makeError(key));
    }

    /**
     * Method to check the phrase in the class
     */
    public void check() {
        this.resultType = checkAST(this.ast);

        // Check if the expression is an empty list and it's valid
        if ( this.resultType.equals(BlocklyType.ARRAY)
            && (this.expectedResultType.equals(BlocklyType.ARRAY_NUMBER)
            || this.expectedResultType.equals(BlocklyType.ARRAY_BOOLEAN)
            || this.expectedResultType.equals(BlocklyType.ARRAY_STRING)
            || this.expectedResultType.equals(BlocklyType.ARRAY_CONNECTION)) ) {
            if ( this.ast instanceof ListCreate ) {
                if ( ((ListCreate) this.ast).exprList.get().size() == 0 ) {
                    return;
                }
            }
        }

        // Check for return type errors
        if ( !this.resultType.equals(this.expectedResultType) ) {
            if ( this.resultType.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !this.resultType.equals(BlocklyType.VOID) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_RETURN_TYPE);
            }
        }
    }

    /**
     * @param NumberConst Expression
     * @return Type of block
     */
    private BlocklyType visitNumConst(NumConst numConst) {
        return numConst.getVarType();
    }

    /**
     * @param MathConst Expression
     * @return Type of block
     */
    private BlocklyType visitMathConst(MathConst mathConst) {
        return mathConst.getVarType();
    }

    /**
     * @param BoolConst Expression
     * @return Type of block
     */
    private BlocklyType visitBoolConst(BoolConst boolConst) {
        return boolConst.getVarType();
    }

    /**
     * @param StringConst Expression
     * @return Type of block
     */
    private BlocklyType visitStringConst(StringConst stringConst) {
        return stringConst.getVarType();
    }

    /**
     * @param ColorConst Expression
     * @return Type of block
     */
    private BlocklyType visitColorConst(ColorConst colorConst) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noColor") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "color");
            } else if ( !okMap.get(this.robotName).contains(colorConst.getHexValueAsString()) ) {
                addError(TcError.TcErrorMsg.INVALID_COLOR, "COLOR", colorConst.getHexValueAsString());
            }
        }
        return colorConst.getVarType();
    }

    /**
     * @param RgbColorAnno Expression
     * @return Type of block
     */
    private BlocklyType visitRgbColor(RgbColor rgbColor) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noColor") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "color");
                return rgbColor.getVarType();
            } else if ( !okMap.get(this.robotName).contains("rgb") && !okMap.get(this.robotName).contains("rgba") ) {
                addError(TcError.TcErrorMsg.ILLEGAL_RGB);
                return rgbColor.getVarType();
            }
            if ( rgbColor.R instanceof EmptyExpr ) {
                addError(TcError.TcErrorMsg.INVALID_RGB_RGBA, "NUM", okMap.get(this.robotName).contains("rgb") ? "3" : "4");
                return rgbColor.getVarType();
            }
        }

        List<BlocklyType> c = new ArrayList(4);
        c.add(checkAST(rgbColor.R));
        c.add(checkAST(rgbColor.G));
        c.add(checkAST(rgbColor.B));
        c.add(checkAST(rgbColor.A));

        for ( int k = 0; k < c.size(); k++ ) {
            BlocklyType t = c.get(k);
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( !t.equals(BlocklyType.NUMBER) ) {
                    ExprlyUnParser up =
                        new ExprlyUnParser(k == 0 ? rgbColor.R : k == 1 ? rgbColor.G : k == 2 ? rgbColor.B : rgbColor.A);
                    addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                }
            }
        }

        return rgbColor.getVarType();
    }

    /**
     * @param ConnectConst Expression
     * @return Type of block
     */
    private BlocklyType visitConnectConst(ConnectConst connectConst) {
        return connectConst.getVarType();
    }

    /**
     * Note: During testing period, the default Type for Var Types is VOID
     *
     * @param Var Expression
     * @return Type of block
     */
    private BlocklyType visitVar(Var var) {
        if ( this.vars == null ) {
            return BlocklyType.VOID;
        }
        for ( VarDeclaration v : this.vars ) {
            if ( var.name.equals(v.name) ) {
                return v.getVarType();
            }
        }
        addError(TcError.TcErrorMsg.UNDECLARED_VARIABLE, "NAME", var.name);
        return BlocklyType.VOID;
    }

    /**
     * @param Empty Expression
     * @return Void BlocklyType
     */
    private BlocklyType visitEmptyExpr(EmptyExpr emptyExpr) {
        return BlocklyType.VOID;
    }

    /**
     * @param Null Expression
     * @return Void BlocklyType
     */
    private BlocklyType visitNullConst(NullConst nullConst) {
        return BlocklyType.VOID;
    }

    /**
     * Method to check Unary expressions, stores any errors found in the error list.
     *
     * @param Unary Expression
     * @return Return Type of block
     */
    private BlocklyType visitUnary(Unary unary) throws UnsupportedOperationException {

        // Get type of the operand
        BlocklyType t = checkAST(unary.expr);
        if ( t.equals(BlocklyType.NOTHING) ) {
            addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
        }
        // Check if the expression should should be boolean
        if ( unary.op.equals(Unary.Op.NOT) ) {
            // If it should be boolean, check if it is
            if ( !t.equals(BlocklyType.BOOLEAN) && !t.equals(BlocklyType.VOID) && !t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            return BlocklyType.BOOLEAN;

            // Check if it's a number operation
        } else if ( unary.op.equals(Unary.Op.PLUS) || unary.op.equals(Unary.Op.NEG) ) {

            // If it is a number operation, check if the argument is boolean
            if ( !t.equals(BlocklyType.NUMBER) && !t.equals(BlocklyType.VOID) && !t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            return BlocklyType.NUMBER;
        }

        throw new UnsupportedOperationException("Expression " + unary.toString() + "has an invalid operation type");
    }

    /**
     * Method to check Binary expressions, stores any errors found in the error list.
     *
     * @param Binary Expression
     * @return Return Type of block
     */
    private BlocklyType visitBinary(Binary binary) throws UnsupportedOperationException {
        // Get type of the operands
        BlocklyType tl = checkAST(binary.left);
        BlocklyType tr = checkAST(binary.getRight());

        if ( tl.equals(BlocklyType.NOTHING) || tr.equals(BlocklyType.NOTHING) ) {
            addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
        }

        // Check if its a number operation
        if ( binary.op.equals(Binary.Op.ADD)
            || binary.op.equals(Binary.Op.MINUS)
            || binary.op.equals(Binary.Op.MULTIPLY)
            || binary.op.equals(Binary.Op.DIVIDE)
            || binary.op.equals(Binary.Op.MOD) ) {
            // Check if the left operand is a Number Type
            if ( !tl.equals(BlocklyType.NUMBER) && !tl.equals(BlocklyType.VOID) && !tl.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) && !tr.equals(BlocklyType.VOID) && !tr.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            return BlocklyType.NUMBER;
        }
        // Check if the operation is a boolean operation
        if ( binary.op.equals(Binary.Op.AND) || binary.op.equals(Binary.Op.OR) ) {
            // Check if the left operand is a Boolean Type
            if ( !tl.equals(BlocklyType.BOOLEAN) && !tl.equals(BlocklyType.VOID) && !tl.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            if ( !tr.equals(BlocklyType.BOOLEAN) && !tr.equals(BlocklyType.VOID) && !tr.equals(BlocklyType.NOTHING) ) {
                // Check if the right operand is a Boolean Type
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            return BlocklyType.BOOLEAN;
        }
        // Check if it's an equality or inequality operation
        if ( binary.op.equals(Binary.Op.EQ) || binary.op.equals(Binary.Op.NEQ) ) {
            // Check if both operands are of the same Type
            if ( !tl.equals(tr)
                && !tl.equals(BlocklyType.VOID)
                && !tr.equals(BlocklyType.VOID)
                && !tl.equals(BlocklyType.NOTHING)
                && !tr.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            return BlocklyType.BOOLEAN;
        }
        // Check if operation is a inecuation
        if ( binary.op.equals(Binary.Op.GT)
            || binary.op.equals(Binary.Op.LT)
            || binary.op.equals(Binary.Op.GTE)
            || binary.op.equals(Binary.Op.LTE) ) {
            // Check if the left operand is a Number Type
            if ( !tl.equals(BlocklyType.NUMBER) && !tl.equals(BlocklyType.VOID) && !tl.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) && !tr.equals(BlocklyType.VOID) && !tr.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            }
            return BlocklyType.BOOLEAN;
        }

        throw new UnsupportedOperationException("Expression " + binary.toString() + "has an invalid operation type");
    }

    /**
     * Method to check List expressions, stores any errors found in the error list.
     *
     * @param List Expression
     * @return Type of block
     */
    private BlocklyType visitExprList(ExprList list) throws IllegalArgumentException {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            }
        }

        // Get list of expressions in the list
        Boolean correct = true;
        List<Expr> eList = list.get();
        List<BlocklyType> tList = new ArrayList<>(eList.size());
        BlocklyType t = BlocklyType.VOID;
        // Check if it's an empty list, by default we'll return Array type in that case

        if ( eList.size() == 0 ) {
            return BlocklyType.ARRAY;
        } else {
            for ( int k = 0; k < eList.size(); k++ ) {
                tList.add(checkAST(eList.get(k)));
                if ( !tList.get(k).equals(BlocklyType.VOID) ) {

                    t = tList.get(k);
                }
            }
            for ( BlocklyType element : tList ) {
                if ( element.equals(BlocklyType.NOTHING) ) {
                    addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
                    // variable errors are detected previously
                } else if ( !element.equals(BlocklyType.VOID) ) {
                    // If the types are different it's considered an error
                    if ( !element.equals(t) && correct ) {
                        addError(TcError.TcErrorMsg.INVALID_TYPE_FOR_LIST_ELEMENT);
                        correct = false;
                    }
                }
            }
            // Return type of array
            if ( t.equals(BlocklyType.NUMBER) ) {
                return BlocklyType.ARRAY_NUMBER;
            }
            if ( t.equals(BlocklyType.BOOLEAN) ) {
                return BlocklyType.ARRAY_BOOLEAN;
            }
            if ( t.equals(BlocklyType.STRING) ) {
                return BlocklyType.ARRAY_STRING;
            }
            if ( t.equals(BlocklyType.CONNECTION) ) {
                return BlocklyType.ARRAY_CONNECTION;
            }
            if ( t.equals(BlocklyType.COLOR) ) {
                return BlocklyType.ARRAY_COLOUR;
            }
            if ( t.equals(BlocklyType.ARRAY)
                || t.equals(BlocklyType.ARRAY_NUMBER)
                || t.equals(BlocklyType.ARRAY_BOOLEAN)
                || t.equals(BlocklyType.ARRAY_STRING)
                || t.equals(BlocklyType.ARRAY_CONNECTION)
                || t.equals(BlocklyType.ARRAY_COLOUR)
                || t.equals(BlocklyType.VOID)
                || t.equals(BlocklyType.NOTHING) ) {
                return BlocklyType.ARRAY;
            }
        }
        throw new IllegalArgumentException("Expression " + list.toString() + "has an unsupported type");
    }

    /**
     * @param Function Expression
     * @return Return Type of block
     */
    private BlocklyType visitFunctionExpr(FunctionExpr funct) {
        return checkAST(funct.getFunction());
    }

    /**
     * Method to check MathNumPropFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        return functionHelper(
            mathNumPropFunct.param,
            mathNumPropFunct.functName.equals(FunctionNames.DIVISIBLE_BY) ? 2 : 1,
            BlocklyType.NUMBER,
            BlocklyType.BOOLEAN);
    }

    /**
     * Method to check MathOnListFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            }
        }
        List<Expr> args = mathOnListFunct.param;
        // All the list functions take only one list
        // Check that is only one
        BlocklyType t = BlocklyType.VOID;
        if ( args.size() != 1 ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }
        // Check that all the elements are numbers
        for ( Expr e : args ) {
            t = checkAST(e);
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( !t.equals(BlocklyType.ARRAY_NUMBER) && !t.equals(BlocklyType.ARRAY) ) {
                    ExprlyUnParser up = new ExprlyUnParser(e);
                    addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                }
            }
        }
        if ( mathOnListFunct.functName.equals(FunctionNames.RANDOM) ) {
            if ( t.equals(BlocklyType.ARRAY_NUMBER) ) {
                return BlocklyType.NUMBER;
            }
            if ( t.equals(BlocklyType.ARRAY_BOOLEAN) ) {
                return BlocklyType.BOOLEAN;
            }
            if ( t.equals(BlocklyType.ARRAY_STRING) ) {
                return BlocklyType.STRING;
            }
            if ( t.equals(BlocklyType.ARRAY_CONNECTION) ) {
                return BlocklyType.CONNECTION;
            }
            if ( t.equals(BlocklyType.ARRAY_COLOUR) ) {
                return BlocklyType.COLOR;
            }
            return BlocklyType.VOID;
        }
        return BlocklyType.NUMBER;
    }

    /**
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        return BlocklyType.NUMBER;
    }

    /**
     * Method to check MathRandomIntFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        return functionHelper(mathRandomIntFunct.param, 2, BlocklyType.NUMBER, BlocklyType.NUMBER);
    }

    /**
     * Method to check MathSingleFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        FunctionNames fname = mathSingleFunct.functName;
        if ( fname.equals(FunctionNames.MAX) || fname.equals(FunctionNames.MIN) ) {
            return functionHelper(mathSingleFunct.param, 2, BlocklyType.NUMBER, BlocklyType.NUMBER);

        } else {
            return functionHelper(mathSingleFunct.param, 1, BlocklyType.NUMBER, BlocklyType.NUMBER);
        }
    }

    /**
     * Method to check MathPowerFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        return functionHelper(mathPowerFunct.param, 2, BlocklyType.NUMBER, BlocklyType.NUMBER);
    }

    /**
     * Method to check MathConstrainFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        return functionHelper(mathConstrainFunct.param, 3, BlocklyType.NUMBER, BlocklyType.NUMBER);
    }

    /**
     * Method to check TextJoinFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noTextJoin") ) {
                addError(TcError.TcErrorMsg.NO_FUNCT, "FUNCT", "createTextWith");
            }
        }
        BlocklyType t;
        List<Expr> args = textJoinFunct.param.get();

        if ( args.size() < 2 ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }
        // Check that is a number type
        for ( Expr e : args ) {
            t = checkAST(e);
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( !t.equals(BlocklyType.STRING) ) {
                    ExprlyUnParser up = new ExprlyUnParser(e);
                    addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                }
            }
        }
        return BlocklyType.STRING;
    }

    /**
     * Method to check TextPrintFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        return functionHelper(textPrintFunct.param, 1, BlocklyType.STRING, BlocklyType.NOTHING);
    }

    /**
     * Method to check SubListFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitGetSubFunct(GetSubFunct getSubFunct) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            } else if ( okMap.get(this.robotName).contains("noSubList") ) {
                addError(TcError.TcErrorMsg.NO_FUNCT, "FUNCT", "subList");
            }
        }
        BlocklyType t, t0;
        t0 = BlocklyType.ARRAY;
        // Get parameters
        List<Expr> args = getSubFunct.param;
        List<IMode> mode = getSubFunct.strParam;
        // Check the number of parameters
        int argNumber = indexArgumentNumber(mode.get(0)) + indexArgumentNumber(mode.get(1)) + 1;
        if ( args.size() != argNumber ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }

        // Check that they're all type correct
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( i == 0 ) {
                    if ( !(t.equals(BlocklyType.ARRAY)
                        || t.equals(BlocklyType.ARRAY_NUMBER)
                        || t.equals(BlocklyType.ARRAY_BOOLEAN)
                        || t.equals(BlocklyType.ARRAY_STRING)
                        || t.equals(BlocklyType.ARRAY_CONNECTION)
                        || t.equals(BlocklyType.ARRAY_COLOUR)) ) {
                        ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                        addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                    }
                } else {
                    if ( !t.equals(BlocklyType.NUMBER) ) {
                        ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                        addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                    }
                }
            }
        }

        if ( t0.equals(BlocklyType.ARRAY_NUMBER)
            || t0.equals(BlocklyType.ARRAY_BOOLEAN)
            || t0.equals(BlocklyType.ARRAY_STRING)
            || t0.equals(BlocklyType.ARRAY_CONNECTION)
            || t0.equals(BlocklyType.ARRAY_COLOUR) ) {
            return t0;
        }
        return BlocklyType.ARRAY;
    }

    /**
     * Method to check ListGetFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitListGetIndex(ListGetIndex listGetIndex) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            }
        }
        BlocklyType t, t0;
        t0 = BlocklyType.VOID;
        // Get parameters
        List<Expr> args = listGetIndex.param;
        IIndexLocation mode = listGetIndex.location;
        int argNumber = indexArgumentNumber(mode) + 1;
        // Check the number of parameters
        if ( args.size() != argNumber ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }
        // Check that they're all type correct
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( i == 0 ) {
                    if ( !(t.equals(BlocklyType.ARRAY)
                        || t.equals(BlocklyType.ARRAY_NUMBER)
                        || t.equals(BlocklyType.ARRAY_BOOLEAN)
                        || t.equals(BlocklyType.ARRAY_STRING)
                        || t.equals(BlocklyType.ARRAY_CONNECTION)
                        || t.equals(BlocklyType.ARRAY_COLOUR)) ) {
                        ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                        addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                    }
                } else {
                    if ( !t.equals(BlocklyType.NUMBER) ) {
                        ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                        addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                    }
                }
            }
        }
        if ( t0.equals(BlocklyType.ARRAY_NUMBER) ) {
            return BlocklyType.NUMBER;
        } else if ( t0.equals(BlocklyType.ARRAY_BOOLEAN) ) {
            return BlocklyType.BOOLEAN;
        } else if ( t0.equals(BlocklyType.ARRAY_STRING) ) {
            return BlocklyType.STRING;
        } else if ( t0.equals(BlocklyType.ARRAY_CONNECTION) ) {
            return BlocklyType.CONNECTION;
        } else if ( t0.equals(BlocklyType.ARRAY_COLOUR) ) {
            return BlocklyType.COLOR;
        }
        return BlocklyType.VOID;
    }

    /**
     * Method to check ListSetFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitListSetIndex(ListSetIndex listSetIndex) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            }
        }
        BlocklyType t, t0;
        t0 = BlocklyType.ARRAY;
        // Get parameters
        List<Expr> args = listSetIndex.param;
        IIndexLocation mode = listSetIndex.location;
        int argNumber = indexArgumentNumber(mode) + 2;
        // Check the number of parameters
        if ( args.size() != argNumber ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( i == 0 ) {
                    if ( !(t0.equals(BlocklyType.ARRAY)
                        || t0.equals(BlocklyType.ARRAY_NUMBER)
                        || t0.equals(BlocklyType.ARRAY_BOOLEAN)
                        || t0.equals(BlocklyType.ARRAY_STRING)
                        || t0.equals(BlocklyType.ARRAY_CONNECTION)
                        || t0.equals(BlocklyType.ARRAY_COLOUR)) ) {
                        ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                        addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                    }
                } else {
                    if ( !t.equals(BlocklyType.NUMBER) ) {
                        ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                        addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                    }
                }
            }
        }

        return BlocklyType.NOTHING;
    }

    /**
     * Method to check ListRepeatFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitListRepeat(ListRepeat listRepeat) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            } else if ( okMap.get(this.robotName).contains("noListRepeat") ) {
                addError(TcError.TcErrorMsg.NO_FUNCT, "FUNCT", "listRepeat");
            }
        }
        BlocklyType t, t0, t1;
        t0 = BlocklyType.VOID;
        t1 = BlocklyType.VOID;
        List<Expr> args = listRepeat.param;
        if ( args.size() != 2 ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( i == 1 ) {
                t1 = t;
            }
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( i == 1 ) {
                    if ( !t1.equals(BlocklyType.NUMBER) ) {
                        ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                        addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                    }
                }
            }
        }

        if ( t0.equals(BlocklyType.NUMBER) ) {
            return BlocklyType.ARRAY_NUMBER;
        } else if ( t0.equals(BlocklyType.BOOLEAN) ) {
            return BlocklyType.ARRAY_BOOLEAN;
        } else if ( t0.equals(BlocklyType.STRING) ) {
            return BlocklyType.ARRAY_STRING;
        } else if ( t0.equals(BlocklyType.CONNECTION) ) {
            return BlocklyType.ARRAY_CONNECTION;
        } else if ( t0.equals(BlocklyType.COLOR) ) {
            return BlocklyType.ARRAY_COLOUR;
        }
        return BlocklyType.ARRAY;
    }

    /**
     * Method to check LengthOfFunctions, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            }
        }
        BlocklyType t, t0;
        t0 = BlocklyType.ARRAY;
        FunctionNames fname = lengthOfIsEmptyFunct.functName;
        List<Expr> args = lengthOfIsEmptyFunct.param;
        if ( args.size() != 1 ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }

        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( !(t0.equals(BlocklyType.ARRAY)
                    || t0.equals(BlocklyType.ARRAY_NUMBER)
                    || t0.equals(BlocklyType.ARRAY_BOOLEAN)
                    || t0.equals(BlocklyType.ARRAY_STRING)
                    || t0.equals(BlocklyType.ARRAY_CONNECTION)
                    || t0.equals(BlocklyType.ARRAY_COLOUR)) ) {
                    ExprlyUnParser up = new ExprlyUnParser(args.get(i));
                    addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                }
            }
        }

        if ( fname.equals(FunctionNames.LIST_LENGTH) ) {
            return BlocklyType.NUMBER;
        } else if ( fname.equals(FunctionNames.LIST_IS_EMPTY) ) {
            return BlocklyType.BOOLEAN;
        }
        throw new UnsupportedOperationException("Invalid function name in LengthOsIsEmptyExpr");
    }

    /**
     * Method to check visitIndexOfFunct, stores any errors found in the error list.
     *
     * @param Function
     * @return Return Type of function
     */
    private BlocklyType visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noList") ) {
                addError(TcError.TcErrorMsg.NO_TYPE, "TYPE", "list");
            }
        }
        BlocklyType t, t1;
        List<Expr> args = indexOfFunct.param;
        if ( args.size() != 2 ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
            return BlocklyType.NUMBER;
        } else {
            t = checkAST(args.get(0));
            t1 = checkAST(args.get(1));
            if ( t.equals(BlocklyType.NOTHING) || t1.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( !(t.equals(BlocklyType.ARRAY)
                    || t.equals(BlocklyType.ARRAY_NUMBER)
                    || t.equals(BlocklyType.ARRAY_BOOLEAN)
                    || t.equals(BlocklyType.ARRAY_STRING)
                    || t.equals(BlocklyType.ARRAY_CONNECTION)
                    || t.equals(BlocklyType.ARRAY_COLOUR)) ) {
                    ExprlyUnParser up = new ExprlyUnParser(args.get(0));
                    addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                } else if ( t.equals(BlocklyType.ARRAY_NUMBER) && !t1.equals(BlocklyType.NUMBER)
                    || t.equals(BlocklyType.ARRAY_BOOLEAN) && !t1.equals(BlocklyType.BOOLEAN)
                    || t.equals(BlocklyType.ARRAY_STRING) && !t1.equals(BlocklyType.STRING)
                    || t.equals(BlocklyType.ARRAY_CONNECTION) && !t1.equals(BlocklyType.CONNECTION)
                    || t.equals(BlocklyType.ARRAY_COLOUR) && !t1.equals(BlocklyType.COLOR) ) {
                    ExprlyUnParser up = new ExprlyUnParser(args.get(1));
                    addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                }
                return BlocklyType.NUMBER;
            }
            return BlocklyType.VOID;
        }
    }

    /**
     * Method to check ternaryOperator, stores any errors found in the error list.
     *
     * @param operation
     * @return Return Type of the possible results of the operation or Void if there's an error
     */
    private BlocklyType visitTernaryExpr(TernaryExpr ternaryExpr) {
        if ( this.robotName != null ) {
            if ( okMap.get(this.robotName).contains("noTernary") ) {
                addError(TcError.TcErrorMsg.NO_FUNCT, "FUNCT", "ternary operation");
            }
        }
        BlocklyType t;
        t = checkAST(ternaryExpr.condition);
        if ( !t.equals(BlocklyType.BOOLEAN) ) {
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else {
                ExprlyUnParser up = new ExprlyUnParser(ternaryExpr.condition);
                addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
            }
        }

        t = checkAST(ternaryExpr.thenPart);
        if ( !t.equals(checkAST(ternaryExpr.elsePart)) ) {
            addError(TcError.TcErrorMsg.INVALID_OPERAND_TYPE);
            return BlocklyType.VOID;
        } else {
            return t;
        }
    }

    /**
     * Method to check exprStmts.
     *
     * @param expression stmt
     * @return Return Type of expr
     */
    @SuppressWarnings("unchecked")
    private BlocklyType visitStmtExpr(StmtExpr stmtExpr) {
        return checkAST((Phrase) stmtExpr.stmt);
    }

    /**
     * Method to check type of the phrase passed as parameter
     *
     * @param ast, phrase to analyze
     * @return BlocklyType of ast
     */
    public BlocklyType checkAST(Phrase ast) throws UnsupportedOperationException {
        if ( ast instanceof Binary ) {
            return visitBinary((Binary) ast);
        }
        if ( ast instanceof Unary ) {
            return visitUnary((Unary) ast);
        }
        if ( ast instanceof MathConst ) {
            return visitMathConst((MathConst) ast);
        }
        if ( ast instanceof NumConst ) {
            return visitNumConst((NumConst) ast);
        }
        if ( ast instanceof BoolConst ) {
            return visitBoolConst((BoolConst) ast);
        }
        if ( ast instanceof StringConst ) {
            return visitStringConst((StringConst) ast);
        }
        if ( ast instanceof ColorConst ) {
            return visitColorConst((ColorConst) ast);
        }
        if ( ast instanceof RgbColor ) {
            return visitRgbColor((RgbColor) ast);
        }
        if ( ast instanceof ConnectConst ) {
            return visitConnectConst((ConnectConst) ast);
        }
        if ( ast instanceof Var ) {
            return visitVar((Var) ast);
        }
        if ( ast instanceof ListCreate ) {
            return visitExprList(((ListCreate) ast).exprList);
        }
        if ( ast instanceof ExprList ) {
            return visitExprList((ExprList) ast);
        }
        if ( ast instanceof FunctionExpr ) {
            return visitFunctionExpr((FunctionExpr) ast);
        }
        if ( ast instanceof MathNumPropFunct ) {
            return visitMathNumPropFunct((MathNumPropFunct) ast);
        }
        if ( ast instanceof MathOnListFunct ) {
            return visitMathOnListFunct((MathOnListFunct) ast);
        }
        if ( ast instanceof MathRandomFloatFunct ) {
            return visitMathRandomFloatFunct((MathRandomFloatFunct) ast);
        }
        if ( ast instanceof MathRandomIntFunct ) {
            return visitMathRandomIntFunct((MathRandomIntFunct) ast);
        }
        if ( ast instanceof MathSingleFunct ) {
            return visitMathSingleFunct((MathSingleFunct) ast);
        }
        if ( ast instanceof LengthOfIsEmptyFunct ) {
            return visitLengthOfIsEmptyFunct((LengthOfIsEmptyFunct) ast);
        }
        if ( ast instanceof ListSetIndex ) {
            return visitListSetIndex((ListSetIndex) ast);
        }
        if ( ast instanceof ListGetIndex ) {
            return visitListGetIndex((ListGetIndex) ast);
        }
        if ( ast instanceof ListRepeat ) {
            return visitListRepeat((ListRepeat) ast);
        }
        if ( ast instanceof GetSubFunct ) {
            return visitGetSubFunct((GetSubFunct) ast);
        }
        if ( ast instanceof IndexOfFunct ) {
            return visitIndexOfFunct((IndexOfFunct) ast);
        }
        if ( ast instanceof TextPrintFunct ) {
            return visitTextPrintFunct((TextPrintFunct) ast);
        }
        if ( ast instanceof TextJoinFunct ) {
            return visitTextJoinFunct((TextJoinFunct) ast);
        }
        if ( ast instanceof MathConstrainFunct ) {
            return visitMathConstrainFunct((MathConstrainFunct) ast);
        }
        if ( ast instanceof MathPowerFunct ) {
            return visitMathPowerFunct((MathPowerFunct) ast);
        }
        if ( ast instanceof NullConst ) {
            return visitNullConst((NullConst) ast);
        }
        if ( ast instanceof EmptyExpr ) {
            return visitEmptyExpr((EmptyExpr) ast);
        }
        if ( ast instanceof StmtExpr ) {
            return visitStmtExpr((StmtExpr) ast);
        }
        if ( ast instanceof TernaryExpr ) {
            return visitTernaryExpr((TernaryExpr) ast);
        }

        throw new UnsupportedOperationException("Expression " + ast.toString() + "cannot be checked");
    }

    // Helper functions

    /**
     * Function that typechecks the arguments of a function
     *
     * @param arguments of the function
     * @param Number of expected arguments
     * @param type of the arguments
     * @param return type of the function
     * @return return type of the function
     */
    private BlocklyType functionHelper(List<Expr> args, int argSize, BlocklyType checkedType, BlocklyType expectedReturn) {
        BlocklyType t;
        if ( args.size() != argSize ) {
            addError(TcError.TcErrorMsg.INVALID_ARGUMENT_NUMBER);
        }
        // Check that is a number type
        for ( Expr e : args ) {
            t = checkAST(e);
            if ( t.equals(BlocklyType.NOTHING) ) {
                addError(TcError.TcErrorMsg.UNEXPECTED_METHOD);
            } else if ( !t.equals(BlocklyType.VOID) ) {
                if ( !t.equals(checkedType) ) {
                    ExprlyUnParser up = new ExprlyUnParser(e);
                    addError(TcError.TcErrorMsg.INVALID_ARGUMENT_TYPE, "EXPR", up.fullUnParse());
                }
            }
        }
        return expectedReturn;
    }

    /**
     * Function to get number of expected parameters of a list function given a IMode
     *
     * @param index mode
     * @return number of parameters the mode adds to the block
     */
    private int indexArgumentNumber(IMode mode) {
        if ( mode.equals(IndexLocation.FROM_END) || mode.equals(IndexLocation.FROM_START) ) {
            return 1;
        } else if ( mode.equals(IndexLocation.FIRST) || mode.equals(IndexLocation.LAST) ) {
            return 0;
        }
        throw new IllegalArgumentException("Illegal Index Mode");
    }

    // Fill the HashMap of robot specific restrictions
    static {
        HashMap<String, HashSet<String>> map = new HashMap();
        HashSet<String> wedo = new HashSet();
        HashSet<String> ev3 = new HashSet();
        HashSet<String> nxt = new HashSet();
        HashSet<String> microbit = new HashSet();
        HashSet<String> botnroll = new HashSet();
        HashSet<String> nao = new HashSet();
        HashSet<String> bob3 = new HashSet();
        HashSet<String> arduino = new HashSet();
        HashSet<String> sensebox = new HashSet();
        HashSet<String> mbot = new HashSet();
        HashSet<String> calliope = new HashSet();

        wedo.add("#FF1493");
        wedo.add("#800080");
        wedo.add("#4876FF");
        wedo.add("#00FFFF");
        wedo.add("#90EE90");
        wedo.add("#008000");
        wedo.add("#FFFF00");
        wedo.add("#FFA500");
        wedo.add("#FF0000");
        wedo.add("#FFFFFE");
        wedo.add("#FFFFFF");
        wedo.add("noList");
        wedo.add("noTernary");
        map.put("wedo", wedo);

        ev3.add("#585858");
        ev3.add("#000000");
        ev3.add("#0057A6");
        ev3.add("#00642E");
        ev3.add("#F7D117");
        ev3.add("#B30006");
        ev3.add("#FFFFFF");
        ev3.add("#532115");
        map.put("ev3", ev3);

        nxt.add("#585858");
        nxt.add("#000000");
        nxt.add("#0057A6");
        nxt.add("#00642E");
        nxt.add("#F7D117");
        nxt.add("#B30006");
        nxt.add("#FFFFFF");
        nxt.add("noListRepeat");
        nxt.add("noSubList");
        nxt.add("noTextJoin");
        map.put("nxt", nxt);

        microbit.add("noColor");
        map.put("microbit", microbit);

        botnroll.add("#FFFFFF");
        botnroll.add("noTextJoin");
        map.put("botnroll", botnroll);

        nao.add("#FF0000");
        nao.add("rgb");
        map.put("nao", nao);

        bob3.add("#DD4422");
        bob3.add("#0000FF");
        bob3.add("#00FF00");
        bob3.add("#FFFF00");
        bob3.add("#FF0000");
        bob3.add("#FFFFFF");
        bob3.add("#6633AA");
        bob3.add("#FF0088");
        bob3.add("#00FFFF");
        bob3.add("#FF8800");
        bob3.add("#FF00FF");
        bob3.add("#77FFDD");
        bob3.add("#FF7755");
        bob3.add("#6699EE");
        bob3.add("#4488AA");
        bob3.add("#4466EE");
        bob3.add("#228822");
        bob3.add("#55FF99");
        bob3.add("#000000");
        bob3.add("noTextJoin");
        map.put("bob3", bob3);

        arduino.add("#999999");
        arduino.add("#CC0000");
        arduino.add("#FF6600");
        arduino.add("#FFCC33");
        arduino.add("#33CC00");
        arduino.add("#00CCCC");
        arduino.add("#3366FF");
        arduino.add("#CC33CC");
        arduino.add("#FFFFFF");
        arduino.add("#000000");
        arduino.add("rgb");
        arduino.add("noTextJoin");
        map.put("arduino", arduino);

        sensebox.add("#999999");
        sensebox.add("#CC0000");
        sensebox.add("#FF6600");
        sensebox.add("#FFCC33");
        sensebox.add("#33CC00");
        sensebox.add("#00CCCC");
        sensebox.add("#3366FF");
        sensebox.add("#CC33CC");
        sensebox.add("#FFFFFF");
        sensebox.add("rgb");
        sensebox.add("noTextJoin");
        map.put("sensebox", sensebox);

        mbot.add("#999999");
        mbot.add("#CC0000");
        mbot.add("#FF6600");
        mbot.add("#FFCC33");
        mbot.add("#33CC00");
        mbot.add("#00CCCC");
        mbot.add("#3366FF");
        mbot.add("#CC33CC");
        mbot.add("#FFFFFF");
        mbot.add("rgb");
        mbot.add("noTextJoin");
        map.put("mbot", mbot);

        calliope.add("#999999");
        calliope.add("#CC0000");
        calliope.add("#FF6600");
        calliope.add("#FFCC33");
        calliope.add("#33CC00");
        calliope.add("#00CCCC");
        calliope.add("#3366FF");
        calliope.add("#CC33CC");
        calliope.add("#FF0000");
        calliope.add("rgba");
        calliope.add("noListRepeat");
        map.put("calliope", calliope);

        okMap = Collections.unmodifiableMap(map);
    }
}
