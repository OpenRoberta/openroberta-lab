package de.fhg.iais.roberta.syntax.lang.expr.eval;

import java.util.HashMap;

/**
 * This class is used to keep errors from the ExprlyTypeChecker
 */
public class TcError {
    private final TcErrorMsg key;
    private final HashMap<String, String> error;
    private final String name;

    /**
     * Constructor for the class
     *
     * @param Message enum instance
     * @param name of the placeholder
     * @param value to be replaced
     */
    private TcError(TcErrorMsg key, String name, String value) {
        this.key = key;
        this.name = name;
        if ( name != null ) {
            this.error = new HashMap<>();
            this.error.put(name, value);
        } else {
            this.error = null;
        }
    }

    /**
     * Function to make an instance of a TypeCheck error
     *
     * @param Message enum instance
     */
    public static TcError makeError(TcErrorMsg key) {
        return new TcError(key, null, null);
    }

    /**
     * Function to make an instance of a TypeCheck error
     *
     * @param Message enum instance
     * @param name of the placeholder
     * @param value to be replaced
     */
    public static TcError makeError(TcErrorMsg key, String name, String value) {
        return new TcError(key, name, value);
    }

    /**
     * @return Error message with the placeholder replaced
     */
    public String getError() {
        if ( this.name == null ) {
            return this.key.getMsg();
        } else {
            String message = "";
            String[] splitMessage = this.key.getMsg().split("[.{.}.]");
            for ( String s : splitMessage ) {
                message += this.error.get(s) == null ? s : "»" + this.error.get(s) + "«";
            }
            return message;
        }
    }

    public static enum TcErrorMsg {
        INVALID_OPERAND_TYPE( "Invalid type of operand in expression!" ),
        UNEXPECTED_RETURN_TYPE( "Wrong type of return value from expression!" ),
        INVALID_TYPE_FOR_LIST_ELEMENT( "All elements on the list must have the same type." ),
        INVALID_ARGUMENT_NUMBER( "Wrong number of arguments in function call." ),
        INVALID_ARGUMENT_TYPE( "The expression {EXPR} is the wrong type of argument for the function call." ),
        UNDECLARED_VARIABLE( "Variable {NAME} not declared." ),
        INVALID_COLOR( "The color {COLOR} is invalid for the current robot." ),
        ILLEGAL_RGB( "The current robot can't use RGB colors." ),
        INVALID_RGB_RGBA( "The getRGB function takes {NUM} parameters for this robot." ),
        INVALID_BLOCK_FOR_ROBOT( "The expression {EXPR} isn't valid for the current robot." ),
        UNEXPECTED_METHOD( "You cannot use void methods in that expression." ),
        NO_TYPE( "The current robot doesn't support the use of type {TYPE}." ),
        NO_FUNCT( "The current robot doesn't support the use of function {FUNCT}." );

        private String msg;

        private TcErrorMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return this.msg;
        }
    }
}
