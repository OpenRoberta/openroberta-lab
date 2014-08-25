package de.fhg.iais.roberta.ast.syntax.functions;

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.functions.Func.Function;
import de.fhg.iais.roberta.dbc.DbcException;

public class TextFunctions {

    @Test
    public void getPresedence() {
        ArrayList<Expr> param = new ArrayList<Expr>();
        StringConst stringConst = StringConst.make("AS");
        param.add(stringConst);
        Func funct = Func.make(Function.ABS, param);
        Assert.assertEquals(10, funct.getPrecedence());
    }

    @Test
    public void getAssoc() {
        ArrayList<Expr> param = new ArrayList<Expr>();
        StringConst stringConst = StringConst.make("AS");
        param.add(stringConst);
        Func funct = Func.make(Function.ABS, param);
        Assert.assertEquals(Assoc.LEFT, funct.getAssoc());
    }

    @Test
    public void getOpSymbol() {
        ArrayList<Expr> param = new ArrayList<Expr>();
        StringConst stringConst = StringConst.make("AS");
        param.add(stringConst);
        Func funct = Func.make(Function.POWER, param);
        Assert.assertEquals("^", funct.getFunctName().getOpSymbol());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Func.Function funct = Function.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Func.Function funct = Function.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Func.Function funct = Function.get("asdf");
    }

    @Test
    public void concatination() throws Exception {
        Helper.generateTransformerString("/syntax/functions/text_concat.xml");

        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";
        // Assert.assertEquals(a, transformer.toString());
    }
}