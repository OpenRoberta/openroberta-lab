package de.fhg.iais.roberta.syntax.expr;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.TypecheckVisitor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Expr1add2Test extends AstTest {
    private static final Logger LOG = LoggerFactory.getLogger(Expr1add2Test.class);

    /**
     * take XML and generate the AST from that. Check that the AST is ok.
     */
    @Test
    public void test1add2xml() throws Exception {
        List<List<Phrase<Void>>> ast = UnitTestHelper.getProgramAst(testFactory, "/expressionblock/expr_1add2.xml");
        LOG.info("" + ast);
        //        TypecheckVisitor typechecker = TypecheckVisitor.makeVisitorAndTypecheck(ast);
        //        Assert.assertEquals(0, typechecker.getErrorCount());
    }

    /**
     * create a correct AST programmatically. Check that the AST is ok.<br>
     * create an incorrect AST programmatically. Check that the AST is wrong.<br>
     */
    @Test
    public void test1add2text() throws Exception {
        NumConst<BlocklyType> n1 = NumConst.make("1");
        NumConst<BlocklyType> n2 = NumConst.make("2");
        BoolConst<BlocklyType> b1 = BoolConst.make(true);
        BoolConst<BlocklyType> b2 = BoolConst.make(true);

        Binary<BlocklyType> add = Binary.make(Op.ADD, n1, n2, "");
        TypecheckVisitor tcAdd = TypecheckVisitor.makeVisitorAndTypecheck(add);
        Assert.assertEquals(0, tcAdd.getErrorCount());
        LOG.info("" + add);

        Binary<BlocklyType> and = Binary.make(Op.AND, b1, b2, "");
        TypecheckVisitor tcAnd = TypecheckVisitor.makeVisitorAndTypecheck(and);
        Assert.assertEquals(0, tcAnd.getErrorCount());
        LOG.info("" + and);

        Binary<BlocklyType> mix = Binary.make(Op.ADD, n1, b1, "");
        TypecheckVisitor tcMix = TypecheckVisitor.makeVisitorAndTypecheck(mix);
        Assert.assertEquals(1, tcMix.getErrorCount());
        LOG.info("" + mix);
    }

    /**
     * create a AST programmatically. Generate code from that using the Calliope C++ code generator.
     */
    @Test
    public void test1add2codegen() throws Exception {
        NumConst<Void> n1 = NumConst.make("1");
        NumConst<Void> n2 = NumConst.make("2");
        NumConst<Void> n4 = NumConst.make("4");
        Binary<Void> add2 = Binary.make(Op.ADD, n1, n2, "");
        Binary<Void> add3 = Binary.make(Op.ADD, add2, n4, "");
        ArrayList<Phrase<Void>> addInList = new ArrayList<>();
        addInList.add(add3);
        ArrayList<ArrayList<Phrase<Void>>> addInListInList = new ArrayList<>();
        addInListInList.add(addInList);
        //
        //        CalliopeCppVisitor visitor = new CalliopeCppVisitor(addInListInList);
        //        visitor.visitBinary(add3);
        //        LOG.info("generated code: " + visitor.getSb().toString());
    }
}
