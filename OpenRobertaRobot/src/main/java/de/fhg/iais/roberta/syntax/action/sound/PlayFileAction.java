package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;

/**
 * This class represents the <b>robActions_play_file</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * playing a stored music file in the brick.<br/>
 * <br/>
 * The client must provide the name of the file.
 */
public class PlayFileAction<V> extends Action<V> {
    private final String fileName;

    private PlayFileAction(String fileName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PLAY_FILE_ACTION"), properties, comment);
        Assert.isTrue(!fileName.equals(""));
        this.fileName = fileName;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PlayFileAction}. This instance is read only and can not be modified.
     *
     * @param filename of the sound; must be different then empty string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PlayFileAction}
     */
    public static <V> PlayFileAction<V> make(String filename, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlayFileAction<V>(filename, properties, comment);
    }

    /**
     * @return the name of the file that will be played
     */
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public String toString() {
        return "PlayFileAction [" + this.fileName + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISoundVisitor<V>) visitor).visitPlayFileAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String filename = helper.extractField(fields, BlocklyConstants.FILE);
        return PlayFileAction.make(filename, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        String fieldValue = getFileName();
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.FILE, fieldValue);
        return jaxbDestination;
    }
}
