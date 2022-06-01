package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class AnnotationHelper {

    public static List<Class<? extends Annotation>> NEPO_FIELD_ANNOTATIONS = Arrays.asList(NepoValue.class, NepoField.class, NepoData.class, NepoHide.class, NepoMutation.class);

    /**
     * check whether the class is annotated with at least one of the @Nepo... annotation
     *
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
    public static <V> Phrase<V> block2astByAnnotation(Block block, Class<?> astClass, Jaxb2ProgramAst<V> helper) {
        List<ConstructorParameter> constructorParameters = new ArrayList<>();
        constructorParameters.add(new ConstructorParameter(parseBlockType(astClass)));
        constructorParameters.add(new ConstructorParameter(Jaxb2Ast.extractBlockProperties(block)));
        constructorParameters.add(new ConstructorParameter(BlocklyComment.class, Jaxb2Ast.extractComment(block)));


        /*
        TODO
         Refactoring-Idea: (especially when adding new annotations and behavior)
         Each annotation could have a NepoAnnotationProcessor (abstract) with methods
         extractConstructorParameters()
         addToJaxb()
         isValid()
         Then each AnnotationHelper can have a map (Map<? extends Annotation, AnnotationProcessor>) which can be used to refactor block2ast and astToBlock

        This would allow a more generic annotation system
         */

        for ( Field field : astClass.getDeclaredFields() ) {
            for ( Annotation anno : field.getAnnotations() ) {
                if ( anno instanceof NepoValue ) {
                    constructorParameters.add(extractNepoValueConstructorParameters(block, (NepoValue) anno, field, helper, astClass));
                    break;
                } else if ( anno instanceof NepoField ) {
                    constructorParameters.add(extractNepoFieldConstructorParameters(block, (NepoField) anno, field));
                    break;
                } else if ( anno instanceof NepoData ) {
                    constructorParameters.add(extractNepoDataConstructorParameters(block, astClass));
                    break;
                } else if ( anno instanceof NepoMutation ) {
                    constructorParameters.add(new ConstructorParameter(Mutation.class, block.getMutation()));
                    break;
                } else if ( anno instanceof NepoHide ) {
                    constructorParameters.add(extractNepoHideConstructorParameters(block));
                    break;
                }
            }
        }

        try {
            Class<?>[] typeArray = constructorParameters.stream().map(ConstructorParameter::getType).toArray(Class[]::new);
            Object[] valueArray = constructorParameters.stream().map(ConstructorParameter::getValue).toArray(Object[]::new);

            @SuppressWarnings("unchecked")
            Constructor<Phrase<V>> declaredConstructor = (Constructor<Phrase<V>>) astClass.getDeclaredConstructor(typeArray);
            return declaredConstructor.newInstance(valueArray);
        } catch ( NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException | IllegalArgumentException e ) {
            throw new DbcException("Constructor in annotated AST class " + astClass.getSimpleName() + " not found or invalid", e);
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
        if ( !isNepoAnnotatedClass(clazz) ) {
            throw new DbcException("the default implementation of astToBlock() fails with the NOT annotated class " + clazz.getSimpleName());
        }
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(phrase, jaxbDestination);
        for ( Field field : clazz.getDeclaredFields() ) {
            for ( Annotation anno : field.getAnnotations() ) {
                try {
                    if ( anno instanceof NepoField ) {
                        Ast2Jaxb.addField(jaxbDestination, ((NepoField) anno).name(), fieldToString(field.get(phrase)));
                    } else if ( anno instanceof NepoValue ) {
                        Ast2Jaxb.addValue(jaxbDestination, ((NepoValue) anno).name(), (Phrase<?>) field.get(phrase));
                    } else if ( anno instanceof NepoData ) {
                        Ast2Jaxb.addData(jaxbDestination, (String) field.get(phrase));
                    } else if ( anno instanceof NepoMutation ) {
                        Ast2Jaxb.addMutation(jaxbDestination, (Mutation) field.get(phrase));
                    } else if ( anno instanceof NepoHide ) {
                        Hide hide = (Hide) field.get(phrase);
                        if ( hide != null ) {
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

    private static String fieldToString(Object fieldValue) throws IllegalAccessException {
        if ( fieldValue instanceof Boolean ) {
            return fieldValue.toString().toUpperCase();
        }
        return fieldValue.toString();
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
        if ( !isNepoAnnotatedClass(clazz) ) {
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

    private static <V> ConstructorParameter extractNepoValueConstructorParameters(
        Block block,
        NepoValue anno,
        Field field,
        Jaxb2ProgramAst<V> helper,
        Class<?> astClass) {
        List<Value> values = block.getValue();
        if ( field.getType().equals(Expr.class) ) {
            Phrase<V> sub = helper.extractValue(values, new ExprParam(anno.name(), anno.type()));
            Expr<V> expr = Jaxb2Ast.convertPhraseToExpr(sub);
            return new ConstructorParameter(Expr.class, expr);
        } else if ( field.getType().equals(Var.class) ) {
            Expr<V> sub = helper.getVar(values, anno.name());
            if ( sub instanceof Var ) {
                return new ConstructorParameter(Var.class, (Var) sub);
            }
        }
        throw new DbcException("Inconsistency in startup");
    }

    private static ConstructorParameter extractNepoFieldConstructorParameters(Block block, NepoField anno, Field field) {
        String fieldValue = block.getField().stream()
            .filter(xmlField -> xmlField.getName().equals(anno.name()))
            .map(de.fhg.iais.roberta.blockly.generated.Field::getValue)
            .findFirst()
            .orElse(anno.value());

        if ( field.getType().equals(String.class) ) {
            return new ConstructorParameter(fieldValue);
        } else if ( field.getType().equals(boolean.class) || field.getType().equals(Boolean.class) ) {
            return new ConstructorParameter(field.getType(), Boolean.parseBoolean(fieldValue));
        } else if ( field.getType().equals(double.class) || field.getType().equals(Double.class) ) {
            return new ConstructorParameter(field.getType(), Double.parseDouble(fieldValue));
        } else if ( field.getType().isEnum() ) {
            return new ConstructorParameter(field.getType(), Enum.valueOf(field.getType().asSubclass(Enum.class), fieldValue));
        } else {
            throw new DbcException("Inconsistency in startup");
        }

    }

    private static ConstructorParameter extractNepoHideConstructorParameters(Block block) {
        Hide hide = null;
        if ( block.getHide().size() == 1 ) {
            hide = block.getHide().get(0);
        }
        return new ConstructorParameter(Hide.class, hide);
    }

    private static ConstructorParameter extractNepoDataConstructorParameters(Block block, Class<?> astClass) {
        String dataValue = Optional.ofNullable(block.getData())
            .map(Data::getValue)
            .orElseThrow(() -> new DbcException("Data block not present in XML, with corresponding ast class " + astClass.getSimpleName()));
        return new ConstructorParameter(dataValue);
    }

    private static BlockType parseBlockType(Class<?> astClass) {
        String blockTypeName = null;
        for ( Annotation anno : astClass.getAnnotations() ) {
            if ( anno instanceof NepoPhrase ) {
                blockTypeName = ((NepoPhrase) anno).containerType();
                break;
            } else if ( anno instanceof NepoOp ) {
                blockTypeName = ((NepoOp) anno).containerType();
                break;
            }
        }
        return BlockTypeContainer.getByName(blockTypeName);
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

    public static void checkNepoAnnotatedClass(Class<?> nepoAnnotatedClass) {
        List<Class<?>> constructorParameterTypes = new ArrayList<>();
        constructorParameterTypes.add(BlockType.class);
        constructorParameterTypes.add(BlocklyBlockProperties.class);
        constructorParameterTypes.add(BlocklyComment.class);

        for ( Field field : nepoAnnotatedClass.getDeclaredFields() ) {
            checkFieldModifier(nepoAnnotatedClass, field);
            Optional.ofNullable(checkAndCollectFieldTypes(nepoAnnotatedClass, field))
                .ifPresent(constructorParameterTypes::add);
        }

        checkConstructor(nepoAnnotatedClass, constructorParameterTypes);
    }

    private static void checkConstructor(Class<?> nepoAnnotatedClass, List<Class<?>> constructorParameterTypes) {
        try {
            Constructor<?> declaredConstructor = nepoAnnotatedClass.getDeclaredConstructor(constructorParameterTypes.toArray(new Class[0]));
            boolean constructorIsPublic = Modifier.isPublic(declaredConstructor.getModifiers());
            if ( !constructorIsPublic ) {
                throw NepoAnnotationException.createNepoConstructorModifierException(nepoAnnotatedClass, declaredConstructor);
            }
        } catch ( NoSuchMethodException e ) {
            throw NepoAnnotationException.createNepoConstructorException(nepoAnnotatedClass, constructorParameterTypes, e);
        }

    }

    private static Class<?> checkAndCollectFieldTypes(Class<?> nepoAnnotatedClass, Field field) {
        for ( Annotation annotation : field.getAnnotations() ) {
            if ( annotation instanceof NepoValue ) {
                return checkForValidType(NepoValue.VALID_TYPES, field, annotation, nepoAnnotatedClass);
            } else if ( annotation instanceof NepoField ) {
                return checkForValidType(NepoField.VALID_TYPES, field, annotation, nepoAnnotatedClass);
            } else if ( annotation instanceof NepoMutation ) {
                return checkForValidType(NepoMutation.VALID_TYPES, field, annotation, nepoAnnotatedClass);
            } else if ( annotation instanceof NepoData ) {
                return checkForValidType(NepoData.VALID_TYPES, field, annotation, nepoAnnotatedClass);
            } else if ( annotation instanceof NepoHide ) {
                return checkForValidType(NepoHide.VALID_TYPES, field, annotation, nepoAnnotatedClass);
            }
        }
        return null;
    }

    private static Class<?> checkForValidType(List<Class<?>> validTypes, Field field, Annotation annotation, Class<?> nepoAnnotatedClass) {
        boolean isValidEnum = validTypes.contains(Enum.class) && field.getType().isEnum();
        boolean isValidType = isValidEnum || validTypes.contains(field.getType());
        if ( !isValidType ) {
            throw NepoAnnotationException.createNepoTypeException(nepoAnnotatedClass, annotation, validTypes, field);
        }
        return field.getType();
    }

    private static void checkFieldModifier(Class<?> nepoAnnotatedClass, Field field) {
        if ( field.getAnnotations().length > 0 ) {
            boolean fieldIsPublic = Modifier.isPublic(field.getModifiers());
            boolean isNepoAnnotation = Arrays.stream(field.getAnnotations())
                .map(Annotation::annotationType)
                .anyMatch(NEPO_FIELD_ANNOTATIONS::contains);
            if ( !fieldIsPublic && isNepoAnnotation ) {
                throw NepoAnnotationException.createNepoFieldModifierException(nepoAnnotatedClass, field);
            }
        }
    }

    private static class ConstructorParameter {
        private final Class<?> type;
        private final Object value;

        public ConstructorParameter(Object value) {
            this.value = value;
            this.type = value.getClass();
        }

        public ConstructorParameter(Class<?> type, Object value) {
            this.type = type;
            this.value = value;
        }

        public Class<?> getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }
    }

}
