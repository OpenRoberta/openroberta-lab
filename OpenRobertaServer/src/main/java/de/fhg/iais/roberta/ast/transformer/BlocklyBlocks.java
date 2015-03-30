package de.fhg.iais.roberta.ast.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyList;
import de.fhg.iais.roberta.ast.syntax.expr.ListCreate;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.ast.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.ast.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.ast.syntax.functions.LenghtOfIsEmptyFunct;
import de.fhg.iais.roberta.ast.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.ast.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.ast.syntax.methods.MethodCall;
import de.fhg.iais.roberta.ast.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.DbcException;

public enum BlocklyBlocks {
    // @formatter: off

    IF_STMT( IfStmt.class, "logic_ternary", "controls_if", "robControls_if", "robControls_ifElse" ),
    MATH_CONST( MathConst.class, "math_constant" ),
    MATH_NUM_PROPERTY( MathNumPropFunct.class, "math_number_property" ),
    MATH_ON_LIST( MathOnListFunct.class, "math_on_list" ),
    MATH_CONSTRAIN( MathConstrainFunct.class, "math_constrain" ),
    MATH_RANDOM_INT( MathRandomIntFunct.class, "math_random_int" ),
    MATH_RANDOM_FLOAT( MathRandomFloatFunct.class, "math_random_float" ),
    STRING_CONST( StringConst.class, "text" ),
    NUM_CONST( NumConst.class, "math_number" ),
    TEXT_JOIN_FUNCT( TextJoinFunct.class, "robText_join", "text_join" ),
    TEXT_PRINT_FUNCT( TextPrintFunct.class, "text_print" ),
    LENGTH_ISEMPTY_FUNC( LenghtOfIsEmptyFunct.class, "lists_length", "lists_isEmpty" ),
    INDEX_OF( IndexOfFunct.class, "lists_indexOf" ),
    EMPTY_LIST( EmptyList.class, "lists_create_empty" ),
    LIST_CREATE( ListCreate.class, "lists_create_with", "robLists_create_with" ),
    LIST_REPEAT( ListRepeat.class, "lists_repeat" ),
    LIST_GET_INDEX( ListGetIndex.class, "lists_getIndex" ),
    LIST_SET_INDEX( ListSetIndex.class, "lists_setIndex" ),
    GET_SUB_FUNC( GetSubFunct.class, "lists_getSublist" ),
    ASSIGN_STMT( AssignStmt.class, "variables_set" ),
    VAR_GET( Var.class, "variables_get" ),
    VAR_DECLARATION( VarDeclaration.class, "robLocalVariables_declare", "robGlobalvariables_declare" ),
    WAIT_STMT( WaitStmt.class, "robControls_wait_for", "robControls_wait" ),
    WAIT_TIME_STMT( WaitTimeStmt.class, "robControls_wait_time" ),
    REPEAT_STMT(
        RepeatStmt.class,
        "robControls_loopForever",
        "controls_whileUntil",
        "controls_for",
        "controls_repeat_ext",
        "controls_repeat",
        "controls_forEach" ),
    FLOW_CONTROL_STMT( StmtFlowCon.class, "controls_flow_statements" ),
    MAIN_TASK( MainTask.class, "robControls_start" ),
    ACTIVITY_TASK( ActivityTask.class, "robControls_activity" ),
    START_ACTIVITY_TASK( StartActivityTask.class, "robControls_start_activity" ),
    METHOD_VOID( MethodVoid.class, "robProcedures_defnoreturn" ),
    METHOD_RETURN( MethodReturn.class, "robProcedures_defreturn" ),
    METHOD_IF_RETURN( MethodIfReturn.class, "robProcedures_ifreturn" ),
    METHOD_CALL( MethodCall.class, "robProcedures_callnoreturn", "robProcedures_callreturn" ),
    MATH_SINGLE_FUNCT( MathSingleFunct.class, "math_round", "math_trig", "math_single" );
    // @formatter:on

    private final Class<?> astClass;
    private final String[] blocklyNames;

    private BlocklyBlocks(Class<?> astClass, String... values) {
        this.astClass = astClass;
        this.blocklyNames = values;
    }

    @SuppressWarnings("unchecked")
    public static <V> Phrase<V> get(Block block, JaxbBlocklyProgramTransformer<V> helper) {

        String className = "";
        if ( block == null ) {
            throw new DbcException("Invalid block: " + block);
        }
        String sUpper = block.getType().trim();
        for ( BlocklyBlocks co : BlocklyBlocks.values() ) {
            if ( co.toString().equals(sUpper) ) {
                className = co.astClass.getName();
                break;
            }
            for ( String value : co.blocklyNames ) {
                if ( sUpper.equals(value) ) {
                    className = co.astClass.getName();
                    break;
                }
            }
        }
        if ( className.equals("") ) {
            throw new DbcException("Invalid Block: " + block.getType());
        }
        Method method;

        try {
            method = Class.forName(className).getMethod("jaxbToAst", Block.class, JaxbAstTransformer.class);
            return (Phrase<V>) method.invoke(null, block, helper);
        } catch ( NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | DbcException e ) {
            throw new DbcException(e.getCause().getMessage());
        }
    }
}
