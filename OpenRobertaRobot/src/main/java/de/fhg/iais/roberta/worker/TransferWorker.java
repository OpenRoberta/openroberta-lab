package de.fhg.iais.roberta.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;

/**
 * Transfers the previously compiled/saved result to the robot using the {@link RobotCommunicator}.
 */
public class TransferWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(TransferWorker.class);

    @Override
    public void execute(Project project) {
        Key run = project.getRobotCommunicator().run(project.getToken(), project.getRobot(), project.getProgramName());
        project.setResult(run);
    }
}
