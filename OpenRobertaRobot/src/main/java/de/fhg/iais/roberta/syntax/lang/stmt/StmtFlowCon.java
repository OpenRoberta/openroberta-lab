package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"controls_flow_statements"}, name = "STMT_FLOW_CONTROL")
public final class StmtFlowCon extends Stmt {
    @NepoField(name = BlocklyConstants.FLOW)
    public final Flow flow;

    public StmtFlowCon(BlocklyProperties properties, Flow flow) {
        super(properties);
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
