package de.fhg.iais.roberta.syntax.programcheck.ev3;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.ev3.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class PythonGlobalVariableCheckTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithOneElement() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/python_global_variables_check_one_used_variables.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, null);
        Assert.assertEquals("[Element3]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithNoElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/python_global_variables_check_no_used_variables.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, null);
        Assert.assertEquals("[]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

    @Test
    public void check_GlobalVariableUsedInUserCreatedFunction_returnsListWithTwoElements() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/visitors/python_global_variables_check_two_used_variables.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, null);
        Assert.assertEquals("[Element, Element3]", checkVisitor.getMarkedVariablesAsGlobal().toString());

    }

}
