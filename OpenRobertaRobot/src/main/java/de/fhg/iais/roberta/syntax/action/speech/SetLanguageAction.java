package de.fhg.iais.roberta.syntax.action.speech;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISpeechVisitor;

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

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISpeechVisitor<V>) visitor).visitSetLanguageAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);

        String language = helper.extractField(fields, BlocklyConstants.LANGUAGE);

        return SetLanguageAction.make(factory.getLanguageMode(language), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.LANGUAGE, this.language.toString());

        return jaxbDestination;
    }
}
