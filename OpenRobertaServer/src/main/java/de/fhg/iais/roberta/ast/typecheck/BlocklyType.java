package de.fhg.iais.roberta.ast.typecheck;

/**
 * needed to typecheck a blockly program. Defines the types usable in a signature of a function/method/operator<br>
 * see {@link Sig} for further explanation
 *
 * @author rbudde
 */
public enum BlocklyType {
    // @formatter:off
    ANY, //
    BOOL(ANY), NUMERIC(ANY), STRING(ANY), COLOR(ANY), //
    NULL, VOID, //
    CAPTURED_TYPE, R, S, T, //
    NOTHING, //
    COMPARABLE, ADDABLE;
    // @formatter:off

    private final BlocklyType[] superTypes;

    private BlocklyType(BlocklyType... superTypes) {
        this.superTypes = superTypes;
    }


}
