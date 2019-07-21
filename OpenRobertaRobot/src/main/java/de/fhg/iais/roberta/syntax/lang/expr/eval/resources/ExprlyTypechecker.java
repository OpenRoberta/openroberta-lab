package de.fhg.iais.roberta.syntax.lang.expr.eval.resources;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
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
import de.fhg.iais.roberta.typecheck.BlocklyType;

public class ExprlyTypechecker<T> {

    private final LinkedList<String> info;
    private final Phrase<T> e;
    private int errorCount = 0;
    private BlocklyType resultType;
    private final BlocklyType expectedResultType;
    private final List<VarDeclaration<T>> vars;

    /**
     * Class constructor, creates an instance of {@link ExprlyTypechecker} for
     * the phrase passed as parameter
     *
     * @param Phrase that will be checked
     **/
    public ExprlyTypechecker(Phrase<T> ast, BlocklyType rt) {
        this.info = new LinkedList<String>();
        this.expectedResultType = rt;
        this.e = ast;
        this.vars = null;
    }

    public ExprlyTypechecker(Phrase<T> ast, BlocklyType rt, List<VarDeclaration<T>> vars) {
        this.info = new LinkedList<String>();
        this.expectedResultType = rt;
        this.e = ast;
        this.vars = vars;
    }

    /**
     * @return list of errors detected
     **/
    public List<String> getErrors() {
        return this.info;
    }

