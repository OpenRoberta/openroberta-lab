package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.AnnotationHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.NepoInfos;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.TransformerVisitor;

/**
 * the top class of all class used to represent the AST (abstract syntax tree) of a program. After construction an AST object is immutable (except {@link #infos}
 * and {@link #blocklyType}.<br>
 * An object of a subclass of this class is initially writable, after the construction of the object has finished,
 * {@link #setReadOnly()} is called. This cannot be undone later. It is expected that all subclasses of this class do the following:<br>
 * - in the construction phase, they should use {@link #mayChange()} to assert that they are in the construction phase.<br>
 * - if the construction has finished and {@link #setReadOnly()} has been called, they should use {@link #isReadOnly()} to assert their immutability.<br>
 * <br>
 * The kind of the Phrase-object is described in the sub-object {@link #blockDescriptor}
 */
abstract public class Phrase {
    private boolean readOnly = false;
    private final BlocklyProperties property;
    private final BlockDescriptor blockDescriptor;
    /**
     * Infos related to this AST-object. The info object is (internally) MUTABLE to store messages of the various visitors!!!
     */
    private final NepoInfos infos = new NepoInfos();
    /**
     * The type of this AST-object.
     * <ul>
     *     <li>if its is unknown, it is NOTHING</li>
     *     <li>some AST-objects know their type (e.g. numeric constants as '5' have type NUMBER) and store it</li>
     *     <li>some AST-objects get their type, when they are type-checked, i.e. when the TypeheckVisitor  traverses the AST-tree</li>
     * </ul>
     * <b>UNDER CONSTRUCTION, ask rbudde!</b>
     */
    private BlocklyType blocklyType = null;

    /**
     * This constructor saves meta information about AST-objects (subclasses of this class Phrase). The {@link #blockDescriptor} is generated from the
     * various annotations of the concrete AST-classes
     *
     * @param property describes representation-related properties of an object
     */
    public Phrase(BlocklyProperties property) {
        Assert.isTrue(property != null, "block property is null!");
        this.blockDescriptor = AstFactory.getBlockDescriptor(this.getClass());
        this.property = property;
    }

    /**
     * This constructor saves meta information about AST-objects (subclasses of this class Phrase).<br>
     * <b>this constructor should only be used for ConfigurationsComponents</b><br>
     * TODO: remove as fast as possible and replace by annotations
     *
     * @param blockDescriptor the descriptor for the ConfigurationsComponent
     * @param property the representation-related properties of the ConfigurationsComponent
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
     * @return the current type of this phrase. May change during type-checking.
     */
    public final BlocklyType getBlocklyType() {
        return this.blocklyType;
    }

    protected void setBlocklyType(BlocklyType blocklyType) {
        if ( this.blocklyType == null ) {
            this.blocklyType = blocklyType;
            return;
        }
        if ( this.blocklyType.equals(blocklyType) ) {
            return;
        }
        if ( this.blocklyType.equals(BlocklyType.NOTHING) || this.blocklyType.equals(BlocklyType.CAPTURED_TYPE) ) {
            this.blocklyType = blocklyType;
            return;
        }
        throw new DbcException("blocklyTypes inconsistent. Was: " + this.blocklyType + ", should be set to: " + blocklyType);
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
        if ( property.blocklyRegion.disabled || (property.blocklyRegion.inTask != null && property.blocklyRegion.inTask == false) ) {
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
