package de.fhg.iais.roberta.ast.funct;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

public class ListFunct extends Expr {
    private final Function functName;
    private final Function where1;
    private final Function where2;
    private final List<Expr> param;

    private ListFunct(Function name, List<Expr> param, Function where1, Function where2) {
        super(Phrase.Kind.ListFunct);
        Assert.isTrue(name != null && param != null && where1 != null);
        this.functName = name;
        this.param = param;
        this.where1 = where1;
        this.where2 = where2;
        setReadOnly();
    }

    public static ListFunct make(Function name, List<Expr> param, Function where1, Function where2) {
        return new ListFunct(name, param, where1, where2);
    }

    public static ListFunct make(Function name, List<Expr> param, Function where1) {
        return new ListFunct(name, param, where1, null);
    }

    public Function getFunctName() {
        return this.functName;
    }

    public List<Expr> getParam() {
        return this.param;
    }

    public Function getWhere1() {
        return this.where1;
    }

    public Function getWhere2() {
        return this.where2;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public String toString() {
        return "ListFunct [functName=" + this.functName + ", where1=" + this.where1 + ", where2=" + this.where2 + ", param=" + this.param + "]";
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("Math.pow(");
        this.param.get(0).generateJava(sb, 0);
        sb.append(", ");
        this.param.get(1).generateJava(sb, 0);
        sb.append(")");
    }

    /**
     * Function names.
     */
    public static enum Function {
        INDEX_OF( 10, Assoc.LEFT );

        private final String[] values;
        private final int precedence;
        private final Assoc assoc;

        private Function(int precedence, Assoc assoc, String... values) {
            this.precedence = precedence;
            this.assoc = assoc;
            this.values = values;
        }

        public String getOpSymbol() {
            if ( this.values.length == 0 ) {
                return this.toString();
            } else {
                return this.values[0];
            }
        }

        public int getPrecedence() {
            return this.precedence;
        }

        public Assoc getAssoc() {
            return this.assoc;
        }

        /**
         * get function from {@link Function} from string parameter. It is possible for one function to have multiple string mappings.
         * Throws exception if the operator does not exists.
         * 
         * @param functName of the function
         * @return function from the enum {@link Function}
         */
        public static Function get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid function name: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Function funct : Function.values() ) {
                if ( funct.toString().equals(sUpper) ) {
                    return funct;
                }
                for ( String value : funct.values ) {
                    if ( sUpper.equals(value) ) {
                        return funct;
                    }
                }
            }
            throw new DbcException("Invalid function name: " + s);
        }
    }

    /**
     * Operation names.
     */
    public static enum Operations {
        END();

        private final String[] values;

        private Operations(String... values) {
            this.values = values;
        }

        public String getOpSymbol() {
            if ( this.values.length == 0 ) {
                return this.toString();
            } else {
                return this.values[0];
            }
        }

        /**
         * get operation from {@link Operations} from string parameter. It is possible for one function to have multiple string mappings.
         * Throws exception if the operator does not exists.
         * 
         * @param operationName of the function
         * @return operation from the enum {@link Operations}
         */
        public static Function get(String operationName) {
            if ( operationName == null || operationName.isEmpty() ) {
                throw new DbcException("Invalid operation name: " + operationName);
            }
            String sUpper = operationName.trim().toUpperCase(Locale.GERMAN);
            for ( Function funct : Function.values() ) {
                if ( funct.toString().equals(sUpper) ) {
                    return funct;
                }
                for ( String value : funct.values ) {
                    if ( sUpper.equals(value) ) {
                        return funct;
                    }
                }
            }
            throw new DbcException("Invalid operation name: " + operationName);
        }
    }

}
