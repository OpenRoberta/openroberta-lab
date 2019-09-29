package de.fhg.iais.roberta.worker.transfer;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.worker.IWorker;

public class Bob3TransferWorker implements IWorker {

    @Override
    public void execute(Project project) {
        // Create agent does not require execution of run call and transferring the binary
        if ( project.getRobotCommunicator().getState(project.getToken()) == null ) {
            project.setResult(Key.ROBOT_PUSH_RUN);
        } else { // otherwise it uses Connector Program
            Key run = project.getRobotCommunicator().run(project.getToken(), project.getRobot(), project.getProgramName());
            project.setResult(run);
        }
    }

}
