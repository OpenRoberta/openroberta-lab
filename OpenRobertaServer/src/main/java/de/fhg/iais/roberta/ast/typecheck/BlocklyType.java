package de.fhg.iais.roberta.ast.typecheck;

import java.util.Locale;

import de.fhg.iais.roberta.dbc.DbcException;

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
    ANY("", ""),
    COMPARABLE("", "", ANY), ADDABLE("", "", ANY), ARRAY("ARRAY", "List", COMPARABLE),
    BOOL("BOOLEAN", "boolean", COMPARABLE), NUMERIC("NUMBER", "float", COMPARABLE, ADDABLE), NUMERIC_INT("NUMBER", "int", COMPARABLE, ADDABLE), STRING("STRING", "String", COMPARABLE, ADDABLE), COLOR("COLOUR", "String", ANY), //
    NULL("", "", STRING, COLOR),
    REF("", "",NULL), PRIM("", "",NUMERIC, BOOL),
    NOTHING("", "",REF, PRIM),
    VOID("", "void"),
    CAPTURED_TYPE("", ""), R("", ""), S("", ""), T("", "");
    // @formatter:on

    private final String blocklyName;
    private final String javaTypeName;
    private final BlocklyType[] superTypes;

    private BlocklyType(String blocklyName, String javaTypeName, BlocklyType... superTypes) {
        this.blocklyName = blocklyName;
        this.javaTypeName = javaTypeName;
        this.superTypes = superTypes;
    }

    /**
     * @return the blocklyName
     */
    public String getBlocklyName() {
        return this.blocklyName;
    }

    /**
     * @return the javaTypeName
     */
    public String getJavaCode() {
        return this.javaTypeName;
    }

    public BlocklyType[] getSuperTypes() {
        return this.superTypes;
    }

    /**
     * get variable type from {@link BlocklyType} from string parameter.
     * Throws exception if the actor variable type does not exists.
     *
     * @param name of the variable type
     * @return variable type from the enum {@link BlocklyType}
     */
    public static BlocklyType get(String variableType) {
        if ( variableType == null || variableType.isEmpty() ) {
            throw new DbcException("Invalid variable type: " + variableType);
        }
        String sUpper = variableType.trim().toUpperCase(Locale.GERMAN);
        for ( BlocklyType ap : BlocklyType.values() ) {
            if ( ap.toString().equals(sUpper) ) {
                return ap;
            }
            if ( sUpper.equals(ap.getBlocklyName()) ) {
                return ap;
            }
        }
        throw new DbcException("Invalid variableType: " + variableType);
    }
}
