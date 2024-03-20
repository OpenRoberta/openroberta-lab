package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;

public class CalliopeV3StackMachineVisitor extends CalliopeStackMachineVisitor {
    public CalliopeV3StackMachineVisitor(
        ConfigurationAst configuration,
        List<List<Phrase>> phrases,
        UsedHardwareBean usedHardwareBean,
        NNBean nnBean) {
        super(configuration, phrases, usedHardwareBean, nnBean);
    }
}
