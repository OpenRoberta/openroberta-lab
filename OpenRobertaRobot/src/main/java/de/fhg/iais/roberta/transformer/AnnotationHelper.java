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
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoData;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.Assoc;

public class AnnotationHelper {
    private static List<Class<? extends Annotation>> NEPO_FIELD_ANNOTATIONS =
        Arrays.asList(NepoValue.class, NepoField.class, NepoData.class, NepoHide.class, NepoMutation.class);

    private static final String externalSensorClassName = ExternalSensor.class.getName();

    /**
     * check whether the class is final AND annotated with a @NepoPhrase or @NepoOp or @NepoExternalSensor annotation
     *
     * @param clazz
     * @return true, if annotated
     */
    public static boolean isNepoAnnotatedClass(Class<?> clazz) {
        for ( Annotation anno : clazz.getAnnotations() ) {
            if ( anno instanceof NepoPhrase ) {
                return true;
            } else if ( anno instanceof NepoExpr ) {
                return true;
            }
        }
        return false;
    }

    /**
     * check whether the class is a subclass of ExternalSensor
     *
     * @param clazz
     * @return true, if annotated
     */
    public static boolean isExternalSensorSubClass(Class<?> clazz) {
        return clazz.getSuperclass().getName().equals(externalSensorClassName);
    }

    /**
     * transform the blockly block to an AST phrase by processing the annotations found in astClass<br>
     * used in {@link Jaxb2ProgramAst}
     *
     * @param block block XML represented by Jaxb
     * @param astClass the subclass of Phrase, that should be used for the AST representation of the block
     * @return the AST phrase corresponding to the blockly block
     */
    public static Phrase block2astByAnnotation(Block block, Class<?> astClass, Jaxb2ProgramAst helper) {
        List<ConstructorParameter> constructorParameters = new ArrayList<>();
        constructorParameters.add(new ConstructorParameter(Jaxb2Ast.extractBlocklyProperties(block)));

        if ( isExternalSensorSubClass(astClass) ) {
            constructorParameters.add(extractNepoExternalSensorParameters(block, helper));
        } else {
            for ( Field field : astClass.getFields() ) {
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
        }

        try {
            Class<?>[] typeArray = constructorParameters.stream().map(ConstructorParameter::getType).toArray(Class[]::new);
            Object[] valueArray = constructorParameters.stream().map(ConstructorParameter::getValue).toArray(Object[]::new);

            @SuppressWarnings("unchecked")
            Constructor<Phrase> declaredConstructor = (Constructor<Phrase>) astClass.getDeclaredConstructor(typeArray);
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
        NepoExpr classAnno = clazz.getAnnotation(NepoExpr.class);
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
        NepoExpr classAnno = clazz.getAnnotation(NepoExpr.class);
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
    public static BlocklyType getReturnType(Class<?> clazz) {
        for ( Annotation anno : clazz.getAnnotations() ) {
            if ( anno instanceof NepoPhrase ) {
                return ((NepoPhrase) anno).blocklyType();
            } else if ( anno instanceof NepoExpr ) {
                return ((NepoExpr) anno).blocklyType();
            } else if ( anno instanceof NepoBasic ) {
                return ((NepoBasic) anno).blocklyType();
            }
        }
        throw new DbcException("the default implementation of getVarType() fails with the NOT annotated class " + clazz.getSimpleName());
    }

    /**
     * converts the AST representation of this block to a JAXB (~~XML) representation<br>
     * <b>This is the default implementation of annotated AST classes used in {@link Phrase}</b>
     *
     * @return the JAXB (~~XML) representation
     */
    public static Block ast2xml(Phrase phrase) {
        Class<?> clazz = phrase.getClass();
        if ( isExternalSensorSubClass(clazz) ) {
            return phrase.ast2xml();
        } else {
            Block jaxbDestination = new Block();
            Ast2Jaxb.setBasicProperties(phrase, jaxbDestination);
            for ( Field field : clazz.getFields() ) {
                for ( Annotation anno : field.getAnnotations() ) {
                    try {
                        if ( anno instanceof NepoField ) {
                            Ast2Jaxb.addField(jaxbDestination, ((NepoField) anno).name(), fieldToString(field.get(phrase)));
                        } else if ( anno instanceof NepoValue ) {
                            Ast2Jaxb.addValue(jaxbDestination, ((NepoValue) anno).name(), (Phrase) field.get(phrase));
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
    public static String toString(Phrase phrase) {
        Class<?> clazz = phrase.getClass();
        if ( !isNepoAnnotatedClass(clazz) ) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName()).append("[");
        boolean first = true;
        for ( Field field : clazz.getFields() ) {
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

    private static ConstructorParameter extractNepoValueConstructorParameters(
        Block block,
        NepoValue anno,
        Field field,
        Jaxb2ProgramAst helper,
        Class<?> astClass) {
        List<Value> values = block.getValue();
        if ( field.getType().equals(Expr.class) ) {
            Phrase sub = helper.extractValue(values, new ExprParam(anno.name(), anno.type()));
            Expr expr = Jaxb2Ast.convertPhraseToExpr(sub);
            return new ConstructorParameter(Expr.class, expr);
        } else if ( field.getType().equals(Var.class) ) {
            Expr sub = helper.getVar(values, anno.name());
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

    private static <V> ConstructorParameter extractNepoExternalSensorParameters(Block block, Jaxb2ProgramAst helper) {
        ExternalSensorBean sensorData = ExternalSensor.extractPortAndModeAndSlot(block, helper);
        return new ConstructorParameter(ExternalSensorBean.class, sensorData);
    }

    private static ConstructorParameter extractNepoDataConstructorParameters(Block block, Class<?> astClass) {
        String dataValue = Optional.ofNullable(block.getData())
            .map(Data::getValue)
            .orElseThrow(() -> new DbcException("Data block not present in XML, with corresponding ast class " + astClass.getSimpleName()));
        return new ConstructorParameter(dataValue);
    }

    public static void checkNepoAnnotatedClass(Class<?> nepoAnnotatedClass) {
        List<Class<?>> constructorParameterTypes = new ArrayList<>();
        constructorParameterTypes.add(BlocklyProperties.class);

        if ( isExternalSensorSubClass(nepoAnnotatedClass) ) {
            constructorParameterTypes.add(ExternalSensorBean.class);
        } else {
            for ( Field field : nepoAnnotatedClass.getFields() ) {
                checkFieldModifier(nepoAnnotatedClass, field);
                Optional.ofNullable(checkAndCollectFieldTypes(nepoAnnotatedClass, field))
                    .ifPresent(constructorParameterTypes::add);
            }
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
