package de.fhg.iais.roberta.syntax.programcheck.ev3;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableClassToInstanceMap;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedHardwareCollectorVisitor;

public class PythonGlobalVariableCheckTest extends AstTest {

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/visitors/python_global_variables_check_one_used_variables.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(null, ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[Element3]", bean.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithNoElements() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/visitors/python_global_variables_check_no_used_variables.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(null, ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[]", bean.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithTwoElements() throws Exception {
        List<List<Phrase<Void>>> phrases = UnitTestHelper.getProgramAst(testFactory, "/visitors/python_global_variables_check_two_used_variables.xml");
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(null, ImmutableClassToInstanceMap.of(UsedHardwareBean.Builder.class, builder));
        for ( List<Phrase<Void>> tree : phrases ) {
            for ( Phrase<Void> phrase : tree ) {
                phrase.accept(checkVisitor);
            }
        }
        UsedHardwareBean bean = builder.build();
        Assert.assertEquals("[Element, Element3]", bean.getMarkedVariablesAsGlobal().toString());

    }

}
