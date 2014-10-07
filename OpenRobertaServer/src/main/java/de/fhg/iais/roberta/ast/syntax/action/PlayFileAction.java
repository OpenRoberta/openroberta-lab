package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_play_file</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for playing a stored music file in the brick.<br/>
 * <br/>
 * The client must provide the name of the file.
 */
public class PlayFileAction<V> extends Action<V> {
    private final String fileName;

    private PlayFileAction(String fileName, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.PLAY_FILE_ACTION, properties, comment);
        Assert.isTrue(!fileName.equals(""));
        this.fileName = fileName;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PlayFileAction}. This instance is read only and can not be modified.
     * 
     * @param filename of the sound,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PlayFileAction}.
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitPlayFileAction(this);
    }
}
