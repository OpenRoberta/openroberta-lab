package de.fhg.iais.roberta.ast.typecheck;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * needed to typecheck a blockly program. Defines the signature of a function/method/operator.<br>
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
 *           NULL      I       I
 *            I        I       I
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
public class Sig {
    private final BlocklyType returnType;
    private final BlocklyType[] paramTypes;

    private Sig(BlocklyType returnType, BlocklyType[] paramTypes) {
        Assert.notNull(returnType);
        Assert.notNull(returnType);
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public static Sig of(BlocklyType returnType, BlocklyType... paramTypes) {
        return new Sig(returnType, paramTypes);
    }

    public BlocklyType typeCheck(Phrase<BlocklyType> phraseWhoseSignaturIsChecked, List<BlocklyType> paramTypes) {
        if ( paramTypes.size() != this.paramTypes.length ) {
            phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error("number of parameters don't match"));
        }
        int i = 0;
        BlocklyType capturedType = null;
        for ( BlocklyType paramType : paramTypes ) {
            Assert.isTrue(paramType != BlocklyType.CAPTURED_TYPE);
            if ( paramType != this.paramTypes[i] ) {
                if ( this.paramTypes[i] == BlocklyType.CAPTURED_TYPE ) {
                    if ( capturedType == null ) {
                        capturedType = paramType;
                    } else if ( paramType != capturedType ) {
                        String message = "for parameter " + i + " the expected captured type is: " + capturedType + ", but found was type : " + paramType;
                        phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                        return null;
                    }
                } else {
                    String message = "for parameter " + i + " expected: " + this.paramTypes[i] + ", but found: " + paramType;
                    phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                    return null;
                }
            }
            i++;
        }
        return this.returnType;
    }
}
