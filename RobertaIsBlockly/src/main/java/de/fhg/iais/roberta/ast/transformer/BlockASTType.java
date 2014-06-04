package de.fhg.iais.roberta.ast.transformer;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.blockly.generated.Block;

public abstract class BlockASTType {

    abstract Phrase blockToAST(BlockAST blockAST, Block block);

    public static enum Types {
        controls_repeat_ext( ControlsRepeatExt.class ), math_number( MathNumber.class );
        private final Class<?> clazz;

        private Types(Class<?> clazz) {
            this.clazz = clazz;
        }

        public BlockASTType make() {

            try {
                return (BlockASTType) this.clazz.newInstance();
            } catch ( Exception e ) {
                throw new RuntimeException(e);
            }
        }
    }
}