    /**
     * @return list of errors detected
     **/
    public int getNumErrors() {
        return this.errorCount;
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

    public void addToInfo(String s) {
        this.info.add(s);
    }

    /**
     * Method to check the phrase in the class
     */
    public void check() {
        this.errorCount = 0;
        this.resultType = checkAST(this.e);
        if ( !this.resultType.equals(this.expectedResultType) ) {
            this.errorCount++;
            if ( this.resultType.equals(BlocklyType.VOID) ) {
                addToInfo("UNDECLARED_VARIABLE");
            } else if ( this.resultType.equals(BlocklyType.NOTHING) ) {
                addToInfo("UNEXPECTED_METHOD");
            } else {
                addToInfo("UNEXPECTED_RETURN_TYPE");
            }
        }
    }

    /**
     * @param NumberConst Expression
     * @return Type of block
     */
    public BlocklyType visitNumConst(NumConst<T> numConst) {
        return numConst.getVarType();
    }

    /**
     * @param MathConst Expression
     * @return Type of block
     */
    public BlocklyType visitMathConst(MathConst<T> mathConst) {
        return mathConst.getVarType();
    }

    /**
     * @param BoolConst Expression
     * @return Type of block
     */
    public BlocklyType visitBoolConst(BoolConst<T> boolConst) {
        return boolConst.getVarType();
    }

    /**
     * @param StringConst Expression
     * @return Type of block
     */
    public BlocklyType visitStringConst(StringConst<T> stringConst) {
        return stringConst.getVarType();
    }

    /**
     * @param ColorConst Expression
     * @return Type of block
     */
    public BlocklyType visitColorConst(ColorConst<T> colorConst) {
        return colorConst.getVarType();
    }

    /**
     * @param RgbColor Expression
     * @return Type of block
     */
    public BlocklyType visitRgbColor(RgbColor<T> rgbColor) {
        return rgbColor.getVarType();
    }

    /**
     * @param ConnectConst Expression
     * @return Type of block
     */
    public BlocklyType visitConnectConst(ConnectConst<T> connectConst) {

        return connectConst.getVarType();
    }

    /**
     * Note: During testing period, the default Type for Var Types is VOID
     *
     * @param Var Expression
     * @return Type of block
     */
    public BlocklyType visitVar(Var<T> var) {
        if ( this.vars == null ) {
            return var.getVarType();
        }
        for ( VarDeclaration<T> v : this.vars ) {
            if ( var.getValue().equals(v.getName()) ) {
                return v.getVarType();
            }
        }
        return var.getVarType();
    }

    /**
     * Method to check Unary expressions, writes errors in the log if there are any.
     *
     * @param Unary Expression
     * @return Return Type of block
     */
    public BlocklyType visitUnary(Unary<T> unary) throws UnsupportedOperationException {

        // Get type of the operand
        BlocklyType t = checkAST(unary.getExpr());
        if ( t.equals(BlocklyType.VOID) ) {
            this.errorCount++;
            addToInfo("UNDECLARED_VARIABLE");
        } else if ( t.equals(BlocklyType.NOTHING) ) {
            this.errorCount++;
            addToInfo("UNEXPECTED_METHOD");
        }
        // Check if the expression should should be boolean
        if ( unary.getOp().equals(Unary.Op.NOT) ) {
            // If it should be boolean, check if it is
            if ( !t.equals(BlocklyType.BOOLEAN) && !t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            return BlocklyType.BOOLEAN;

            // Check if it's a number operation
        } else if ( unary.getOp().equals(Unary.Op.PLUS) || unary.getOp().equals(Unary.Op.NEG) ) {

            // If it is a number operation, check if the argument is boolean
            if ( !t.equals(BlocklyType.NUMBER) && !t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            return BlocklyType.NUMBER;
        }

        throw new UnsupportedOperationException("Expression " + unary.toString() + "has an invalid operation type");
    }

    /**
     * Method to check Binary expressions, writes errors in the log if there are any.
     *
     * @param Binary Expression
     * @return Return Type of block
     */
    public BlocklyType visitBinary(Binary<T> binary) throws UnsupportedOperationException {
        // Get type of the operands
        BlocklyType tl = checkAST(binary.getLeft());
        BlocklyType tr = checkAST(binary.getRight());

        if ( tl.equals(BlocklyType.VOID) || tr.equals(BlocklyType.VOID) ) {
            this.errorCount++;
            addToInfo("UNDECLARED_VARIABLE");
        } else if ( tl.equals(BlocklyType.NOTHING) || tr.equals(BlocklyType.NOTHING) ) {
            this.errorCount++;
            addToInfo("UNEXPECTED_METHOD");
        }

        // Check if its a number operation
        if ( binary.getOp().equals(Binary.Op.ADD)
            || binary.getOp().equals(Binary.Op.MINUS)
            || binary.getOp().equals(Binary.Op.MULTIPLY)
            || binary.getOp().equals(Binary.Op.DIVIDE)
            || binary.getOp().equals(Binary.Op.MOD) ) {
            // Check if the left operand is a Number Type
            if ( !tl.equals(BlocklyType.NUMBER) && !tl.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) && !tr.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            return BlocklyType.NUMBER;
        }
        // Check if the operation is a boolean operation
        if ( binary.getOp().equals(Binary.Op.AND) || binary.getOp().equals(Binary.Op.OR) ) {
            // Check if the left operand is a Boolean Type
            if ( !tl.equals(BlocklyType.BOOLEAN) && !tl.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            if ( !tr.equals(BlocklyType.BOOLEAN) && !tr.equals(BlocklyType.VOID) ) {
                // Check if the right operand is a Boolean Type
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            return BlocklyType.BOOLEAN;
        }
        // Check if it's an equality or inequality operation
        if ( binary.getOp().equals(Binary.Op.EQ) || binary.getOp().equals(Binary.Op.NEQ) ) {
            // Check if both operands are of the same Type
            if ( !tl.equals(tr) && !tl.equals(BlocklyType.VOID) && !tr.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            return BlocklyType.BOOLEAN;
        }
        // Check if operation is a inecuation
        if ( binary.getOp().equals(Binary.Op.GT)
            || binary.getOp().equals(Binary.Op.LT)
            || binary.getOp().equals(Binary.Op.GTE)
            || binary.getOp().equals(Binary.Op.LTE) ) {
            // Check if the left operand is a Number Type
            if ( !tl.equals(BlocklyType.NUMBER) && !tl.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) && !tr.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("INVALID_OPERAND_TYPE");
            }
            return BlocklyType.BOOLEAN;
        }

        throw new UnsupportedOperationException("Expression " + binary.toString() + "has an invalid operation type");
    }

    /**
     * Method to check List expressions, writes errors in the log if there are any.
     *
     * @param List Expression
     * @return Type of block
     */
    public BlocklyType visitExprList(ExprList<T> list) throws IllegalArgumentException {
        // Get list of expressions in the list
        List<Expr<T>> eList = list.get();
        List<BlocklyType> tList = new ArrayList<BlocklyType>(eList.size());
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
            for ( int k = 0; k < tList.size(); k++ ) {
                // If it's a variable, store a warning to be able to recover later
                if ( tList.get(k).equals(BlocklyType.VOID) ) {
                    this.errorCount++;
                    addToInfo("UNDECLARED_VARIABLE");
                } else if ( tList.get(k).equals(BlocklyType.NOTHING) ) {
                    this.errorCount++;
                    addToInfo("UNEXPECTED_METHOD");
                } else {
                    // If the types are different it's considered an error
                    if ( !tList.get(k).equals(t) ) {
                        this.errorCount++;
                        addToInfo("INVALID_TYPE_FOR_LIST_ELEMENT");
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
            if ( t.equals(BlocklyType.ARRAY)
                || t.equals(BlocklyType.ARRAY_NUMBER)
                || t.equals(BlocklyType.ARRAY_BOOLEAN)
                || t.equals(BlocklyType.ARRAY_STRING)
                || t.equals(BlocklyType.ARRAY_CONNECTION)
                || t.equals(BlocklyType.VOID) ) {
                return BlocklyType.ARRAY;
            }
        }
        throw new IllegalArgumentException("Expression " + list.toString() + "has an unsupported type");
    }

    /**
     * @param Function Expression
     * @return Return Type of block
     */
    public BlocklyType visitFunctionExpr(FunctionExpr<T> funct) {
        return checkAST(funct.getFunction());
    }

    /**
     * Method to check MathNumPropFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathNumPropFunct(MathNumPropFunct<T> mathNumPropFunct) {
        return functionHelper(mathNumPropFunct.getParam(), 1, BlocklyType.NUMBER, BlocklyType.BOOLEAN);
    }

    /**
     * Method to check MathOnListFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathOnListFunct(MathOnListFunct<T> mathOnListFunct) {
        List<Expr<T>> args = mathOnListFunct.getParam();
        // All the list functions take only one list
        // Check that is only one
        BlocklyType t;
        if ( args.size() != 1 ) {
            this.errorCount++;
            this.info.add("INVALID_ARGUMENT_NUMBER");
        }
        // Check that all the elements are numbers
        for ( Expr<T> e : args ) {
            t = checkAST(e);
            if ( t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("UNDECLARED_VARIABLE");
            } else if ( t.equals(BlocklyType.NOTHING) ) {
                this.errorCount++;
                addToInfo("UNEXPECTED_METHOD");
            } else {
                if ( !t.equals(BlocklyType.ARRAY_NUMBER) && !checkAST(e).equals(BlocklyType.ARRAY) ) {
                    this.errorCount++;
                    addToInfo("INVALID_ARGUMENT_TYPE");
                }
            }
        }
        return BlocklyType.NUMBER;
    }

    /**
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct<T> mathRandomFloatFunct) {
        if ( mathRandomFloatFunct.hasArgs() ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }
        return BlocklyType.NUMBER;
    }

    /**
     * Method to check MathRandomIntFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathRandomIntFunct(MathRandomIntFunct<T> mathRandomIntFunct) {
        return functionHelper(mathRandomIntFunct.getParam(), 2, BlocklyType.NUMBER, BlocklyType.NUMBER);
    }

    /**
     * Method to check MathSingleFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathSingleFunct(MathSingleFunct<T> mathSingleFunct) {
        FunctionNames fname = mathSingleFunct.getFunctName();
        if ( fname.equals(FunctionNames.MAX) || fname.equals(FunctionNames.MIN) ) {
            return functionHelper(mathSingleFunct.getParam(), 2, BlocklyType.NUMBER, BlocklyType.NUMBER);

        } else {
            return functionHelper(mathSingleFunct.getParam(), 1, BlocklyType.NUMBER, BlocklyType.NUMBER);
        }
    }

    /**
     * Method to check MathPowerFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathPowerFunct(MathPowerFunct<T> mathPowerFunct) {
        return functionHelper(mathPowerFunct.getParam(), 2, BlocklyType.NUMBER, BlocklyType.NUMBER);
    }

    /**
     * Method to check MathConstrainFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathConstrainFunct(MathConstrainFunct<T> mathConstrainFunct) {
        return functionHelper(mathConstrainFunct.getParam(), 3, BlocklyType.NUMBER, BlocklyType.NUMBER);
    }

    /**
     * Method to check TextJoinFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitTextJoinFunct(TextJoinFunct<T> textJoinFunct) {
        return functionHelper(textJoinFunct.getParam().get(), 2, BlocklyType.STRING, BlocklyType.STRING);
    }

    /**
     * Method to check TextPrintFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitTextPrintFunct(TextPrintFunct<T> textPrintFunct) {
        return functionHelper(textPrintFunct.getParam(), 1, BlocklyType.STRING, BlocklyType.NOTHING);
    }

    /**
     * Method to check SubListFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitGetSubFunct(GetSubFunct<T> getSubFunct) {
        BlocklyType t, t0;
        t0 = BlocklyType.ARRAY;
        // Get parameters
        List<Expr<T>> args = getSubFunct.getParam();
        List<IMode> mode = getSubFunct.getStrParam();
        // Check the number of parameters
        int argNumber = indexArgumentNumber(mode.get(0)) + indexArgumentNumber(mode.get(1)) + 1;
        if ( args.size() != argNumber ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }

        // Check that they're all type correct
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("UNDECLARED_VARIABLE");
            } else if ( t.equals(BlocklyType.NOTHING) ) {
                this.errorCount++;
                addToInfo("UNEXPECTED_METHOD");
            } else {
                if ( i == 0 ) {
                    if ( !(t.equals(BlocklyType.ARRAY)
                        || t.equals(BlocklyType.ARRAY_NUMBER)
                        || t.equals(BlocklyType.ARRAY_BOOLEAN)
                        || t.equals(BlocklyType.ARRAY_STRING)
                        || t.equals(BlocklyType.ARRAY_CONNECTION)) ) {
                        this.errorCount++;
                        addToInfo("INVALID_ARGUMENT_TYPE");
                    }
                } else {
                    if ( !t.equals(BlocklyType.NUMBER) ) {
                        this.errorCount++;
                        addToInfo("INVALID_ARGUMENT_TYPE");
                    }
                }
            }
        }

        if ( t0.equals(BlocklyType.ARRAY_NUMBER) ) {
            return BlocklyType.ARRAY_NUMBER;
        } else if ( t0.equals(BlocklyType.ARRAY_BOOLEAN) ) {
            return BlocklyType.ARRAY_BOOLEAN;
        } else if ( t0.equals(BlocklyType.ARRAY_STRING) ) {
            return BlocklyType.ARRAY_STRING;
        } else if ( t0.equals(BlocklyType.ARRAY_CONNECTION) ) {
            return BlocklyType.ARRAY_CONNECTION;
        }
        return BlocklyType.ARRAY;
    }

    /**
     * Method to check ListGetFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitListGetIndex(ListGetIndex<T> listGetIndex) {
        BlocklyType t, t0;
        t0 = BlocklyType.VOID;
        // Get parameters
        List<Expr<T>> args = listGetIndex.getParam();
        IIndexLocation mode = listGetIndex.getLocation();
        int argNumber = indexArgumentNumber(mode) + 1;
        // Check the number of parameters
        if ( args.size() != argNumber ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }
        // Check that they're all type correct
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("UNDECLARED_VARIABLE");
            } else if ( t.equals(BlocklyType.NOTHING) ) {
                this.errorCount++;
                addToInfo("UNEXPECTED_METHOD");
            } else {
                if ( i == 0 ) {
                    if ( !(t.equals(BlocklyType.ARRAY)
                        || t.equals(BlocklyType.ARRAY_NUMBER)
                        || t.equals(BlocklyType.ARRAY_BOOLEAN)
                        || t.equals(BlocklyType.ARRAY_STRING)
                        || t.equals(BlocklyType.ARRAY_CONNECTION)) ) {
                        this.errorCount++;
                        addToInfo("INVALID_ARGUMENT_TYPE");
                    }
                } else {
                    if ( !t.equals(BlocklyType.NUMBER) ) {
                        this.errorCount++;
                        addToInfo("INVALID_ARGUMENT_TYPE");
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
        }
        return BlocklyType.VOID;
    }

    /**
     * Method to check ListSetFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitListSetIndex(ListSetIndex<T> listSetIndex) {
        BlocklyType t, t0;
        t0 = BlocklyType.ARRAY;
        // Get parameters
        List<Expr<T>> args = listSetIndex.getParam();
        IIndexLocation mode = listSetIndex.getLocation();
        int argNumber = indexArgumentNumber(mode) + 2;
        // Check the number of parameters
        if ( args.size() != argNumber ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("UNDECLARED_VARIABLE");

            } else if ( t.equals(BlocklyType.NOTHING) ) {
                this.errorCount++;
                addToInfo("UNEXPECTED_METHOD");
            } else {
                if ( i == 0 ) {
                    if ( !(t0.equals(BlocklyType.ARRAY)
                        || t0.equals(BlocklyType.ARRAY_NUMBER)
                        || t0.equals(BlocklyType.ARRAY_BOOLEAN)
                        || t0.equals(BlocklyType.ARRAY_STRING)
                        || t0.equals(BlocklyType.ARRAY_CONNECTION)) ) {
                        this.errorCount++;
                        addToInfo("INVALID_ARGUMENT_TYPE");
                    }
                } else {
                    if ( !t.equals(BlocklyType.NUMBER) ) {
                        this.errorCount++;
                        addToInfo("INVALID_ARGUMENT_TYPE");
                    }
                }
            }
        }

        return BlocklyType.NOTHING;
    }

    /**
     * Method to check ListRepeatFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitListRepeat(ListRepeat<T> listRepeat) {
        BlocklyType t, t1;
        t1 = BlocklyType.NOTHING;
        List<Expr<T>> args = listRepeat.getParam();
        if ( args.size() != 2 ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }
        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 1 ) {
                t1 = t;
            }
            if ( t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("UNDECLARED_VARIABLE");
            } else if ( t.equals(BlocklyType.NOTHING) ) {
                this.errorCount++;
                addToInfo("UNEXPECTED_METHOD");
            } else {
                if ( i == 1 ) {
                    if ( !t1.equals(BlocklyType.NUMBER) ) {
                        this.errorCount++;
                        addToInfo("INVALID_ARGUMENT_TYPE");
                    }
                }
            }
        }
        return BlocklyType.ARRAY;
    }

    /**
     * Method to check LengthOfFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<T> lengthOfIsEmptyFunct) {
        BlocklyType t, t0;
        t0 = BlocklyType.ARRAY;
        FunctionNames fname = lengthOfIsEmptyFunct.getFunctName();
        List<Expr<T>> args = lengthOfIsEmptyFunct.getParam();
        if ( args.size() != 1 ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }

        for ( int i = 0; i < args.size(); i++ ) {
            t = checkAST(args.get(i));
            if ( i == 0 ) {
                t0 = t;
            }
            if ( t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("UNDECLARED_VARIABLE");
            } else if ( t.equals(BlocklyType.NOTHING) ) {
                this.errorCount++;
                addToInfo("UNEXPECTED_METHOD");
            } else {
                if ( !(t0.equals(BlocklyType.ARRAY)
                    || t0.equals(BlocklyType.ARRAY_NUMBER)
                    || t0.equals(BlocklyType.ARRAY_BOOLEAN)
                    || t0.equals(BlocklyType.ARRAY_STRING)
                    || t0.equals(BlocklyType.ARRAY_CONNECTION)) ) {
                    this.errorCount++;
                    addToInfo("INVALID_ARGUMENT_TYPE");
                }
            }
        }

        if ( fname.equals(FunctionNames.LISTS_LENGTH) ) {
            return BlocklyType.NUMBER;
        } else if ( fname.equals(FunctionNames.LIST_IS_EMPTY) ) {
            return BlocklyType.BOOLEAN;
        }
        throw new UnsupportedOperationException("Invalid function name in LengthOsIsEmptyExpr");
    }

    /**
     * Method to check type of the phrase passed as parameter
     *
     * @param ast, phrase to analyze
     * @return BlocklyType of ast
     */
    public BlocklyType checkAST(Phrase<T> ast) throws UnsupportedOperationException {
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
        if ( ast instanceof ListCreate<?> ) {
            return visitExprList(((ListCreate<T>) ast).getValue());
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
    BlocklyType functionHelper(List<Expr<T>> args, int argSize, BlocklyType checkedType, BlocklyType expectedReturn) {
        BlocklyType t;
        if ( args.size() != argSize ) {
            this.errorCount++;
            this.info.add("INVALID_ARGUMENT_NUMBER");
        }
        // Check that is a number type
        for ( Expr<T> e : args ) {
            t = checkAST(e);
            if ( t.equals(BlocklyType.VOID) ) {
                this.errorCount++;
                addToInfo("UNDECLARED_VARIABLE");
            } else if ( t.equals(BlocklyType.NOTHING) ) {
                this.errorCount++;
                addToInfo("UNEXPECTED_METHOD");
            } else {
                if ( !t.equals(checkedType) ) {
                    this.errorCount++;
                    addToInfo("INVALID_ARGUMENT_TYPE");
                }
            }
        }
        return expectedReturn;
    }

    /**
     * Function to get number of expected parameters of a list function given a IMode
     *
     * @param index pode
     * @return number of parameters the mode adds to the block
     */
    int indexArgumentNumber(IMode mode) {
        if ( mode.equals(IndexLocation.FROM_END) || mode.equals(IndexLocation.FROM_START) ) {
            return 1;
        } else if ( mode.equals(IndexLocation.FIRST) || mode.equals(IndexLocation.LAST) ) {
            return 0;
        }
        throw new IllegalArgumentException("Illegal Index Mode");
    }

}
