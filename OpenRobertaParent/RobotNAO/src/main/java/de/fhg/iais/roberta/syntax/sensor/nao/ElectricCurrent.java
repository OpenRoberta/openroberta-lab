package de.fhg.iais.roberta.syntax.sensor.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.mode.action.nao.Joint;
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
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for making
 * the robot walk for a distance.<br/>
 * <br/>
 * The client must provide the {@link joint} and {@link degrees} (direction and distance to walk).
 */
public final class ElectricCurrent<V> extends Sensor<V> {

    private final Joint joint;

    private ElectricCurrent(Joint joint, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ELECTRIC_CURRENT"), properties, comment);
        Assert.notNull(joint, "Missing joint in ElectricCurrent block!");
        this.joint = joint;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ElectricCurrent}. This instance is read only and can not be modified.
     *
     * @param joint {@link joint} the sensors data will be read from,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ElectricCurrent}
     */
    private static <V> ElectricCurrent<V> make(Joint joint, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ElectricCurrent<V>(joint, properties, comment);
    }

    public Joint getJoint() {
        return this.joint;
    }

    @Override
    public String toString() {
        return "ElectricCurrent [" + this.joint + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitElectricCurrent(this);
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

        String joint = helper.extractField(fields, BlocklyConstants.JOINT);

        return ElectricCurrent.make(Joint.get(joint), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.JOINT, this.joint.toString());

        return jaxbDestination;
    }
}
