package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * needed to typecheck a blockly program. Defines the signature of a function/method/operator. see {@link BlocklyType} for further explanation
 *
 * @author rbudde
 */
public class Sig {
    /**
     * use as a placeholder
     */
    public static final Sig VOID = Sig.of(BlocklyType.VOID);
    public final BlocklyType returnType;
    public final BlocklyType[] paramTypes;

    private Sig(BlocklyType returnType, BlocklyType[] paramTypes) {
        Assert.notNull(returnType);
        Assert.notNull(paramTypes);
        this.returnType = returnType;
        this.paramTypes = paramTypes;
    }

    public static Sig of(BlocklyType returnType, BlocklyType... paramTypes) {
        return new Sig(returnType, paramTypes);
    }

    /**
     * for a given AST-object {@link phraseWhoseSignaturIsChecked}, that has a signature as defined in {@link this} (that are its parameter types and a return type),
     * check that its parameter types are consistent with the actual parameter types as given in {@link paramTypes}. If they are constsiten, return the return type,
     * otherwise add an error annoation to the AST-object and return null
     *
     * @param phraseWhoseSignaturIsChecked the AST-object checked. Is needed only to add the error annotation.
     * @param actualParamTypes types found in a usage
     * @return
     */
    public BlocklyType typeCheck(Phrase phraseWhoseSignaturIsChecked, List<BlocklyType> actualParamTypes) {
        if ( actualParamTypes.size() != this.paramTypes.length ) {
            phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error("number of parameters don't match"));
        }
        int i = 0;
        BlocklyType capturedType = null;
        for ( BlocklyType actualParamType : actualParamTypes ) {
            Assert.isTrue(actualParamType != BlocklyType.CAPTURED_TYPE);
            if ( actualParamType != this.paramTypes[i] ) {
                if ( this.paramTypes[i] == BlocklyType.CAPTURED_TYPE ) {
                    if ( capturedType == null ) {
                        capturedType = actualParamType;
                    } else if ( actualParamType != capturedType ) {
                        String message = "for parameter " + i + " the expected captured type is: " + capturedType + ", but found was type : " + actualParamType;
                        phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                        return null;
                    }
                } else if ( this.paramTypes[i] == BlocklyType.CAPTURED_TYPE_ARRAY_ITEM ) {
                    if ( capturedType == null ) {
                        capturedType = actualParamType.getMatchingArrayTypeForElementType();
                    } else if ( actualParamType != capturedType.getMatchingElementTypeForArrayType() ) {
                        String message = "for parameter " + i + " the expected captured element type is: " + capturedType.getMatchingElementTypeForArrayType()
                            + ", but found was type : " + actualParamType;
                        phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                        return null;
                    }
                } else {
                    String message = "for parameter " + (i + 1) + " the expected type is: " + this.paramTypes[i] + ", but found was type: " + actualParamType;
                    phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                    return null;
                }
            }
            i++;
        }
        if ( this.returnType.equals(BlocklyType.CAPTURED_TYPE) && capturedType != null ) {
            return capturedType;
        } else if ( this.returnType.equals(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM) && capturedType != null && capturedType.isArray() ) {
            return capturedType.getMatchingElementTypeForArrayType();
        } else {
            return this.returnType;
        }
    }

    public BlocklyType typeCheckParameter(Phrase functionExpr, IVisitor<BlocklyType> visitor, Phrase... phrases) {
        List<BlocklyType> parameterTypes = new ArrayList<>();
        for ( Phrase phrase : phrases ) {
            BlocklyType typeOfPOhrase = phrase.accept(visitor);
            typeOfPOhrase = typeOfPOhrase == null ? BlocklyType.NOTHING : typeOfPOhrase;
            parameterTypes.add(typeOfPOhrase);
        }
        return typeCheck(functionExpr, parameterTypes);
    }
}
