package de.fhg.iais.roberta.syntax.lang.expr.eval.resources;

import java.util.LinkedList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
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
    public Integer check() {
        this.errorCount = 0;
        this.resultType = checkAST(this.e);
        return this.errorCount;
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
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at unary expression " + unary.toString() + ": Boolean type expected at operand");
            }
            return BlocklyType.BOOLEAN;

            // Check if it's a number operation
        } else if ( unary.getOp().equals(Unary.Op.PLUS) || unary.getOp().equals(Unary.Op.NEG) ) {

            // If it is a number operation, check if the argument is boolean
            if ( !t.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at unary expression " + unary.toString() + ": Number type expected at operand");
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
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at expression " + binary.toString() + ": Number type expected at left operand");
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at expression " + binary.toString() + ": Number type expected at right operand");
            }
            return BlocklyType.NUMBER;
        }
        // Check if the operation is a boolean operation
        if ( binary.getOp().equals(Binary.Op.AND) || binary.getOp().equals(Binary.Op.OR) ) {
            // Check if the left operand is a Boolean Type
            if ( !tl.equals(BlocklyType.BOOLEAN) ) {
                this.errorCount++;
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at expression " + binary.toString() + ": Boolean type expected at left operand");
            }
            if ( !tr.equals(BlocklyType.BOOLEAN) ) {
                // Check if the right operand is a Boolean Type
                this.errorCount++;
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at expression " + binary.toString() + ": Boolean type expected at right operand");
            }
            return BlocklyType.BOOLEAN;
        }
        // Check if it's an equality or inequality operation
        if ( binary.getOp().equals(Binary.Op.EQ) || binary.getOp().equals(Binary.Op.NEQ) ) {
            // Check if both operands are of the same Type
            if ( !tl.equals(tr) ) {
                this.errorCount++;
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at expression " + binary.toString() + ": Both opperands should have the same type");
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
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at expression " + binary.toString() + ": Number type expected at left operand");
            }
            // Check if the right operand is a Number Type
            if ( !tr.equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(
                    "Error number " + Integer.toString(this.errorCount) + " at expression " + binary.toString() + ": Number type expected at right operand");
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
        if ( args.size() > 1 ) {
            this.errorCount++;
            this.info
                .add(
                    "Error number "
                        + Integer.toString(this.errorCount)
                        + " at expression "
                        + mathNumPropFunct.toString()
                        + ": NumProp Functions only take one argument");
        }
        // Check that is a number type
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(
                    "Error number "
                        + Integer.toString(this.errorCount)
                        + " at expression "
                        + mathNumPropFunct.toString()
                        + ": Number type expected as argument");
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
        if ( args.size() > 1 ) {
            this.errorCount++;
            this.info
                .add(
                    "Error number "
                        + Integer.toString(this.errorCount)
                        + " at expression "
                        + mathOnListFunct.toString()
                        + ": List Functions only take one List type as argument");
        }
        // Check that all the elements are numbers
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.ARRAY_NUMBER) ) {
                this.errorCount++;
                addToInfo(
                    "Error number "
                        + Integer.toString(this.errorCount)
                        + " at expression "
                        + mathOnListFunct.toString()
                        + ": List Functions only take NumberList types as an argument");
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
            this.info
                .add(
                    "Error number "
                        + Integer.toString(this.errorCount)
                        + " at expression "
                        + mathRandomIntFunct.toString()
                        + ": RandomInt function only takes 2 arguments");
        }

        // Check that they're all number types
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(
                    "Error number "
                        + Integer.toString(this.errorCount)
                        + " at expression "
                        + mathRandomIntFunct.toString()
                        + ": RandomInt function only takes Number types as arguments");
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
        // Get parammeters
        List<Expr<T>> args = mathSingleFunct.getParam();
        // Check the number of parameters
        if ( args.size() > 2 ) {
            this.errorCount++;
            addToInfo(
                "Error number "
                    + Integer.toString(this.errorCount)
                    + " at expression "
                    + mathSingleFunct.toString()
                    + ": Math functions take up to 2 arguments");
        }
        // Check that they're all numbers
        for ( Expr<T> e : args ) {
            if ( !checkAST(e).equals(BlocklyType.NUMBER) ) {
                this.errorCount++;
                addToInfo(
                    "Error number "
                        + Integer.toString(this.errorCount)
                        + " at expression "
                        + mathSingleFunct.toString()
                        + ": Math functions only take Number types as arguments");
            }
        }
        return BlocklyType.NUMBER;
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
        throw new UnsupportedOperationException("Expression " + ast.toString() + "cannot be checked");
    }

}
