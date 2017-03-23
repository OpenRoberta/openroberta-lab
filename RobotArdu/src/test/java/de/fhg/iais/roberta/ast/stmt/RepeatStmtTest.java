package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.Helper;

public class RepeatStmtTest {
    Helper h = new Helper();

    @Test
    public void loopForeverArdu() throws Exception {
        String a = "BlockAST[project=[[Location[x=9,y=91],(repeat[FOREVER_ARDU,BoolConst[true]])]]]";

        Assert.assertEquals(a.replaceAll("\\s+", ""), this.h.generateTransformerString("/syntax/stmt/loop_forever_ardu.xml").replaceAll("\\s+", ""));
    }

}
