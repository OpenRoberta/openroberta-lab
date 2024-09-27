package de.fhg.iais.roberta.typecheck;

import java.util.Locale;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * needed to typecheck a blockly program. Defines the types usable in a signature of a function/method/operator<br>
 * The basis structure of the types is:
 *
 * <pre>
 *                    ANY
 *                     I
 *  +---------+--------+-------+
 *  I         I                I
 *  I         I              PRIM
 *  I         I                I
 *  I         I        +-------+-------+
 *  I         I        I       I       I
 * STRING   COLOR   NUMERIC   BOOL   STRING
 *  I         I        I       I       I
 *  +---------+        I       I       I
 *            I        I       I       I
 *           NULL      I       I       I
 *            I        I       I       I
 *           REF       I       I       I
 *            I        I       I       I
 *            +--------+-------+-------+
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
    ANY(""),
    COMPARABLE("",ANY),
    ADDABLE("",ANY),
    PRIM("", ANY),

    BOOLEAN("Boolean",COMPARABLE, PRIM),
    NUMBER("Number",COMPARABLE, ADDABLE, PRIM),
    NUMBER_INT("Number", COMPARABLE, ADDABLE),
    STRING("String",COMPARABLE, ADDABLE, PRIM),
    COLOR("Colour",ANY), //
    IMAGE("Image", ANY),
    CONNECTION("Connection", ANY),
    PREDEFINED_IMAGE("PredefinedImage", ANY),

    NULL("",STRING, COLOR, CONNECTION),
    REF("",NULL),

    NOTHING("Void", REF, PRIM),
    VOID(""),
    CAPTURED_TYPE(""),
    CAPTURED_TYPE_ARRAY_ITEM(""),
    VARARGS("", ANY),

    ARRAY("Array",CAPTURED_TYPE,true,COMPARABLE),
    ARRAY_NUMBER("Array_Number",NUMBER,true,COMPARABLE),
    ARRAY_STRING("Array_String", STRING,true,COMPARABLE),
    ARRAY_COLOUR("Array_Colour", COLOR,true,COMPARABLE),
    ARRAY_BOOLEAN("Array_Boolean",BOOLEAN,true,COMPARABLE),
    ARRAY_IMAGE("Array_Image", IMAGE,true,COMPARABLE),
    ARRAY_CONNECTION("Array_Connection", CONNECTION,true,COMPARABLE);
    // @formatter:on

    private final String blocklyName;
    private final BlocklyType[] superTypes;
    private final BlocklyType arrayElementType;
    private final boolean array;

    private BlocklyType(String blocklyName, BlocklyType... superTypes) {
        this.blocklyName = blocklyName;
        this.superTypes = superTypes;
        arrayElementType = null;
        this.array = false;
    }

    private BlocklyType(String blocklyName, BlocklyType arrayElementType, boolean array, BlocklyType... superTypes) {
        Assert.isTrue(array);
        this.blocklyName = blocklyName;
        this.superTypes = superTypes;
        this.arrayElementType = arrayElementType;
        this.array = true;
    }

    /**
     * @return the blocklyName
     */
    public String getBlocklyName() {
        return this.blocklyName;
    }

    public BlocklyType[] getSuperTypes() {
        return this.superTypes;
    }

    public boolean hasAsSuperType(BlocklyType potentialSuperType) {
        if ( this.equalAsTypes(potentialSuperType) ) {
            return true;
        } else if ( this.equals(BlocklyType.NOTHING) || potentialSuperType.equals(BlocklyType.NOTHING) ) {
            return false;
        } else if ( this.superTypes.length == 0 ) {
            return false;
        } else {
            for ( BlocklyType superType : this.superTypes ) {
                if ( superType.equalAsTypes(potentialSuperType) ) {
                    return true;
                } else {
                    if ( superType.hasAsSuperType(potentialSuperType) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isArray() {
        return this.array;
    }

    public BlocklyType getMatchingArrayTypeForElementType() {
        for ( BlocklyType bt : BlocklyType.values() ) {
            if ( this.equals(bt.arrayElementType) ) {
                return bt;
            }
        }
        return BlocklyType.VOID;
    }

    public BlocklyType getMatchingElementTypeForArrayType() {
        Assert.isTrue(this.isArray());
        for ( BlocklyType bt : BlocklyType.values() ) {
            if ( this.equals(bt) ) {
                return this.arrayElementType;
            }
        }
        throw new DbcException("for array type " + this + " no matching element type");
    }

    /**
     * get variable type from {@link BlocklyType} from string parameter. Throws exception if the actor variable type does not exists.
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
            if ( variableType.trim().equals(ap.getBlocklyName()) ) {
                return ap;
            }
        }
        throw new DbcException("Invalid variableType: " + variableType);
    }

    public static BlocklyType getByBlocklyName(String blocklyName) {
        for ( BlocklyType ap : BlocklyType.values() ) {
            if ( ap.getBlocklyName().toUpperCase().equals(blocklyName.toUpperCase()) ) {
                return ap;
            }
        }
        throw new DbcException("blockly type name is invalid: " + blocklyName);
    }

    public boolean equalAsTypes(BlocklyType other) {
        if ( this.equals(BlocklyType.NOTHING) || other.equals(BlocklyType.NOTHING) ) {
            return false;
        } else {
            return this.equals(other);
        }
    }
}
