package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents <b>text_prompt</b> blocks from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(List, BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link Functions} contains all allowed functions.
 */
public class TextPromptFunct<V> extends Function<V> {
    private final Functions functName;
    private final String text;

    private TextPromptFunct(Functions name, String strParam, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.TEXT_PROMPT_FUNCT, properties, comment);
        Assert.isTrue(name != null && strParam != null);
        this.functName = name;
        this.text = strParam;
        setReadOnly();
    }

    /**
     * Creates instance of {@link TextPromptFunct}. This instance is read only and can not be modified.
     *
     * @param name of the function,
     * @param param list of expression parameters for the function,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @param strParam list of string parameters for the function
     * @return read only object of class {@link TextPromptFunct}
     */
    public static <V> TextPromptFunct<V> make(Functions name, String strParam, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TextPromptFunct<V>(name, strParam, properties, comment);
    }

    /**
     * @return name of the function
     */
    public Functions getFunctName() {
        return this.functName;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public int getPrecedence() {
        return this.functName.getPrecedence();
    }

    @Override
    public Assoc getAssoc() {
        return this.functName.getAssoc();
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return "TextPromptFunct [" + this.functName + ", " + this.text + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addField(jaxbDestination, "TYPE", getFunctName().name());
        AstJaxbTransformerHelper.addField(jaxbDestination, "TEXT", getText());
        return jaxbDestination;
    }
}
