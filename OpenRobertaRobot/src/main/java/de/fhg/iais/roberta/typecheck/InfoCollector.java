package de.fhg.iais.roberta.typecheck;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.ast.TextRegion;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.visitor.IInfoCollectable;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * This class is implementing {@link IVisitor}. It is responsible for collecting the infos generated (and stored in the Phrases) during typechecking for an AST.
 * It does this by traversing the AST using <b>reflection</b>. Be careful when changing this visitor and when changing the overall structure of an AST.
 */
public class InfoCollector {

    private final List<NepoInfo> infos = new ArrayList<>();

    private static final String TYPECHECK_ERROR_KEY = "PROGRAM_ERROR_EXPRBLOCK_TYPECHECK";
    private final List<JSONObject> textlyErrors = new ArrayList<>();
    private TextRegion lastValidTextRegion = null;


    public static List<JSONObject> collectTextlyErrorsAsJson(Phrase phrase) {
        try {
            InfoCollector astVisitor = new InfoCollector();
            return astVisitor.collectTextlyJson(phrase);
        } catch ( IllegalAccessException e ) {
            throw new DbcException("textly error collection failed");
        }
    }

    private List<JSONObject> collectTextlyJson(Object object) throws IllegalAccessException {
        List<JSONObject> textlyErrors = new ArrayList<>();


        if ( object != null ) {
            Class<?> clazz = object.getClass();


            if ( List.class.isAssignableFrom(clazz) ) {
                for ( Object item : (List<?>) object ) {
                    textlyErrors.addAll(collectTextlyJson(item));
                }
            } else if ( Phrase.class.isAssignableFrom(clazz) ) {
                Phrase phrase = (Phrase) object;

                TextRegion currentTextRegion = phrase.getProperty().getTextRegion();
                if ( currentTextRegion != null ) {
                    lastValidTextRegion = currentTextRegion;
                }

                for ( NepoInfo info : phrase.getInfos().getInfos() ) {
                    if ( info.getSeverity().equals(NepoInfo.Severity.ERROR) ) {

                        if ( info.getMessage().contains("PROGRAM_ERROR_EXPRBLOCK_TYPECHECK") ) {
                            String customMessage = info.getMessage().replace("PROGRAM_ERROR_EXPRBLOCK_TYPECHECK ", "");

                            if ( lastValidTextRegion != null ) {
                                JSONObject errorObject = new JSONObject();
                                errorObject.put("line", lastValidTextRegion.getLineStart());
                                errorObject.put("charPositionInLine", lastValidTextRegion.getColStart());
                                errorObject.put("offendingSymbol", "");
                                errorObject.put("message", customMessage);

                                textlyErrors.add(errorObject);
                            }
                        }
                    }
                }


                for ( Field field : clazz.getFields() ) {
                    textlyErrors.addAll(collectTextlyJson(field.get(phrase)));
                }
            } else if ( IInfoCollectable.class.isAssignableFrom(clazz) ) {
                IInfoCollectable collectable = (IInfoCollectable) object;
                for ( Field field : clazz.getDeclaredFields() ) {
                    textlyErrors.addAll(collectTextlyJson(field.get(collectable)));
                }
            }
        }

        return textlyErrors;
    }

    /**
     * initialize the info collector visitor.
     */
    private InfoCollector() {
    }

    /**
     * collect the infos generated (and stored in the Phrases) during typechecking for an AST. This is done by a visitor, which is an instance of this class
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

    /**
     * collect the infos generated (and stored in the Phrases) during typechecking for an AST and put the messages into the "parent" AST object
     *
     * @param phrase whose infos should be collected
     * @return list of collected infos
     */
    public static void collectInfosAndStore(Phrase phrase) //
    {
        List<NepoInfo> nepoInfos = InfoCollector.collectInfos(phrase);
        for ( NepoInfo info : nepoInfos ) {
            phrase.addNepoInfo(info);
            if ( info.getSeverity().equals(NepoInfo.Severity.ERROR) ) {
                phrase.addError(info.getMessage());
            }

        }
    }

    /**
     * collect infos generated by the typechecker by reflection. It traverses the AST by identifying all fields recursively, that are Phrases and extracts the
     * infos from them.
     *
     * @param object
     * @throws IllegalAccessException
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
                    if ( info.getSeverity().equals(NepoInfo.Severity.ERROR) && !infos.contains(info) ) {
                        infos.add(info);
                    }
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
