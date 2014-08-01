package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon.Flow;

public class StmtFlowContrTest {

    @Test
    public void make() throws Exception {
        StmtFlowCon flowCon = StmtFlowCon.make(Flow.BREAK);
        Assert.assertEquals("\nStmtFlowCon [BREAK]", flowCon.toString());
    }

    @Test
    public void getFlow() throws Exception {
        StmtFlowCon flowCon = StmtFlowCon.make(Flow.BREAK);
        Assert.assertEquals(Flow.BREAK, flowCon.getFlow());
    }

    @Test
    public void getFlowE() throws Exception {
        try {
            Flow.get("");

        } catch ( Exception e ) {
            Assert.assertEquals("Invalid flow kind: ", e.getMessage());
        }
    }

    @Test
    public void getFlowE1() throws Exception {
        Assert.assertEquals(Flow.CONTINUE, Flow.get("continue"));

    }
}
