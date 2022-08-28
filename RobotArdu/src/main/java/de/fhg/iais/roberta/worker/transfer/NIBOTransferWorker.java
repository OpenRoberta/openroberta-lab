package de.fhg.iais.roberta.worker.transfer;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.visitor.codegen.NIBOHexPrefix;
import de.fhg.iais.roberta.worker.IWorker;

public class NIBOTransferWorker implements IWorker {

    @Override
    public void execute(Project project) {
        // Check whether the robot is connected with create agent program...
        if ( project.getRobotCommunicator().getState(project.getToken()) == null ) {
            project.setResult(Key.ROBOT_PUSH_RUN);
        } else { // otherwise it uses Connector Program
            Key run = project.getRobotCommunicator().run(project.getToken(), project.getRobot(), project.getProgramName());
            project.setResult(run);
        }
    }
}
