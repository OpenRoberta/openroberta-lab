package de.fhg.iais.roberta.javaServer.restServices.all.service;

import java.util.List;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.worker.IWorker;

public final class ProjectService {
    private ProjectService() {
    }

    public static void executeWorkflow(String workflowName, Project project) {
        List<IWorker> workflowPipe = project.getRobotFactory().getWorkerPipe(workflowName);
        if ( project.hasSucceeded() ) {
            for ( IWorker worker : workflowPipe ) {
                worker.execute(project);
                if ( !project.hasSucceeded() ) {
                    break;
                }
                // TODO: here separators in the worker list should be used and not such a hard condition
            }
        }
    }
}
