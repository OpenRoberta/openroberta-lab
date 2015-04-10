package de.fhg.iais.roberta.ast.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * JAXB to AST transformer. Client should provide tree of jaxb objects.
 */
public class JaxbBlocklyProgramTransformer<V> extends JaxbAstTransformer<V> {

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
        String sUpper = block.getType().trim();
        for ( Phrase.Kind co : Phrase.Kind.values() ) {
            if ( co.toString().equals(sUpper) ) {
                return invokeMethod(block, co.getAstClass().getName());
            }
            for ( String value : co.getBlocklyNames() ) {
                if ( sUpper.equals(value) ) {
                    return invokeMethod(block, co.getAstClass().getName());
                }

            }
        }
        throw new DbcException("Invalid Block: " + block.getType());
    }

    @SuppressWarnings("unchecked")
    private Phrase<V> invokeMethod(Block block, String className) {
        Method method;
        try {
            method = Class.forName(className).getMethod("jaxbToAst", Block.class, JaxbAstTransformer.class);
            return (Phrase<V>) method.invoke(null, block, this);
        } catch ( NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | DbcException e ) {
            throw new DbcException(e.getCause().getMessage());
        }
    }
}
