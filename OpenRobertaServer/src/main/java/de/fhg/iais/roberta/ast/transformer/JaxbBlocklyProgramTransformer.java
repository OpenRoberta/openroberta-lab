package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;

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
        return Phrase.Kind.invokeJaxbToAstTransform(block, this);
    }
}
