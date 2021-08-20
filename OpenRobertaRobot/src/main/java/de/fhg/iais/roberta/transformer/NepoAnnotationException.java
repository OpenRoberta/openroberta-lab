package de.fhg.iais.roberta.transformer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class NepoAnnotationException extends DbcException {

    public NepoAnnotationException() {
    }

    public NepoAnnotationException(String message) {
        super(message);
    }

    public NepoAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NepoAnnotationException(Throwable cause) {
        super(cause);
    }

    public static NepoAnnotationException createNepoConstructorException(Class<?> astClass, List<Class<?>> constructorParameterTypes, Throwable cause) {
        String message = String.format("Excepted a constructor with the following parameter types %s on %s", constructorParameterTypes, astClass.getSimpleName());
        return new NepoAnnotationException(message, cause);
    }

    public static NepoAnnotationException createNepoConstructorModifierException(Class<?> astClass, Constructor<?> constructor) {
        String message = String.format("Constructor %s on %s must be public", constructor.toString(), astClass.getSimpleName());
        return new NepoAnnotationException(message);
    }

    public static NepoAnnotationException createNepoFieldModifierException(Class<?> astClass, Field field) {
        String message = String.format("Wrong usage at %s#%s: Field should be public", astClass.getSimpleName(), field.getName());
        return new NepoAnnotationException(message);
    }

    public static NepoAnnotationException createNepoTypeException(Class<?> astClass, Annotation annotation, List<Class<?>> validTypes, Field field) {
        String annotationName = annotation.annotationType().getSimpleName();
        String astClassName = astClass.getSimpleName();
        String information = String.format("%s was used incorrectly at %s#%s: Fields annotated with %s must have one of the following types %s", annotationName, astClassName, field.getName(), annotationName, validTypes);
        return new NepoAnnotationException(information);
    }

}
