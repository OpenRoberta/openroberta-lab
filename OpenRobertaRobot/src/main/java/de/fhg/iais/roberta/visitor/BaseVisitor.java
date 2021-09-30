package de.fhg.iais.roberta.visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class BaseVisitor<V> implements IVisitor<V> {

    /**
     * Is called before a Phrase is visited.
     * Use by override this.
     *
     * @param visitable meant to be visited
     */
    protected void beforeVisit(Phrase<V> visitable) {
    }

    /**
     * Is called after a Phrase is visited.
     * Use by override this.
     *
     * @param visitable meant to be visited
     */
    protected void afterVisit(Phrase<V> visitable) {
    }

    /**
     * Delegates visitable to matching visitT(T t) method from substructure
     *
     * @param visitable meant to be visited
     * @return output of delegated visit-method
     */
    public final V visit(Phrase<V> visitable) {
        beforeVisit(visitable);
        V result = findAndVisitPhrase(visitable);
        afterVisit(visitable);

        return result;
    }

    private V findAndVisitPhrase(Phrase<V> visitable) {
        String className = visitable.getClass().getSimpleName();
        String methodName = String.format("visit%s", className);
        try {
            Method m = getClass().getMethod(methodName, visitable.getClass());
            @SuppressWarnings("unchecked")
            V result = (V) m.invoke(this, new Object[] {visitable});
            return result;
        } catch ( NoSuchMethodException e ) {
            throw new DbcException(String.format("visit Method \"%s\" not found on \"%s\" (Block \"%s\" is probably not supported)", methodName, this.getClass().getSimpleName(), className), e);
        } catch ( IllegalAccessException e ) {
            throw new DbcException(e);
        } catch ( InvocationTargetException e ) {
            // rethrow cause
            Throwable cause = e.getCause();
            if ( cause instanceof RuntimeException ) {
                throw (RuntimeException) cause;
            }
            throw new DbcException(String.format("unexpected exception was throw inside a visit method %s#%s", className, methodName), e);
        }
    }
}
