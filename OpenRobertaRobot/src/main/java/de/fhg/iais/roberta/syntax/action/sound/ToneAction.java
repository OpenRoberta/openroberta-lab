package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;

/**
 * This class represents the <b>robActions_play_tone</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * making sound.<br/>
 * <br/>
 * The client must provide the frequency and the duration of the sound.
 */
public class ToneAction<V> extends Action<V> {
    private final Expr<V> frequency;
    private final Expr<V> duration;
    private final String port;

    private ToneAction(Expr<V> frequency, Expr<V> duration, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TONE_ACTION"), properties, comment);
        Assert.isTrue(frequency.isReadOnly() && duration.isReadOnly() && (frequency != null) && (duration != null));
        this.frequency = frequency;
        this.duration = duration;
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ToneAction}. This instance is read only and can not be modified.
     *
     * @param frequency of the sound; must be <b>not</b> null and <b>read only</b>,
     * @param duration of the sound; must be <b>not</b> null and <b>read only</b>,
     * @param port
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ToneAction}.
     */
    public static <V> ToneAction<V> make(Expr<V> frequency, Expr<V> duration, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ToneAction<>(frequency, duration, port, properties, comment);
    }

    /**
     * @return frequency of the sound
     */
    public Expr<V> getFrequency() {
        return this.frequency;
    }

    /**
     * @return duration of the sound.
     */
    public Expr<V> getDuration() {
        return this.duration;
    }

    /**
     * @return port of the buzzer.
     */
    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "ToneAction [" + this.frequency + ", " + this.duration + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISoundVisitor<V>) visitor).visitToneAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 3);
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.FREQUENCE, BlocklyType.NUMBER_INT));
        Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, BlocklyType.NUMBER_INT));
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        return ToneAction
            .make(
                helper.convertPhraseToExpr(left),
                helper.convertPhraseToExpr(right),
                factory.sanitizePort(port),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.FREQUENCE, getFrequency());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.DURATION, getDuration());
        return jaxbDestination;
    }
}
