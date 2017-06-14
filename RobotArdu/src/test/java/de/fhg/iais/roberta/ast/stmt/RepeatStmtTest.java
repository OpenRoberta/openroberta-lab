package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNroll;

public class RepeatStmtTest {
	HelperBotNroll h = new HelperBotNroll();

    @Test
    public void loopForeverArdu() throws Exception {
        String a = "BlockAST[project=[[Location[x=9,y=91],(repeat[FOREVER_ARDU,BoolConst[true]])]]]";

        Assert.assertEquals(a.replaceAll("\\s+", ""), this.h.generateTransformerString("/syntax/stmt/loop_forever_ardu.xml").replaceAll("\\s+", ""));
    }

}
