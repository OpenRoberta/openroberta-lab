package de.fhg.iais.roberta.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * JAXB to AST transformer. Client should provide tree of jaxb objects.
 */
public class Jaxb2ProgramAst<V> extends AbstractJaxb2Ast<V> {

    public Jaxb2ProgramAst(IRobotFactory robotFactory) {
        super(robotFactory);
    }

    /**
     * Converts object of type {@link BlockSet} to AST tree.
     *
     * @param set the BlockSet to transform
     */
    public ProgramAst<V> blocks2Ast(BlockSet set) {
        ProgramAst.Builder<V> builder =
            new ProgramAst.Builder<V>()
                .setRobotType(set.getRobottype())
                .setXmlVersion(set.getXmlversion())
                .setDescription(set.getDescription())
                .setTags(set.getTags());

        List<Instance> instances = set.getInstance();
        for ( Instance instance : instances ) {
            builder.addToTree(instanceToAST(instance));
        }
        return builder.build();
    }

    private List<Phrase<V>> instanceToAST(Instance instance) {
        List<Block> blocks = instance.getBlock();
        Location<V> location = Location.make(instance.getX(), instance.getY());
        List<Phrase<V>> range = new ArrayList<>();
        range.add(location);
        for ( Block block : blocks ) {
            range.add(blockToAST(block));
        }
        return range;
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
        Method method = null;
        try {
            method = Class.forName(className).getMethod("jaxbToAst", Block.class, AbstractJaxb2Ast.class);
            return (Phrase<V>) method.invoke(null, block, this);
        } catch ( NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | DbcException e ) {
            if ( method == null ) {
                throw new DbcException("Could not get method for " + className, e.getCause());
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Could not invoke method ");
                sb.append(method.getName());
                sb.append(" for block ");
                sb.append(block.getType());
                sb.append(" with fields ");
                block.getField().forEach(field -> {
                    sb.append(field.getName());
                    sb.append(" ");
                    sb.append(field.getValue());
                    sb.append(" ");
                });
                throw new DbcException(sb.toString(), e.getCause());
            }
        }
    }
}
