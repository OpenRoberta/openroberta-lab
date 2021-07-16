package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class AnnotationHelper {

    /**
     * check whether the class is annotated with at least one of the @Nepo... annotation
     * @param clazz
     * @return true, if @Nepo... annotated
     */
    public static boolean isNepoAnnotatedClass(Class<?> clazz) {
        for ( Annotation anno : clazz.getAnnotations() ) {
            if ( anno instanceof NepoPhrase ) {
                return true;
            } else if ( anno instanceof NepoOp ) {
                return true;
            }
        }
        return false;
    }
    /**
     * transform the blockly block to an AST phrase by processing the annotations found in astClass<br>
     * used in {@link Jaxb2ProgramAst}
     * 
     * @param block block XML represented by Jaxb
     * @param astClass the subclass of Phrase, that should be used for the AST representation of the block
     * @return the AST phrase corresponding to the blockly block
     */
    public static <V> Phrase<V> block2astByAnnotation(Block block, Class<?> astClass, Jaxb2ProgramAst helper) {
        try {
            String btcName = null;
            for ( Annotation anno : astClass.getAnnotations() ) {
                if ( anno instanceof NepoPhrase ) {
                    btcName = ((NepoPhrase) anno).containerType();
                    break;
                } else if ( anno instanceof NepoOp ) {
                    btcName = ((NepoOp) anno).containerType();
                    break;
                }
            }
            BlockType btc = BlockTypeContainer.getByName(btcName);
            BlocklyBlockProperties bp = Jaxb2Ast.extractBlockProperties(block);
            BlocklyComment bc = Jaxb2Ast.extractComment(block);
            List<Class<?>> constructorParameterTypes = new ArrayList<>();
            constructorParameterTypes.add(BlockType.class);
            constructorParameterTypes.add(BlocklyBlockProperties.class);
            constructorParameterTypes.add(BlocklyComment.class);
            List<Object> constructorParameterValues = new ArrayList<>();
            constructorParameterValues.add(btc);
            constructorParameterValues.add(bp);
            constructorParameterValues.add(bc);
            for ( Field field : astClass.getDeclaredFields() ) {
                for ( Annotation anno : field.getAnnotations() ) {
                    if ( anno instanceof NepoValue ) {
                        NepoValue nepoValue = (NepoValue) anno;
                        List<Value> values = block.getValue();
                        if ( field.getType().equals(Expr.class) ) {
                            Phrase<V> sub = helper.extractValue(values, new ExprParam(nepoValue.name(), nepoValue.type()));
                            Expr<V> expr = Jaxb2Ast.convertPhraseToExpr(sub);
                            constructorParameterTypes.add(Expr.class);
                            constructorParameterValues.add(expr);
                            break;
                        } else if ( field.getType().equals(Var.class) ) {
                            Phrase<V> sub = helper.getVar(values, nepoValue.name());
                            constructorParameterTypes.add(Var.class);
                            constructorParameterValues.add(sub);
                            break;
                        } else {
                            throw new DbcException(
                                "type of " + field.getType().getSimpleName() + " in AST class " + astClass.getSimpleName() + " not supported");
                        }
                    } else if ( anno instanceof NepoField ) {
                        NepoField nepoField = (NepoField) anno;
                        List<de.fhg.iais.roberta.blockly.generated.Field> xmlFields = block.getField();
                        String fieldValue = null;
                        for ( de.fhg.iais.roberta.blockly.generated.Field xmlField : xmlFields ) {
                            if ( xmlField.getName().equals(nepoField.name()) ) {
                                fieldValue = xmlField.getValue();
                                break;
                            }
                        }
                        if ( fieldValue == null ) {
                            fieldValue = nepoField.value();
                        }
                        constructorParameterTypes.add(String.class);
                        constructorParameterValues.add(fieldValue);
                    } else if ( anno instanceof NepoData ) {
                        NepoData nepoData = (NepoData) anno;
                        Data data = block.getData();
                        if (data == null) {
                            throw new DbcException("invalid data block in AST class " + astClass.getSimpleName());
                        }
                        String dataValue = data.getValue();
                        constructorParameterTypes.add(String.class);
                        constructorParameterValues.add(dataValue);
                    } else if ( anno instanceof NepoMutation ) {
                        constructorParameterTypes.add(Mutation.class);
                        constructorParameterValues.add(block.getMutation());
                    } else if ( anno instanceof NepoHide ) {
                        constructorParameterTypes.add(Hide.class);
                        if (block.getHide().size() == 1) {
                            constructorParameterValues.add(block.getHide().get(0));
                        } else {
                            constructorParameterValues.add(null);
                        }
                    }
                }
            }
            Constructor<?> declaredConstructor =
                astClass.getDeclaredConstructor(constructorParameterTypes.toArray(new Class[constructorParameterTypes.size()]));
            @SuppressWarnings("unchecked")
            Phrase<V> tk = (Phrase<V>) declaredConstructor.newInstance(constructorParameterValues.toArray(new Object[constructorParameterValues.size()]));
            return tk;
        } catch ( NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException | IllegalArgumentException e ) {
            throw new DbcException("constructor in annotated AST class " + astClass.getSimpleName() + " not found or invalid", e);
        }
    }

    /**
     * get the precedence of the expression
     * <b>This is the default implementation of annotated AST classes used in {@link Expr}</b>
     *
     * @return the precedence
     */
    public static int getPrecedence(Class<?> clazz) {
        NepoOp classAnno = clazz.getAnnotation(NepoOp.class);
        if ( classAnno == null ) {
            throw new DbcException("the default implementation of getPrecedence() fails with the NOT annotated class " + clazz.getSimpleName());
        }
        return classAnno.precedence();
    }

    /**
     * get the association of the expression
     * <b>This is the default implementation of annotated AST classes used in {@link Expr}</b>
     *
     * @return the association
     */
    public static Assoc getAssoc(Class<?> clazz) {
        NepoOp classAnno = clazz.getAnnotation(NepoOp.class);
        if ( classAnno == null ) {
            throw new DbcException("the default implementation of getAssoc() fails with the NOT annotated class " + clazz.getSimpleName());
        }
        return classAnno.assoc();
    }

    /**
     * get the BlocklyType (used for typechecking ...) of this expression
     * <b>This is the default implementation of annotated AST classes used in {@link Expr}</b>
     *
     * @return the BlocklyType
     */
    public static BlocklyType getVarType(Class<?> clazz) {
        NepoOp classAnno = clazz.getAnnotation(NepoOp.class);
        if ( classAnno == null ) {
            throw new DbcException("the default implementation of getVarType() fails with the NOT annotated class " + clazz.getSimpleName());
        }
        return classAnno.blocklyType();
    }

    /**
     * converts the AST representation of this block to a JAXB (~~XML) representation<br>
     * <b>This is the default implementation of annotated AST classes used in {@link Phrase}</b>
     *
     * @return the JAXB (~~XML) representation
     */
    public static Block astToBlock(Phrase<?> phrase) {
        Class<?> clazz = phrase.getClass();
        boolean validNepoAnnotation = false;
        for ( Annotation anno : clazz.getAnnotations() ) {
            if ( anno instanceof NepoPhrase || anno instanceof NepoOp ) {
                validNepoAnnotation = true;
                break;
            }
        }
        if ( !validNepoAnnotation ) {
            throw new DbcException("the default implementation of astToBlock() fails with the NOT annotated class " + clazz.getSimpleName());
        }
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(phrase, jaxbDestination);
        for ( Field field : clazz.getDeclaredFields() ) {
            for ( Annotation anno : field.getAnnotations() ) {
                try {
                    if ( anno instanceof NepoField ) {
                        Ast2Jaxb.addField(jaxbDestination, ((NepoField) anno).name(), (String) field.get(phrase));
                    } else if ( anno instanceof NepoValue ) {
                        Ast2Jaxb.addValue(jaxbDestination, ((NepoValue) anno).name(), (Phrase<?>) field.get(phrase));
                    } else if ( anno instanceof NepoData ) {
                        Ast2Jaxb.addData(jaxbDestination, (String) field.get(phrase));
                    } else if ( anno instanceof NepoMutation ) {
                        Ast2Jaxb.addMutation(jaxbDestination, (Mutation) field.get(phrase));
                    } else if ( anno instanceof NepoHide ) {
                        Hide hide = (Hide) field.get(phrase);
                        if (hide != null) {
                            jaxbDestination.getHide().add(hide);
                        }
                    }
                } catch ( IllegalArgumentException | IllegalAccessException e ) {
                    throw new DbcException("the field " + field.getName() + " of phrase " + clazz.getSimpleName() + " cannot be accessed", e);
                }
            }
        }
        return jaxbDestination;
    }

    /**
     * the String representation of a phrase. To be used for debugging, not programming!<br>
     * <b>This is the default implementation of annotated AST classes used in {@link Phrase}</b>
     *
     * @param phrase from which a string representation is generated
     * @return the String representation of the phrase; return null, if the generation failed
     */
    public static String toString(Phrase<?> phrase) {
        Class<?> clazz = phrase.getClass();
        NepoPhrase classAnno = clazz.getAnnotation(NepoPhrase.class);
        boolean validNepoAnnotation = false;
        for ( Annotation anno : clazz.getAnnotations() ) {
            if ( anno instanceof NepoPhrase || anno instanceof NepoOp ) {
                validNepoAnnotation = true;
                break;
            }
        }
        if ( !validNepoAnnotation ) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName()).append("[");
        boolean first = true;
        for ( Field field : clazz.getDeclaredFields() ) {
            for ( Annotation anno : field.getAnnotations() ) {
                if ( anno instanceof NepoField || anno instanceof NepoValue ) {
                    if ( first ) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    try {
                        sb.append(field.getName()).append(": ").append(field.get(phrase).toString());
                    } catch ( IllegalArgumentException | IllegalAccessException e ) {
                        return null;
                    }
                    break;
                }
            }
        }
        sb.append("]");
        return sb.toString();
    }


    /**
     * this extremely dangerous methods allows to modify values of provate, final fields.
     * It was used with the first version of @Nepo-annotations, but should now NOT be used.
     * It stays some time for documentation
     */
    private static <V> void assignToPublicFinalField(Class<?> astClass, Phrase<V> tk, Field field, Object sub) {
        try {
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(tk, sub);
            modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL);
            modifiersField.setAccessible(false);
            field.setAccessible(false);
        } catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e ) {
            throw new DbcException("field " + field.getName() + " in AST class " + astClass.getSimpleName() + " could not be assigned to", e);
        }
    }
}
