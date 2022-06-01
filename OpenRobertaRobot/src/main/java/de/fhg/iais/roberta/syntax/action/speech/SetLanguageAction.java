package de.fhg.iais.roberta.syntax.action.speech;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>naoActions_setLanguage</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting the language of the robot.<br/>
 * <br/>
 * The client must provide the {@link ILanguage} (the language NAOs speech engine is set to).
 */
public final class SetLanguageAction<V> extends Action<V> {

    private final ILanguage language;

    private SetLanguageAction(ILanguage language, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_LANGUAGE"), properties, comment);
        Assert.notNull(language, "Missing language in SetLanguage block!");
        this.language = language;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "SetLanguage [" + this.language + "]";
    }

    /**
     * Creates instance of {@link SetLanguageAction}. This instance is read only and can not be modified.
     *
     * @param language {@link ILanguage} the speech engine of the robot is set to,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetLanguageAction}
     */
    public static <V> SetLanguageAction<V> make(ILanguage language, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetLanguageAction<V>(language, properties, comment);
    }

    public ILanguage getLanguage() {
        return this.language;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);

        String language = Jaxb2Ast.extractField(fields, BlocklyConstants.LANGUAGE);

        return SetLanguageAction.make(factory.getLanguageMode(language), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LANGUAGE, this.language.toString());

        return jaxbDestination;
    }
}
