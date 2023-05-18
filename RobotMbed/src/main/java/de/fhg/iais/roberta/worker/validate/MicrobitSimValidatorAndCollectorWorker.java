package de.fhg.iais.roberta.worker.validate;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.MicrobitValidatorAndCollectorVisitor;

public class MicrobitSimValidatorAndCollectorWorker extends MicrobitValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        Boolean displaySwitchUsed = false;
        for ( List<Phrase> subTree : project.getProgramAst().getTree() ) {
            displaySwitchUsed = subTree.toString().contains("SwitchLedMatrix");
        }
        return new MicrobitValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, true, displaySwitchUsed);
    }
}
