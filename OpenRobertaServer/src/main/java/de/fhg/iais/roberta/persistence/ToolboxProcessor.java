package de.fhg.iais.roberta.persistence;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.Toolbox;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.ToolboxDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class ToolboxProcessor extends AbstractProcessor {
    public ToolboxProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState.getUserId());
    }

    public Toolbox getToolbox(String toolboxName, int userId, String robotName) {
        if ( !Util.isValidJavaIdentifier(toolboxName) ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("TOOLBOX_NAME", toolboxName);
            setStatus(ProcessorStatus.FAILED, Key.TOOLBOX_ERROR_ID_INVALID, processorParameters);
            return null;
        } else {
            User owner = null;
            ToolboxDao toolboxDao = new ToolboxDao(this.dbSession);
            Toolbox toolbox = null;
            if ( userId != 0 ) {
                UserDao userDao = new UserDao(this.dbSession);
                owner = userDao.get(userId);
            }
            RobotDao robotDao = new RobotDao(this.dbSession);
            Robot robot = robotDao.loadRobot(robotName);
            toolbox = toolboxDao.load(toolboxName, owner, robot);
            if ( toolbox == null ) {
                setStatus(ProcessorStatus.FAILED, Key.TOOLBOX_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.SUCCEEDED, Key.TOOLBOX_GET_ONE_SUCCESS, new HashMap<>());
            }
            return toolbox;
        }
    }
}
