package de.fhg.iais.roberta.syntax.lang.expr.eval.resources;

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
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
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

    /**
     * Class constructor, creates an instance of {@link ExprlyTypechecker} for
     * the phrase pased as parameter
     *
     * @param Phrase that will be checked
     **/
    public ExprlyTypechecker(Phrase<T> ast, BlocklyType rt) {
        this.info = new LinkedList<>();
        this.expectedResultType = rt;
        this.e = ast;
    }

    /**
     * @return list of errors detected
     **/
    public List<String> getErrors() {
        return this.info;
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
     *
     * @return number of errors in the expression
     */
    public void check() {
        this.errorCount = 0;
        this.resultType = checkAST(this.e);
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
        // Check if the expression should should be boolean
        if ( unary.getOp().equals(Unary.Op.NOT) ) {
            // If it should be boolean, check if it is
            if ( !t.equals(BlocklyType.BOOLEAN) ) {
                this.errorCount++;
                addToInfo(t.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
            }
            return BlocklyType.BOOLEAN;

            // Check if it's a number operation
        } else if ( unary.getOp().equals(Unary.Op.PLUS) || unary.getOp().equals(Unary.Op.NEG) ) {

            // If it is a number operation, check if the argument is boolean
            if ( !t.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(t.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
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

        // Check if its a number operation
        if ( binary.getOp().equals(Binary.Op.ADD)
            || binary.getOp().equals(Binary.Op.MINUS)
            || binary.getOp().equals(Binary.Op.MULTIPLY)
            || binary.getOp().equals(Binary.Op.DIVIDE)
            || binary.getOp().equals(Binary.Op.MOD) ) {
            // Check if the left operand is a Number Type
            if ( !tl.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(tl.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(tr.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
            }
            return BlocklyType.NUMBER;
        }
        // Check if the operation is a boolean operation
        if ( binary.getOp().equals(Binary.Op.AND) || binary.getOp().equals(Binary.Op.OR) ) {
            // Check if the left operand is a Boolean Type
            if ( !tl.equals(BlocklyType.BOOLEAN) ) {
                this.errorCount++;
                addToInfo(tl.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
            }
            if ( !tr.equals(BlocklyType.BOOLEAN) ) {
                // Check if the right operand is a Boolean Type
                this.errorCount++;
                addToInfo(tr.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
            }
            return BlocklyType.BOOLEAN;
        }
        // Check if it's an equality or inequality operation
        if ( binary.getOp().equals(Binary.Op.EQ) || binary.getOp().equals(Binary.Op.NEQ) ) {
            // Check if both operands are of the same Type
            if ( !tl.equals(tr) ) {
                this.errorCount++;
                addToInfo(tr.equals(BlocklyType.VOID) || tl.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
            }
            return BlocklyType.BOOLEAN;
        }
        // Check if operation is a inecuation
        if ( binary.getOp().equals(Binary.Op.GT)
            || binary.getOp().equals(Binary.Op.LT)
            || binary.getOp().equals(Binary.Op.GTE)
            || binary.getOp().equals(Binary.Op.LTE) ) {
            // Check if the left operand is a Number Type
            if ( !tl.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(tl.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(tr.equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_OPERAND_TYPE");
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
        BlocklyType t;
        // Check if it's an empty list, by default we'll return Array type in that case
        if ( eList.size() == 0 ) {
            return BlocklyType.ARRAY;
        } else {
            // Get the type of the first element of the array
            // The type of the list will be considered to be the type of the first element of the array
            t = checkAST(eList.get(0));
            for ( Expr<T> e : eList ) {
                // If the types are different it's considered an error
                if ( !checkAST(e).equals(t) && !checkAST(e).equals(BlocklyType.VOID) ) {
                    this.errorCount++;
                    this.info
                        .add(
                            "Error number "
                                + Integer.toString(this.errorCount)
                                + " at expression "
                                + list.toString()
                                + ": All expressions should have the same return type");
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
                // Void is the default value of VAR, we'll put it here for now
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
        List<Expr<T>> args = mathNumPropFunct.getParam();
        // All the numProp functions take only one number as argument
        // Check that it's only one argument
        if ( args.size() != 1 ) {
            this.errorCount++;
            this.info.add("INVALID_ARGUMENT_NUMBER");
        }
        // Check that is a number type
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(checkAST(e).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
            }
        }
        return BlocklyType.BOOLEAN;
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
        if ( args.size() != 1 ) {
            this.errorCount++;
            this.info.add("INVALID_ARGUMENT_NUMBER");
        }
        // Check that all the elements are numbers
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.ARRAY_NUMBER) ) {
                this.errorCount++;
                addToInfo(checkAST(e).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
            }
        }
        return BlocklyType.NUMBER;
    }

    /**
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct<T> mathRandomFloatFunct) {
        return BlocklyType.NUMBER;
    }

    /**
     * Method to check MathRandomIntFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathRandomIntFunct(MathRandomIntFunct<T> mathRandomIntFunct) {
        // Get arguments
        List<Expr<T>> args = mathRandomIntFunct.getParam();
        // Check that there are only 2 arguments
        if ( args.size() != 2 ) {
            this.errorCount++;
            this.info.add("INVALID_ARGUMENT_NUMBER");
        }

        // Check that they're all number types
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(checkAST(e).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
            }
        }
        return BlocklyType.NUMBER;
    }

    /**
     * Method to check MathSingleFunctions, writes errors in the log if there are any.
     *
     * @param Function
     * @return Return Type of function
     */
    public BlocklyType visitMathSingleFunct(MathSingleFunct<T> mathSingleFunct) {
        // Get parameters
        List<Expr<T>> args = mathSingleFunct.getParam();
        // Check the number of parameters
        if ( args.size() > 2 ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }
        // Check that they're all numbers
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(checkAST(e).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
            }
        }
        return BlocklyType.NUMBER;
    }

    public BlocklyType visitMathConstrainFunct(MathConstrainFunct<T> mathConstrainFunct) {
        // Get parameters
        List<Expr<T>> args = mathConstrainFunct.getParam();
        // Check the number of parameters
        if ( args.size() != 3 ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }
        // Check that they're all numbers
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(checkAST(e).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
            }
        }
        return BlocklyType.NUMBER;
    }

    public BlocklyType visitTextJoinFunct(TextJoinFunct<T> textJoinFunct) {
        // Get parameters
        List<Expr<T>> args = textJoinFunct.getParam().get();
        // Check the number of parameters
        if ( args.size() != 2 ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }
        // Check that they're all numbers
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.STRING) ) {
                this.errorCount++;
                addToInfo(checkAST(e).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
            }
        }
        return BlocklyType.STRING;
    }

    public BlocklyType visitTextPrintFunct(TextPrintFunct<T> textPrintFunct) {
        // Get parameters
        List<Expr<T>> args = textPrintFunct.getParam();
        // Check the number of parameters
        if ( args.size() != 1 ) {
            this.errorCount++;
            addToInfo("INVALID_ARGUMENT_NUMBER");
        }

        if ( !checkAST(args.get(0)).equals(BlocklyType.STRING) ) {
            this.errorCount++;
            addToInfo(checkAST(this.e).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
        }

        return BlocklyType.VOID;
    }

    public BlocklyType visitGetSubFunct(GetSubFunct<T> getSubFunct) {
        // Get parameters
        List<Expr<T>> args = getSubFunct.getParam();
        List<IMode> mode = getSubFunct.getStrParam();
        // Check the number of parameters
        if ( mode.get(0).equals(IndexLocation.FROM_START) || mode.get(0).equals(IndexLocation.FROM_END) ) {
            if ( mode.get(1).equals(IndexLocation.LAST) ) {
                if ( args.size() != 2 ) {
                    this.errorCount++;
                    addToInfo("INVALID_ARGUMENT_NUMBER");
                }
            } else {
                if ( args.size() != 3 ) {
                    this.errorCount++;
                    addToInfo("INVALID_ARGUMENT_NUMBER");
                }
            }
        } else {
            if ( mode.get(1).equals(IndexLocation.LAST) ) {
                if ( args.size() != 1 ) {
                    this.errorCount++;
                    addToInfo("INVALID_ARGUMENT_NUMBER");
                }
            } else {
                if ( args.size() != 2 ) {
                    this.errorCount++;
                    addToInfo("INVALID_ARGUMENT_NUMBER");
                }
            }
        }
        // Check that they're all type correct
        for ( int i = 0; i < args.size(); i++ ) {
            if ( i == 0 ) {
                if ( !(checkAST(args.get(0)).equals(BlocklyType.ARRAY)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_NUMBER)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_BOOLEAN)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_STRING)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_CONNECTION)) ) {
                    addToInfo(checkAST(args.get(0)).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
                } else {
                    if ( !checkAST(args.get(i)).equals(BlocklyType.NUMBER) ) {
                        addToInfo(checkAST(args.get(i)).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
                    }
                }
            }
        }

        if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY) ) {
            return BlocklyType.ARRAY;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_NUMBER) ) {
            return BlocklyType.ARRAY_NUMBER;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_BOOLEAN) ) {
            return BlocklyType.ARRAY_BOOLEAN;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_STRING) ) {
            return BlocklyType.ARRAY_STRING;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_CONNECTION) ) {
            return BlocklyType.ARRAY_CONNECTION;
        }
        return BlocklyType.ARRAY;
    }

    public BlocklyType visitListGetIndex(ListGetIndex<T> listGetIndex) {
        // Get parameters
        List<Expr<T>> args = listGetIndex.getParam();
        IIndexLocation mode = listGetIndex.getLocation();
        // Check the number of parameters
        if ( mode.equals(IndexLocation.FROM_START) || mode.equals(IndexLocation.FROM_END) ) {
            if ( args.size() != 2 ) {
                //error
            }
        } else {
            if ( args.size() != 1 ) {
                //error
            }
        }
        // Check that they're all type correct
        for ( int i = 0; i < args.size(); i++ ) {
            if ( i == 0 ) {
                if ( !(checkAST(args.get(0)).equals(BlocklyType.ARRAY)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_NUMBER)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_BOOLEAN)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_STRING)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_CONNECTION)) ) {
                    addToInfo(checkAST(args.get(0)).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
                } else {
                    if ( !checkAST(args.get(i)).equals(BlocklyType.NUMBER) ) {
                        addToInfo(checkAST(args.get(i)).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
                    }
                }
            }
        }

        if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY) ) {
            return BlocklyType.VOID;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_NUMBER) ) {
            return BlocklyType.NUMBER;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_BOOLEAN) ) {
            return BlocklyType.BOOLEAN;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_STRING) ) {
            return BlocklyType.STRING;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_CONNECTION) ) {
            return BlocklyType.CONNECTION;
        }
        return BlocklyType.VOID;
    }

    public BlocklyType visitListSetIndex(ListSetIndex<T> listSetIndex) {
        // Get parameters
        List<Expr<T>> args = listSetIndex.getParam();
        IIndexLocation mode = listSetIndex.getLocation();
        // Check the number of parameters
        if ( mode.equals(IndexLocation.FROM_START) || mode.equals(IndexLocation.FROM_END) ) {
            if ( args.size() != 3 ) {
                //error
            }
        } else {
            if ( args.size() != 2 ) {
                //error
            }
        }
        // Check that they're all type correct
        for ( int i = 0; i < args.size(); i++ ) {
            if ( i == 0 ) {
                if ( !(checkAST(args.get(0)).equals(BlocklyType.ARRAY)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_NUMBER)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_BOOLEAN)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_STRING)
                    || checkAST(args.get(0)).equals(BlocklyType.ARRAY_CONNECTION)) ) {
                    addToInfo(checkAST(args.get(0)).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
                } else {
                    if ( !checkAST(args.get(i)).equals(BlocklyType.NUMBER) ) {
                        addToInfo(checkAST(args.get(i)).equals(BlocklyType.VOID) ? "WARNING_VARIABLE_TYPE" : "INVALID_ARGUMENT_TYPE");
                    }
                }
            }
        }

        if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY) ) {
            return BlocklyType.VOID;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_NUMBER) ) {
            return BlocklyType.NUMBER;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_BOOLEAN) ) {
            return BlocklyType.BOOLEAN;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_STRING) ) {
            return BlocklyType.STRING;
        } else if ( checkAST(args.get(0)).equals(BlocklyType.ARRAY_CONNECTION) ) {
            return BlocklyType.CONNECTION;
        }
        return BlocklyType.VOID;
    }

    public BlocklyType visitListRepeat(ListRepeat<T> ListRepeat) {
        // TODO Auto-generated method stub
        return null;
    }

    public BlocklyType visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<T> LengthOfIsEmptyFunct) {
        // TODO Auto-generated method stub
        return null;
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
        throw new UnsupportedOperationException("Expression " + ast.toString() + "cannot be checked");
    }

}
