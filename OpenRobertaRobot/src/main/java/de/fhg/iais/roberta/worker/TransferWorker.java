package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;

/**
 * Transfers the previously compiled/saved result to the robot using the {@link RobotCommunicator}.
 */
public class TransferWorker implements IWorker {

    @Override
    public final void execute(Project project) {
        Key run = project.getRobotCommunicator().run(project.getToken(), project.getRobot(), project.getProgramName());
        project.setResult(run);
    }
}
