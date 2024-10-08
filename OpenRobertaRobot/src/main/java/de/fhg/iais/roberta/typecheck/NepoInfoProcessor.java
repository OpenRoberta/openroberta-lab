package de.fhg.iais.roberta.typecheck;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.visitor.IInfoCollectable;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class is a visitor and responsible for processing the infos generated (and stored in the Phrases) during validation and typechecking for an AST.
 * It does this by traversing the AST using <b>reflection</b>. Be careful when changing this visitor and when changing the overall structure of an AST.
 */
public class NepoInfoProcessor {

    private final List<NepoInfo> infos = new ArrayList<>();
    private boolean deleteOldInfo;

    /**
     * initialize the info collector visitor.
     */
    private NepoInfoProcessor(boolean deleteOldInfo) {
        this.deleteOldInfo = deleteOldInfo;
    }

    /**
     * collect the infos (of type info and type error) generated (and stored in the Phrases) during validation and typechecking for an AST
     *
     * @param phrase whose infos should be collected
     * @return list of collected infos
     */
    public static List<NepoInfo> collectNepoInfos(Phrase phrase) //
    {
        NepoInfoProcessor phraseVisitor = new NepoInfoProcessor(false);
        try {
            phraseVisitor.collect(phrase);
        } catch ( IllegalAccessException e ) {
            throw new DbcException("Cannot collect nepo infos", e);
        }
        return phraseVisitor.infos;
    }

    /**
     * collect the infos only of type error generated (and stored in the Phrases) during validation and typechecking for an AST
     *
     * @param phrase whose errors should be collected
     * @return list of collected errors
     */
    public static List<NepoInfo> collectNepoErrors(Phrase phrase) //
    {
        List<NepoInfo> errors = new ArrayList<>();
        for (NepoInfo nepoInfo : collectNepoInfos(phrase)) {
            if (nepoInfo.getSeverity().equals(NepoInfo.Severity.ERROR)) {
                errors.add(nepoInfo);
            }
        }
        return errors;
    }

    /**
     * process the infos generated (and stored in the Phrases) during validation and typechecking for an AST
     * - store them in the topmost phrase and<br>
     * - delete them in the lower-level phrase
     *
     * @param phrase whose infos should be processed
     * @return list of collected infos
     */
    public static List<NepoInfo> elevateNepoInfos(Phrase phrase) //
    {
        NepoInfoProcessor phraseVisitor = new NepoInfoProcessor(true);
        try {
            phraseVisitor.collect(phrase);
        } catch ( IllegalAccessException e ) {
            throw new DbcException("Cannot collect nepo infos", e);
        }
        for (NepoInfo nepoInfo : phraseVisitor.infos) {
            phrase.addNepoInfo(nepoInfo);
        }
        return phraseVisitor.infos;
    }

    /**
     * collect infos generated by the typechecker by reflection. It traverses the AST by identifying all fields recursively, that are Phrases and extracts the
     * infos from them.
     *
     * @param object a phrase or an arbitrary object. Decide how to handle it.
     */
    private void collect(Object object) throws IllegalAccessException {
        if ( object != null ) {
            Class<?> clazz = object.getClass();
            if ( List.class.isAssignableFrom(clazz) ) {
                for ( Object item : (List) object ) {
                    collect(item);
                }
            } else if ( Phrase.class.isAssignableFrom(clazz) ) {
                Phrase phrase = (Phrase) object;
                for ( NepoInfo info : phrase.getInfos().getInfos() ) {
                    infos.add(info);
                }
                if ( deleteOldInfo) {
                    phrase.getInfos().clear();
                }
                for ( Field field : clazz.getFields() ) {
                    collect(field.get(phrase));
                }
            } else if ( IInfoCollectable.class.isAssignableFrom(clazz) ) {
                IInfoCollectable collectable = (IInfoCollectable) object;
                for ( Field field : clazz.getDeclaredFields() ) {
                    collect(field.get(collectable));
                }
            }
        }
    }
}
