package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.Toolbox;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ToolboxDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class ToolboxProcessor extends AbstractProcessor {
    public ToolboxProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public Toolbox getToolbox(String toolboxName, int userId) {
        if ( !Util.isValidJavaIdentifier(toolboxName) ) {
            setError(Key.TOOLBOX_ERROR_ID_INVALID, toolboxName);
            return null;
        } else {
            User owner = null;
            ToolboxDao toolboxDao = new ToolboxDao(this.dbSession);
            Toolbox toolbox = null;
            if ( userId != 0 ) {
                UserDao userDao = new UserDao(this.dbSession);
                owner = userDao.get(userId);
            }
            toolbox = toolboxDao.load(toolboxName, owner);
            if ( toolbox == null ) {
                System.out.println("null");
                setError(Key.TOOLBOX_GET_ONE_ERROR_NOT_FOUND);
            } else {
                System.out.println("S");
                setSuccess(Key.TOOLBOX_GET_ONE_SUCCESS);
            }
            return toolbox;
        }
    }
}
