package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;

public abstract class AbstractUsedHardwareCollectorWorker implements IWorker {

    protected abstract AbstractCollectorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project);

    @Override
    public void execute(Project project) {
        UsedHardwareBean.Builder builder = new UsedHardwareBean.Builder();
        AbstractCollectorVisitor visitor = getVisitor(builder, project);
        ArrayList<ArrayList<Phrase<Void>>> tree = project.getProgramAst().getTree();
        collectGlobalVariables(tree, visitor);
        for ( ArrayList<Phrase<Void>> phrases : tree ) {
            for ( Phrase<Void> phrase : phrases ) {
                if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                    builder.setProgramEmpty(phrases.size() == 2);
                } else {
                    phrase.accept(visitor);
                }
            }
        }
        UsedHardwareBean bean = builder.build();
        project.addWorkerResult("CollectedHardware", bean);
    }

    private void collectGlobalVariables(List<ArrayList<Phrase<Void>>> phrasesSet, IVisitor<Void> visitor) {
        for ( List<Phrase<Void>> phrases : phrasesSet ) {
            Phrase<Void> phrase = phrases.get(1);
            if ( phrase.getKind().getName().equals("MAIN_TASK") ) {
                phrase.accept(visitor);
            }
        }
    }
}
