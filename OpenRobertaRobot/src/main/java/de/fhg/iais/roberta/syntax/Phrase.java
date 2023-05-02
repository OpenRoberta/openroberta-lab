package de.fhg.iais.roberta.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.NepoInfos;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.TransformerVisitor;

/**
 * the top class of all class used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immutable.
 * An object of a subclass of {@link Phrase} is initially writable, after the construction of the object has finished,
 * {@link #setReadOnly()} is called. This cannot be undone later. It is expected that all subclasses of {@link #Phrase} do the following:<br>
 * - if in construction phase, they should use {@link #mayChange()} to assert that.<br>
 * - if the construction has finished and {@link #setReadOnly()} has been called, they should use {@link #isReadOnly()} to assert their immutability.<br>
 * <br>
 * To find out which kind a {@link #Phrase}-object is use {@link #getBlockDescriptor()}
 */
abstract public class Phrase {
    private static final Logger LOG = LoggerFactory.getLogger(Phrase.class);
    private boolean readOnly = false;
    private final BlocklyProperties property;
    private final BlockDescriptor blockDescriptor;

    private final NepoInfos infos = new NepoInfos(); // the content of the info object is MUTABLE !!!

    /**
     * This constructor set the kind of the object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockDescriptor}.
     *
     * @param comment that the user added to the block
     */
    public Phrase(BlocklyProperties property) {
        Assert.isTrue(property != null, "block property is null!");
        this.blockDescriptor = AstFactory.getBlockDescriptor(this.getClass());
        this.property = property;
    }

    /**
     * only for ConfigurationsComponent and TODO: remove as fast as possible
     *
     * @param blockDescriptor
     * @param property
     * @param comment
     */
    public Phrase(BlockDescriptor blockDescriptor, BlocklyProperties property) {
        Assert.isTrue(property != null, "block property is null!");
        this.blockDescriptor = blockDescriptor;
        this.property = property;
    }

    /**
     * @return true, if the object is writable/mutable. This is true, if {@link #setReadOnly()} has not yet been called for this object
     */
    public final boolean mayChange() {
        return !this.readOnly;
    }

    /**
     * @return true, if the object is read-only/immutable. This is true, if {@link #setReadOnly()} has been called for this object
     */
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * make this {@link Phrase}-object read-only/immutable. Should be called if the construction phase has finished
     */
    public final void setReadOnly() {
        this.readOnly = true;
    }

    /**
     * @return the block description of this phrase
     */
    public final BlockDescriptor getKind() {
        return this.blockDescriptor;
    }

    /**
     * @return true if name of this phrase (as found in its block descriptor) is equals to one of the names given as parameter
     */
    public final boolean hasName(String... namesToCheck) {
        return blockDescriptor.hasName(namesToCheck);
    }

    public BlocklyProperties getProperty() {
        return this.property;
    }

    /**
     * add an info (error, warning e.g.) to this phrase
     *
     * @param info to be added
     */
    public final void addInfo(NepoInfo info) {
        this.infos.addInfo(info);
    }

    public final NepoInfos getInfos() {
        return this.infos;
    }

    /**
     * visit this phrase. Inside this method is a LOG statement, usually commented out. If it is commented in, it will generate a nice trace of the phrases of
     * the AST when they are visited.
     *
     * @param visitor to be used
     */
    public final <V> V accept(IVisitor<V> visitor) {
        // LOG.info("{}", this);
        if ( getProperty().isDisabled() || (getProperty().isInTask() != null && getProperty().isInTask() == false) ) {
            return null;
        }
        return visitor.visit(this);
    }

    /**
     * Can be used to modify the Phrase itself. Used in conjunction with {@link TransformerVisitor} to replace phrases with copies of themselves or even other
     * phrases.
     *
     * @param visitor the modify visitor to use
     * @return a newly constructed phrase
     */
    public final Phrase modify(TransformerVisitor visitor) {
        return visitor.visit(this);
    }

    /**
     * converts the AST representation of this block to a XML representation<br>
     * <b>This is the default implementation of annotated AST classes</b>
     *
     * @return the XML representation, usable by blockly
     */
    public Block ast2xml() {
        return AnnotationHelper.ast2xml(this);
    }

    /**
     * the String representation of this phrase. To be used for debugging, not programming!<br>
     * <b>This is the default implementation of annotated AST classes</b>
     *
     * @return the String representation of this phrase
     */
    @Override
    public String toString() {
        String generated = AnnotationHelper.toString(this);
        return generated != null ? generated : super.toString();
    }

    /**
     * append a newline, then append spaces up to an indentation level, then append an (optional) text<br>
     * helper for constructing readable text from statement trees
     *
     * @param sb the string builder, to which has to be appended
     * @param indentation number defining the level of indentation
     * @param text an (optional) text to append; may be null
     */
    protected final void appendNewLine(StringBuilder sb, int indentation, String text) {
        sb.append("\n");
        for ( int i = 0; i < indentation; i++ ) {
            sb.append(" ");
        }
        if ( text != null ) {
            sb.append(text);
        }
    }
}
