package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
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
    public final BlocklyType varargParamType;

    private Sig(BlocklyType returnType, BlocklyType[] paramTypes) {
        Assert.notNull(returnType);
        Assert.notNull(paramTypes);
        this.returnType = returnType;
        this.paramTypes = paramTypes;
        int indexParamType = -1;
        for ( int i = 0; i < paramTypes.length; i++ ) {
            if ( paramTypes[i].equalAsTypes(BlocklyType.VARARGS) ) {
                if ( paramTypes.length != i + 2 || paramTypes[i + 1].equalAsTypes(BlocklyType.VARARGS) ) {
                    throw new DbcException("invalid signature: " + this);
                }
                indexParamType = i + 1;
                break;
            }
        }
        varargParamType = indexParamType == -1 ? null : paramTypes[indexParamType];
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
        if ( varargParamType == null && actualParamTypes.size() != this.paramTypes.length ) {
            phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error("number of parameters don't match"));
            return BlocklyType.NOTHING;
        }
        int i = 0;
        BlocklyType capturedType = null;
        for ( BlocklyType actualParamType : actualParamTypes ) {
            Assert.isFalse(actualParamType.equalAsTypes(BlocklyType.CAPTURED_TYPE) || actualParamType.equalAsTypes(BlocklyType.VARARGS));
            if ( this.paramTypes[i].equalAsTypes(BlocklyType.VARARGS) ) {
                if ( actualParamType.hasAsSuperType(varargParamType) ) {
                    // everything is fine, do NOT increment i !!!
                } else {
                    String message = "for parameter " + i + " a vararg expected (super-)type is: " + varargParamType + ", but found was type : " + actualParamType;
                    phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                    return BlocklyType.NOTHING;
                }
            } else {
                if ( actualParamType.equalAsTypes(this.paramTypes[i]) ) {
                    // everything is fine
                } else {
                    if ( this.paramTypes[i].equalAsTypes(BlocklyType.CAPTURED_TYPE) ) {
                        if ( capturedType == null ) {
                            capturedType = actualParamType;
                        } else if ( !actualParamType.equalAsTypes(capturedType) ) {
                            String message = "for parameter " + i + " the expected captured type is: " + capturedType + ", but found was type : " + actualParamType;
                            phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                            return BlocklyType.NOTHING;
                        }
                    } else if ( this.paramTypes[i].equalAsTypes(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM) ) {
                        if ( capturedType == null ) {
                            capturedType = actualParamType.getMatchingArrayTypeForElementType();
                        } else if ( !actualParamType.equalAsTypes(capturedType.getMatchingElementTypeForArrayType()) ) {
                            String message = "for parameter " + i + " the expected captured element type is: " + capturedType.getMatchingElementTypeForArrayType()
                                + ", but found was type : " + actualParamType;
                            phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                            return BlocklyType.NOTHING;
                        }
                    } else {
                        String message = "for parameter " + (i + 1) + " the expected type is: " + this.paramTypes[i] + ", but found was type: " + actualParamType;
                        phraseWhoseSignaturIsChecked.addInfo(NepoInfo.error(message));
                        return BlocklyType.NOTHING;
                    }
                }
                i++;
            }

        }
        if ( this.returnType.equals(BlocklyType.CAPTURED_TYPE) && capturedType != null ) {
            return capturedType;
        } else if ( this.returnType.equals(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM) && capturedType != null && capturedType.isArray() ) {
            return capturedType.getMatchingElementTypeForArrayType();
        } else {
            return this.returnType;
        }
    }

    public BlocklyType typeCheckExprList(Phrase phraseToCheck, IVisitor<BlocklyType> visitor, List<Expr> exprs) {
        List<BlocklyType> parameterTypes = new ArrayList<>();
        for ( Expr expr : exprs ) {
            BlocklyType typeOfPOhrase = expr.accept(visitor);
            typeOfPOhrase = typeOfPOhrase == null ? BlocklyType.NOTHING : typeOfPOhrase;
            parameterTypes.add(typeOfPOhrase);
        }
        return typeCheck(phraseToCheck, parameterTypes);
    }

    public BlocklyType typeCheckExprs(Phrase phraseToCheck, IVisitor<BlocklyType> visitor, Expr... exprs) {
        List<BlocklyType> parameterTypes = new ArrayList<>();
        for ( Expr expr : exprs ) {
            BlocklyType typeOfPOhrase = expr.accept(visitor);
            typeOfPOhrase = typeOfPOhrase == null ? BlocklyType.NOTHING : typeOfPOhrase;
            parameterTypes.add(typeOfPOhrase);
        }
        return typeCheck(phraseToCheck, parameterTypes);
    }
}
