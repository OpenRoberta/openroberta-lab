package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "GET_LANGUAGE", category = "ACTOR", blocklyNames = {"naoActions_getLanguage"}, blocklyType = BlocklyType.STRING)
public final class GetLanguage extends Action {

    public GetLanguage(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }
}
