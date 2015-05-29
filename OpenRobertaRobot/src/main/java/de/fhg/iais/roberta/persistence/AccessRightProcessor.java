package de.fhg.iais.roberta.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.AccessRightDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class AccessRightProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AccessRightProcessor.class);

    public AccessRightProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public void shareToUser(int ownerId, String userToShareName, String programName, String right) {
        ProgramDao programDao = new ProgramDao(this.dbSession);
        UserDao userDao = new UserDao(this.dbSession);

        User owner = userDao.get(ownerId);
        if ( owner == null ) {
            setError(Key.OWNER_DOES_NOT_EXIST);
        }
        Program programToShare = programDao.load(programName, owner);
        if ( programToShare == null ) {
            setError(Key.PROGRAM_TO_SHARE_DOES_NOT_EXIST);
        }
        User userToShare = userDao.loadUser(userToShareName);
        if ( userToShare == null ) {
            setError(Key.USER_TO_SHARE_DOES_NOT_EXIST);
        }
        if ( !isOk() ) {
            return;
        }

        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);
        if ( right.equals("NONE") ) {
            accessRightDao.deleteAccessRight(userToShare, programToShare);
            setSuccess(Key.ACCESS_RIGHT_DELETED);
        } else {
            try {
                Relation relation = Relation.valueOf(right);
                accessRightDao.persistAccessRight(userToShare, programToShare, relation);
                setSuccess(Key.ACCESS_RIGHT_CHANGED);
            } catch ( Exception e ) {
                LOG.error("invalid share request. Uid: " + ownerId + ", user to share: " + userToShareName + ", access right: " + right);
                setError(Key.SERVER_ERROR);
            }
        }
    }
}