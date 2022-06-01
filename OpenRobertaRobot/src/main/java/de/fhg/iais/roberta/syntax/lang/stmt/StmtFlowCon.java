package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>controls_flow_statements</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for flow control of a statement. <br>
 * See enum {@link Flow} for all possible flows.
 */
@NepoPhrase(containerType = "STMT_FLOW_CONTROL")
public class StmtFlowCon<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.FLOW)
    public final Flow flow;

    public StmtFlowCon(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Flow flow) {
        super(kind, properties, comment);
        Assert.isTrue(flow != null);
        this.flow = flow;
        setReadOnly();
    }

    /**
     * Create read only object of {@link StmtFlowCon}.
     *
     * @param flow; must be <b>not</b> null; see enum {@link Flow} for all the possible kind of flow controls,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StmtFlowCon}
     */
    public static <V> StmtFlowCon<V> make(Flow flow, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StmtFlowCon<V>(BlockTypeContainer.getByName("STMT_FLOW_CONTROL"), properties, comment, flow);
    }

    /**
     * @return the kind of control. See enum {@link Flow} for all the possible kind of flow controls
     */
    public Flow getFlow() {
        return this.flow;
    }

    /**
     * Flow controls that statement can have.
     */
    public enum Flow {
        CONTINUE(), BREAK();

        private final String[] values;

        Flow(String... values) {
            this.values = values;
        }
    }

}
