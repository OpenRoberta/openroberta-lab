package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class TextFunctions {
    @Test
    public void getPresedence() {
        ArrayList<Expr<Void>> param = new ArrayList<>();
        StringConst<Void> stringConst = StringConst.make("AS", BlocklyBlockProperties.make("1", "1"), null);
        param.add(stringConst);
        MathPowerFunct<Void> funct = MathPowerFunct.make(FunctionNames.ABS, param, BlocklyBlockProperties.make("1", "1"), null);
        Assert.assertEquals(10, funct.getPrecedence());
    }

    @Test
    public void getAssoc() {
        ArrayList<Expr<Void>> param = new ArrayList<>();
        StringConst<Void> stringConst = StringConst.make("AS", BlocklyBlockProperties.make("1", "1"), null);
        param.add(stringConst);
        MathPowerFunct<Void> funct = MathPowerFunct.make(FunctionNames.ABS, param, BlocklyBlockProperties.make("1", "1"), null);
        Assert.assertEquals(Assoc.LEFT, funct.getAssoc());
    }

    @Test
    public void getOpSymbol() {
        ArrayList<Expr<Void>> param = new ArrayList<>();
        StringConst<Void> stringConst = StringConst.make("AS", BlocklyBlockProperties.make("1", "1"), null);
        param.add(stringConst);
        MathPowerFunct<Void> funct = MathPowerFunct.make(FunctionNames.POWER, param, BlocklyBlockProperties.make("1", "1"), null);
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
}