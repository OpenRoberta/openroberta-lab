package de.fhg.iais.roberta.ast.typecheck;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.typecheck.TypecheckVisitor.AddErrorProvider;
import de.fhg.iais.roberta.dbc.Assert;

public class TypeSignature {
    private final BlocklyType returnType;
    private final BlocklyType[] paramTypes;

    private TypeSignature(BlocklyType returnType, BlocklyType[] paramTypes) {
        Assert.notNull(returnType);
        Assert.notNull(returnType);
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public static TypeSignature of(BlocklyType returnType, BlocklyType... paramTypes) {
        return new TypeSignature(returnType, paramTypes);
    }

    public BlocklyType typeCheck(Phrase<BlocklyType> phraseWhoseSignaturIsChecked, List<BlocklyType> paramTypes, AddErrorProvider addErrorProvider) {
        if ( paramTypes.size() != this.paramTypes.length ) {
            addErrorProvider.addError(phraseWhoseSignaturIsChecked, "number of parameters don't match");
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
                        addErrorProvider.addError(phraseWhoseSignaturIsChecked, message);
                        return null;
                    }
                } else {
                    String message = "for parameter " + i + " expected: " + this.paramTypes[i] + ", but found: " + paramType;
                    addErrorProvider.addError(phraseWhoseSignaturIsChecked, message);
                    return null;
                }
            }
            i++;
        }
        return this.returnType;
    }
}
