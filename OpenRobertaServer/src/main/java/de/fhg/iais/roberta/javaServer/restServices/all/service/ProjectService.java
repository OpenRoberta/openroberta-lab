package de.fhg.iais.roberta.javaServer.restServices.all.service;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.worker.IWorker;

public final class ProjectService {
    private static final Logger LOG = LoggerFactory.getLogger(ProjectService.class);

    private ProjectService() {
    }

    public static void executeWorkflow(String workflowName, IRobotFactory robotFactory, Project project) throws JSONException, JAXBException {
        List<IWorker> workflowPipe = robotFactory.getWorkerPipe(workflowName);
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
