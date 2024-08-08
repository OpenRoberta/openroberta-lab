package de.fhg.iais.roberta.javaServer.restServices.all.service;

import java.util.List;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.worker.IWorker;
import de.fhg.iais.roberta.worker.RegenerateNepoWorker;

public final class ProjectService {
    private ProjectService() {
    }

    public static void executeWorkflow(String workflowName, Project project) {
        List<IWorker> workflowPipe = project.getRobotFactory().getWorkerPipe(workflowName);
        for ( IWorker worker : workflowPipe ) {
            if ( project.hasSucceeded() || worker.mustRunEvenIfPreviousWorkerFailed()) {
                worker.execute(project);
            }
        }
    }
}
