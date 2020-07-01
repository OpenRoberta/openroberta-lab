package de.fhg.iais.roberta.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.NepoInfos;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.ITransformerVisitor;
import de.fhg.iais.roberta.visitor.IVisitor;

/**
 * the top class of all class used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immutable. The logic to achieve
 * that is in this class. An object of a subclass of {@link Phrase} is initially writable, after the construction of the object has finished,
 * {@link #setReadOnly()} is called. This cannot be undone later. It is expected that all subclasses of {@link #Phrase} do the following:<br>
 * - if in construction phase, they should use {@link #mayChange()} to assert that.<br>
 * - if the construction has finished and {@link #setReadOnly()} has been called, they should use {@link #isReadOnly()} to assert their immutability.<br>
 * <br>
 * There are two ways for a client to find out which kind a {@link #Phrase}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
abstract public class Phrase<V> {
    private static final Logger LOG = LoggerFactory.getLogger(Phrase.class);

    private boolean readOnly = false;

    private final BlocklyBlockProperties property;
    private final BlocklyComment comment;
    private final BlockType kind;

    private final NepoInfos infos = new NepoInfos(); // the content of the info object is MUTABLE !!!

    /**
     * This constructor set the kind of the object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockType}.
     *
     * @param kind of the the object used in AST,
     * @param disabled,
     * @param comment that the user added to the block
     */
    public Phrase(BlockType kind, BlocklyBlockProperties property, BlocklyComment comment) {
        Assert.isTrue(property != null, "block property is null!");
        this.kind = kind;
        this.property = property;
        this.comment = comment;
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
     * @return the kind of the expression. See enum {@link BlockType} for all kinds possible<br>
     */
    public final BlockType getKind() {
        return this.kind;
    }

    public BlocklyBlockProperties getProperty() {
        return this.property;
    }

    /**
     * @return comment that the user added to the block
     */
    public final BlocklyComment getComment() {
        return this.comment;
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
    public final V accept(IVisitor<V> visitor) {
        // LOG.info("{}", this);
        if ( getProperty().isDisabled() || (getProperty().isInTask() != null && getProperty().isInTask() == false) ) {
            return null;
        }
        return this.acceptImpl(visitor);
    }

    /**
     * accept an visitor
     */
    protected abstract V acceptImpl(IVisitor<V> visitor);

    /**
     * Can be used to modify the Phrase itself. Used in conjunction with {@link ITransformerVisitor} to replace phrases with copies of themselves or even other
     * phrases.
     *
     * @param visitor the modify visitor to use
     * @return a newly constructed phrase
     */
    public final Phrase<Void> modify(ITransformerVisitor<?> visitor) {
        // don't use accept, go over ALL blocks
        @SuppressWarnings("unchecked")
        V v = this.acceptImpl((IVisitor<V>) visitor);

        if ( v instanceof Phrase ) {
            @SuppressWarnings("unchecked")
            Phrase<Void> voidPhrase = (Phrase<Void>) v;
            return voidPhrase;
        } else {
            throw new DbcException("Template parameter of this phrase is not a Phrase!");
        }
    }

    /**
     * @return converts AST representation of block to JAXB representation of block
     */
    public abstract Block astToBlock();

    /**
     * append a newline, then append spaces up to an indentation level, then append an (optional) text<br>
     * helper for constructing readable {@link #toString()}- and {@link #generateJava(StringBuilder, int)}-methods for statement trees
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
