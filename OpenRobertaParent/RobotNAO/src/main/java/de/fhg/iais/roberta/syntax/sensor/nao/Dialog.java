package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.nao.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_sayText</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * making the robot say the text.<br>
 * <br>
 * <br>
 */
public class Dialog<V> extends Sensor<V> {
    private final DialogPhrase phrase;

    private Dialog(DialogPhrase phrase, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DIALOG"), properties, comment);
        Assert.isTrue(phrase != null);
        this.phrase = phrase;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DisplayTextAction}. This instance is read only and can not be modified.
     *
     * @param msg {@link msg} that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplayTextAction}
     */
    static <V> Dialog<V> make(DialogPhrase phrase, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Dialog<>(phrase, properties, comment);
    }

    /**
     * @return the phrase.
     */
    public DialogPhrase getPhrase() {
        return this.phrase;
    }

    @Override
    public String toString() {
        return "Dialog [" + this.phrase + ", " + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitDialog(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String phrase = helper.extractField(fields, BlocklyConstants.PHRASE);
        return Dialog.make(DialogPhrase.valueOf(phrase), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.PHRASE, this.phrase.toString());

        return jaxbDestination;
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum DialogPhrase {
        HAND( "hand" ), HEAD( "head" ), BUMPER( "bumper" );

        private final String pythonCode;

        private DialogPhrase(String pythonCode) {
            this.pythonCode = pythonCode;
        }

        public String getPythonCode() {
            return this.pythonCode;
        }
    }
}
