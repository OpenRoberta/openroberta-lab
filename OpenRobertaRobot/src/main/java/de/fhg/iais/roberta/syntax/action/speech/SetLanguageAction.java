package de.fhg.iais.roberta.syntax.action.speech;

import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SET_LANGUAGE", category = "ACTOR", blocklyNames = {"naoActions_setLanguage", "robActions_setLanguage"})
public final class SetLanguageAction extends Action {

    @NepoField(name = "LANGUAGE")
    public final Language language;

    public SetLanguageAction(BlocklyProperties properties, Language language) {
        super(properties);
        Assert.notNull(language, "Missing language in SetLanguage block!");
        this.language = language;
        setReadOnly();
    }
}
