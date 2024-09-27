package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
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

    public static Sig ofParamList(BlocklyType returnType, List<BlocklyType> paramList) {
        return new Sig(returnType, paramList.toArray(new BlocklyType[paramList.size()]));
    }

    /**
     * for a given AST-object phraseToCheck, that has a signature object of class Sig (a return type and its parameter types),
     * check that the parameter types of the signature are consistent with the actual parameter types as defined by actualParamTypes
     * If they are not consistent, add an error annotation to the AST-object.
     *
     * @param phraseToCheck the (parent) AST-object whose parameters are to be checked. It is needed only to add the error annotation.
     * @param actualParamTypes the actual parameter types derived from the actual AST-object (by typechecking, of course)
     * @return the result type from the signature; never null
     */
    private BlocklyType typeCheck(Phrase phraseToCheck, List<BlocklyType> actualParamTypes) {
        BlocklyType returnTypeIfError = this.returnType; // before: BlocklyType.NOTHING
        if ( varargParamType == null && actualParamTypes.size() != this.paramTypes.length ) {
            phraseToCheck.addTextlyError("number of parameters don't match", true);
            return returnTypeIfError;
        }
        int i = 0;
        BlocklyType capturedType = null;
        for ( BlocklyType actualParamType : actualParamTypes ) {
            Assert.isFalse(actualParamType.equalAsTypes(BlocklyType.CAPTURED_TYPE) || actualParamType.equalAsTypes(BlocklyType.VARARGS));
            if ( this.paramTypes[i].equalAsTypes(BlocklyType.VARARGS) ) {
                if ( actualParamType.hasAsSuperType(varargParamType) ) {
                    // everything is fine, do NOT increment i !!!
                } else {
                    String message = "for parameter " + (i+1) + " an expected type is: " + b2m(varargParamType) + ", but found was type : " + b2m(actualParamType);
                    phraseToCheck.addTextlyError(message, true);
                    return returnTypeIfError;
                }
            } else {
                if ( actualParamType.hasAsSuperType(this.paramTypes[i]) ) {
                    // everything is fine
                } else {
                    if ( this.paramTypes[i].equalAsTypes(BlocklyType.CAPTURED_TYPE) ) {
                        if ( capturedType == null ) {
                            capturedType = actualParamType;
                            returnTypeIfError = capturedType;
                        }
                        if ( returnType == BlocklyType.VOID && !capturedType.isArray() ) {
                            String message = "a list type was expected, but found was type: " + b2m(actualParamType);
                            phraseToCheck.addTextlyError(message, true);
                            return returnTypeIfError;
                        } else if ( !actualParamType.equalAsTypes(capturedType) ) {
                            String message = "for parameter " + i + " the expected captured type is: " + b2m(capturedType) + ", but found was type : " + b2m(actualParamType);
                            phraseToCheck.addTextlyError(message, true);
                            return returnTypeIfError;
                        }
                    } else if ( this.paramTypes[i].equalAsTypes(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM) ) {
                        if ( capturedType == null ) {
                            capturedType = actualParamType.getMatchingArrayTypeForElementType();
                            returnTypeIfError = capturedType;
                        } else if ( !actualParamType.equalAsTypes(capturedType.getMatchingElementTypeForArrayType()) ) {
                            String message = "For the " + n2m(phraseToCheck) + " the expected type for one of the parameters is: " + b2m(capturedType.getMatchingElementTypeForArrayType()) + ", but found was type: " + b2m(actualParamType);
                            phraseToCheck.addTextlyError(message, true);
                            return returnTypeIfError;
                        }
                    } else {
                        if ( phraseToCheck instanceof Binary ) {
                            String[] op = ((Binary) phraseToCheck).op.values;
                            String leftOrRight = i==0 ? "left" : "right";
                            String message = "For the binary op \"" + op[0] + "\" the expected type for the " + leftOrRight + " parameter is: " + b2m(this.paramTypes[i]) + ", but found was type: " + b2m(actualParamType);
                            phraseToCheck.addTextlyError(message, true);
                            return returnTypeIfError;
                        } else {
                            String message = "For the parameter " + (i+1) + " of " + n2m(phraseToCheck) + " the expected type: " + b2m(this.paramTypes[i]) + ", but found was type: " + b2m(actualParamType);
                            phraseToCheck.addTextlyError(message, true);
                            return returnTypeIfError;
                        }
                    }
                }
                i++;
            }

        }
        if ( this.returnType.equals(BlocklyType.CAPTURED_TYPE) && capturedType != null ) {
            return capturedType;
        } else if ( this.returnType.equals(BlocklyType.CAPTURED_TYPE_ARRAY_ITEM) && capturedType != null ) {
            if ( capturedType.isArray() ) {
                return capturedType.getMatchingElementTypeForArrayType();
            } else {
                phraseToCheck.addTextlyError("For this function a list was expected, but found was type: " + b2m(capturedType), true);
                return returnTypeIfError;
            }

        } else {
            return this.returnType;
        }
    }

    /**
     * format a name of a phrase to be usable as part of a message
     * @param phrase of which the name is formatted
     * @return the formatted name
     */
    private static String n2m(Phrase phrase) {
        String name = phrase.getKind().getName();
        String formatted = name.toLowerCase().replace('_', ' ');

        if ( formatted.length() > 0 ) {
            formatted = formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
        }

        return formatted;
    }

    /**
     * format a BlocklyType to be usable as part of a message
     * @param blocklyType the BlocklyType to be formatted
     * @return the formatted BlocklyType
     */private static String b2m(BlocklyType blocklyType) {
        return String.valueOf(blocklyType).toLowerCase();
    }

    public BlocklyType typeCheckPhraseList(Phrase phraseToCheck, IVisitor<BlocklyType> visitor, List<? extends Phrase> phrases) {
        List<BlocklyType> parameterTypes = new ArrayList<>();
        for ( Phrase phrase : phrases ) {
            BlocklyType typeOfPOhrase = phrase.accept(visitor);
            typeOfPOhrase = typeOfPOhrase == null ? BlocklyType.NOTHING : typeOfPOhrase;
            parameterTypes.add(typeOfPOhrase);
        }
        return typeCheck(phraseToCheck, parameterTypes);
    }

    public BlocklyType typeCheckPhrases(Phrase phraseToCheck, IVisitor<BlocklyType> visitor, Phrase... phrases) {
        List<BlocklyType> parameterTypes = new ArrayList<>();
        for ( Phrase phrase : phrases ) {
            BlocklyType typeOfPOhrase = phrase.accept(visitor);
            Assert.notNull(typeOfPOhrase, "result of typechecking should never be null");
            parameterTypes.add(typeOfPOhrase);
        }
        return typeCheck(phraseToCheck, parameterTypes);
    }
}
