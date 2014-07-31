package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_play_file</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for playing a stored music file in the brick.<br/>
 * <br/>
 * The client must provide the name of the file.
 */
public class PlayFileAction extends Action {
    private final String filename;

    private PlayFileAction(String filename) {
        super(Phrase.Kind.PLAY_FILE_ACTION);
        Assert.isTrue(!filename.equals(""));
        this.filename = filename;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PlayFileAction}. This instance is read only and can not be modified.
     * 
     * @param filename of the sound,
     * @return read only object of class {@link PlayFileAction}.
     */
    public static PlayFileAction make(String filename) {
        return new PlayFileAction(filename);
    }

    /**
     * @return the name of the file that will be played
     */
    public String getFilename() {
        return this.filename;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("hal.playFile(\"" + this.filename + "\");");
    }

    @Override
    public String toString() {
        return "PlayFileAction [" + this.filename + "]";
    }

}
