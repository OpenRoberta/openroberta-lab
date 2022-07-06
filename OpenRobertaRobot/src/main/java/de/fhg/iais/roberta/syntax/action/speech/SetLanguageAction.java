package de.fhg.iais.roberta.syntax.action.speech;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "SET_LANGUAGE", category = "ACTOR", blocklyNames = {"naoActions_setLanguage", "robActions_setLanguage"})
public final class SetLanguageAction<V> extends Action<V> {

    public final ILanguage language;

    public SetLanguageAction(ILanguage language, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(language, "Missing language in SetLanguage block!");
        this.language = language;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "SetLanguage [" + this.language + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);

        String language = Jaxb2Ast.extractField(fields, BlocklyConstants.LANGUAGE);

        return new SetLanguageAction<V>(factory.getLanguageMode(language), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LANGUAGE, this.language.toString());

        return jaxbDestination;
    }
}
