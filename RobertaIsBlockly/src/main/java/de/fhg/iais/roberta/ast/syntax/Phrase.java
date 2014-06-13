package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.ast.syntax.aktion.DriveAktion;
import de.fhg.iais.roberta.ast.syntax.aktion.LightAktion;
import de.fhg.iais.roberta.ast.syntax.aktion.ShowAktion;
import de.fhg.iais.roberta.ast.syntax.aktion.ToneAktion;
import de.fhg.iais.roberta.ast.syntax.aktion.TurnAktion;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.sensoren.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.DrehSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.SteinSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.UltraSSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.AktionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * the top class of all class used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immmutable. The logic to acieve
 * that is in this class. An object of a subclass of {@link Phrase} is initially writable, after the construction of the object has finished,
 * {@link #setReadOnly()} is called.
 * This cannot be undone later. It is expected that all subclasses of {@link #Phrase} do the following:<br>
 * - if in construction phase, they should use {@link #mayChange()} to assert that.<br>
 * - if the construction has finished and {@link #setReadOnly()} has been called, they should use {@link #isReadOnly()} to assert their immutability.<br>
 * <br>
 * There are two ways for a client to find out which kind a {@link #Phrase}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
abstract public class Phrase {
    private boolean readOnly = false;
    private final Kind kind;

    public Phrase(Kind kind) {
        this.kind = kind;
    }

    /**
     * @return true, if the object is writable/mutable. This is true, if {@link #setReadOnly()} has not yet been called for this object
     */
    public final boolean mayChange() {
        return !this.readOnly;
    }

    /**
     * @return true, if the object is read-only/immutable. This is true, if {@link #setReadOnly()} has been called for this object
     */
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * make this {@link Phrase}-object read-only/immutable. Should be called if the construction phase has finished
     */
    public final void setReadOnly() {
        this.readOnly = true;
    }

    /**
     * returns the expression if and only if it is an object of the class given as parameter. Otherwise an exception is thrown. The type of the returned
     * expression is
     * the type of the requested class.<br>
     * <i>Is this complexity really needed? Isn't brute-force casting at the client side sufficient?</i>
     * 
     * @param clazz the class the object must be an instance of to return without exception
     * @return the object casted to the type requested
     */
    @SuppressWarnings("unchecked")
    public final <T> T getAs(Class<T> clazz) {
        Assert.isTrue(clazz.equals(getKind().getPhraseClass()));
        return (T) this;
    }

    /**
     * @return the kind of the expression. See enum {@link Kind} for all kinds possible<br>
     */
    public final Kind getKind() {
        return this.kind;
    }

    /**
     * append a newline, then append spaces up to an indentation level, then append an (optional) text<br>
     * helper for constructing readable {@link #toString()}- and {@link #toStringBuilder(StringBuilder, int)}-methods for statement trees
     * 
     * @param sb the string builder, to which has to be appended
     * @param indentation number defining the level of indentation
     * @param text an (optional) text to append; may be null
     */
    protected final void appendNewLine(StringBuilder sb, int indentation, String text) {
        sb.append("\n");
        for ( int i = 0; i < indentation; i++ ) {
            sb.append(" ");
        }
        if ( text != null ) {
            sb.append(text);
        }
    }

    /**
     * append a nice, dense and human-readable representation of a expression for <b>debugging and testing purposes</b>
     * 
     * @param sb the string builder, to which has to be appended
     * @param indentation number defining the level of indentation
     */
    abstract public void toStringBuilder(StringBuilder sb, int indentation);

    public static enum Kind {
        ColorSensor( ColorSensor.class, Category.SENSOR ),
        TouchSensor( TouchSensor.class, Category.SENSOR ),
        UltraSSensor( UltraSSensor.class, Category.SENSOR ),
        InfraredSensor( InfraredSensor.class, Category.SENSOR ),
        DrehSensor( DrehSensor.class, Category.SENSOR ),
        SteinSensor( SteinSensor.class, Category.SENSOR ),
        GyroSensor( GyroSensor.class, Category.SENSOR ),
        ExprList( ExprList.class, Category.EXPR ),
        StringConst( StringConst.class, Category.EXPR ),
        NullConst( NullConst.class, Category.EXPR ),
        BoolConst( BoolConst.class, Category.EXPR ),
        NumConst( NumConst.class, Category.EXPR ),
        MathConst( MathConst.class, Category.EXPR ),
        Var( Var.class, Category.EXPR ),
        Unary( Unary.class, Category.EXPR ),
        Binary( Binary.class, Category.EXPR ),
        SensorExpr( SensorExpr.class, Category.EXPR ),
        EmptyExpr( EmptyExpr.class, Category.EXPR ),
        IfStmt( IfStmt.class, Category.STMT ),
        RepeatStmt( RepeatStmt.class, Category.STMT ),
        ExprStmt( ExprStmt.class, Category.STMT ),
        StmtList( StmtList.class, Category.STMT ),
        AssignStmt( AssignStmt.class, Category.STMT ),
        AktionStmt( AktionStmt.class, Category.STMT ),
        SensorStmt( SensorStmt.class, Category.STMT ),
        StmtFlowCon( StmtFlowCon.class, Category.STMT ),
        TurnAktion( TurnAktion.class, Category.AKTOR ),
        DriveAktion( DriveAktion.class, Category.AKTOR ),
        ShowAktion( ShowAktion.class, Category.AKTOR ),
        ToneAktion( ToneAktion.class, Category.AKTOR ),
        LightAktion( LightAktion.class, Category.AKTOR );

        private final Class<?> clazz;
        private final Category category;

        private Kind(Class<?> clazz, Category category) {
            this.clazz = clazz;
            this.category = category;
        }

        public final Class<?> getPhraseClass() {
            return this.clazz;
        }

        public final Category getCategory() {
            return this.category;
        }
    }

    public static enum Category {
        EXPR, SENSOR, AKTOR, STMT;
    }
}
