package de.fhg.iais.roberta.ast.typecheck;

/**
 * needed to typecheck a blockly program. Defines the types usable in a signature of a function/method/operator<br>
 * The basis structure of the types is:
 *
 * <pre>
 *                    ANY
 *                     I
 *  +---------+--------+-------+
 *  I         I        I       I
 * STRING   COLOR   NUMERIC   BOOL
 *  I         I        I       I
 *  +---------+        I       I
 *            I        I       I
 *           NULL      +-------+
 *            I                I
 *           REF              PRIM
 *            I                I
 *            +--------+-------+
 *                     I
 *                   NOTHING
 * </pre>
 *
 * <b>Note:</b><br>
 * <ul>
 * <li>our simple type system cannot be extended by new types (e.g. classes) and is thus closed
 * <li>ANY is supertype of all concrete types, NOTHING is subtype of all concrete types
 * <li>VOID represents no type, e.g. methods that return no object (void in Java)
 * <li>NULL is subtype of reference types (String and Color)
 * <li>there is only one numeric type
 * <li>R, S and T are used for type variables
 * <li>a CAPTURED_TYPE is used to transfer type information in a signature and is not used explicitly. E.g. in <code>eq(TxT->Bool)</code> the type variable is
 * captured, expressing that T has to be substituted by the same concrete type at all places
 * <li>COMPARABLE, ADDABLE are abstract types used for <code>< <= > >= == !=</code> and <code>+</code> (strings and numeric!)
 * </ul>
 *
 * @author rbudde
 */
public enum BlocklyType {
    // @formatter:off
    ANY,
    COMPARABLE(ANY), ADDABLE(ANY),
    BOOL(COMPARABLE), NUMERIC(COMPARABLE, ADDABLE), STRING(COMPARABLE, ADDABLE), COLOR(ANY), //
    NULL(STRING, COLOR),
    REF(NULL), PRIM(NUMERIC, BOOL),
    NOTHING(REF, PRIM),
    VOID,
    CAPTURED_TYPE, R, S, T;
    // @formatter:on

    private final BlocklyType[] superTypes;

    private BlocklyType(BlocklyType... superTypes) {
        this.superTypes = superTypes;
    }

    public BlocklyType[] getSuperTypes() {
        return this.superTypes;
    }
}
