package de.fhg.iais.roberta.typecheck;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class InfoCollector {

    private final List<NepoInfo> infos = new ArrayList<>();

    /**
     * initialize the info collector visitor.
     */
    private InfoCollector() {
    }

    /**
     * collects the infos generated during typechecking for an AST. This is done by a visitor, which is an instance of this class<br>
     *
     * @param phrase whose infos should be collected
     * @return list of collected infos
     */
    public static List<NepoInfo> collectInfos(Phrase phrase) //
    {
        try {
            InfoCollector astVisitor = new InfoCollector();
            astVisitor.collect(phrase);
            return astVisitor.infos;
        } catch ( IllegalAccessException e ) {
            throw new DbcException("info collection failed");
        }

    }

    private void collect(Object object) throws IllegalAccessException {
        if ( object != null ) {
            Class<?> clazz = object.getClass();
            if ( Phrase.class.isAssignableFrom(clazz) ) {
                Phrase phrase = (Phrase) object;
                infos.addAll(phrase.getInfos().getInfos());
                for ( Field field : clazz.getFields() ) {
                    collect(field.get(phrase));
                }
            }
        }
    }
}
