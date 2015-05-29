package de.fhg.iais.roberta.typecheck;

import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * needed to typecheck a blockly program. Defines the signature of a function/method/operator. see {@link BlocklyType} for further explanation
 *
 * @author rbudde
 */
public class Sig {
    private final BlocklyType returnType;
    private final BlocklyType[] paramTypes;

    private Sig(BlocklyType returnType, BlocklyType[] paramTypes) {
        Assert.notNull(returnType);
        Assert.notNull(paramTypes);
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
