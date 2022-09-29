package de.fhg.iais.roberta.visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class BaseVisitor<V> implements IVisitor<V> {

    /**
     * Delegates visitable to matching visitT(T t) method from substructure
     *
     * @param visitable meant to be visited
     * @return output of delegated visit-method
     */
    public V visit(Phrase visitable) {
        try {
            Method m = getVisitMethodFor(visitable.getClass());
            @SuppressWarnings("unchecked")
            V result = (V) m.invoke(this, new Object[] {visitable});
            return result;
        } catch ( NoSuchMethodException e ) {
            throw new DbcException(String.format("visit Method not found for phrase \"%s\"", visitable.getClass()), e);
        } catch ( IllegalAccessException e ) {
            throw new DbcException(e);
        } catch ( InvocationTargetException e ) {
            // rethrow cause
            Throwable cause = e.getCause();
            if ( cause instanceof RuntimeException ) {
                throw (RuntimeException) cause;
            }
            throw new DbcException(String.format("visit method for phrase \"%s\" threw exception", visitable.getClass()), e);
        }
    }

    private Method getVisitMethodFor(Class<?> clazz) throws NoSuchMethodException {
        Method m = null;
        try {
            return getClass().getMethod("visit", clazz);
        } catch ( NoSuchMethodException e ) {
            String methodName = "visit" + clazz.getSimpleName();
            return getClass().getMethod("visit" + clazz.getSimpleName(), clazz);
        }
    }

}
