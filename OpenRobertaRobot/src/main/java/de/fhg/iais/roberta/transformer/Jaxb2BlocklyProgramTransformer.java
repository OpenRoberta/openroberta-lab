package de.fhg.iais.roberta.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to AST transformer. Client should provide tree of jaxb objects.
 */
public class Jaxb2BlocklyProgramTransformer<V> extends Jaxb2AstTransformer<V> {

    public Jaxb2BlocklyProgramTransformer(IRobotFactory robotFactory) {
        super(robotFactory);
    }

    /**
     * Converts object of type {@link BlockSet} to AST tree.
     *
     * @param program
     */
    public void transform(BlockSet set) {
        List<Instance> instances = set.getInstance();
        for ( Instance instance : instances ) {
            instanceToAST(instance);
        }
    }

    private void instanceToAST(Instance instance) {
        List<Block> blocks = instance.getBlock();
        Location<V> location = Location.make(instance.getX(), instance.getY());
        ArrayList<Phrase<V>> range = new ArrayList<Phrase<V>>();
        range.add(location);
        for ( Block block : blocks ) {
            range.add(blockToAST(block));
        }
        this.tree.add(range);
    }

    @Override
    protected Phrase<V> blockToAST(Block block) {
        return invokeJaxbToAstTransform(block);
    }

    private Phrase<V> invokeJaxbToAstTransform(Block block) {
        if ( block == null ) {
            throw new DbcException("Invalid block: " + block);
        }
        String type = block.getType().trim().toLowerCase();
        BlockType matchingBlockType = BlockTypeContainer.getByBlocklyName(type);
        Assert.notNull(matchingBlockType, "Invalid Block: " + block.getType());
        return invokeMethod(block, matchingBlockType.getAstClass().getName());
    }

    @SuppressWarnings("unchecked")
    private Phrase<V> invokeMethod(Block block, String className) {
        Method method;
        try {
            method = Class.forName(className).getMethod("jaxbToAst", Block.class, Jaxb2AstTransformer.class);
            return (Phrase<V>) method.invoke(null, block, this);
        } catch ( NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | DbcException e ) {
            throw new DbcException(e.getCause().getMessage());
        }
    }
}
