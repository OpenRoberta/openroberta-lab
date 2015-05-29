package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.expr.Assoc;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class TextFunctions {

    @Test
    public void getPresedence() {
        ArrayList<Expr<Void>> param = new ArrayList<Expr<Void>>();
        StringConst<Void> stringConst = StringConst.make("AS", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        param.add(stringConst);
        MathPowerFunct<Void> funct =
            MathPowerFunct.make(FunctionNames.ABS, param, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        Assert.assertEquals(10, funct.getPrecedence());
    }

    @Test
    public void getAssoc() {
        ArrayList<Expr<Void>> param = new ArrayList<Expr<Void>>();
        StringConst<Void> stringConst = StringConst.make("AS", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        param.add(stringConst);
        MathPowerFunct<Void> funct =
            MathPowerFunct.make(FunctionNames.ABS, param, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        Assert.assertEquals(Assoc.LEFT, funct.getAssoc());
    }

    @Test
    public void getOpSymbol() {
        ArrayList<Expr<Void>> param = new ArrayList<Expr<Void>>();
        StringConst<Void> stringConst = StringConst.make("AS", BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        param.add(stringConst);
        MathPowerFunct<Void> funct =
            MathPowerFunct.make(FunctionNames.POWER, param, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        Assert.assertEquals("^", funct.getFunctName().getOpSymbol());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        FunctionNames.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        FunctionNames.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        FunctionNames.get("asdf");
    }

    @Test
    public void concatenation() throws Exception {
        Helper.generateTransformerString("/syntax/functions/text_concat.xml");

        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";
        // Assert.assertEquals(a, transformer.toString());
    }
}