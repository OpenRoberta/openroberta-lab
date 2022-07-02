package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"controls_flow_statements"}, name = "STMT_FLOW_CONTROL")
public final class StmtFlowCon<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.FLOW)
    public final Flow flow;

    public StmtFlowCon(BlocklyBlockProperties properties, BlocklyComment comment, Flow flow) {
        super(properties, comment);
        Assert.isTrue(flow != null);
        this.flow = flow;
        setReadOnly();
    }

    public enum Flow {
        CONTINUE(), BREAK();

        public final String[] values;

        Flow(String... values) {
            this.values = values;
        }
    }

}
