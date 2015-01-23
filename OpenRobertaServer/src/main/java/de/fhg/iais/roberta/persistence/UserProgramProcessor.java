package de.fhg.iais.roberta.persistence;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserProgram;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserProgramDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Util;

public class UserProgramProcessor extends AbstractProcessor {

    public UserProgramProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    @Deprecated
    public void setRights(int ownerId, String programName, JSONArray usersJSONArray) {

        UserProgramDao userProgramDao = new UserProgramDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);

        //I am assuming that the user exists and that it has the correct values
        UserDao userDao = new UserDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Program program = programDao.load(programName, owner);

        for ( int i = 0; i < usersJSONArray.length(); i++ ) {
            try {
                JSONObject userToInvite = usersJSONArray.getJSONObject(i);
                int userId = userToInvite.getInt("id");
                String right = userToInvite.getString("right");
                UserDao userDaoN = new UserDao(this.dbSession);
                User user = userDaoN.get(userId);

                if ( user != null ) {
                    userProgramDao.persistUserProgram(user, program, right);
                }
            } catch ( JSONException e ) {
                // TODO: crash setError(Util.SYSTEM_ERROR);
            }
        }
    }

    @Deprecated
    public void shareToUser(int ownerId, String userToShareName, String programName, String right) {
        ProgramDao programDao = new ProgramDao(this.dbSession);
        UserDao userDao = new UserDao(this.dbSession);

        User owner = userDao.get(ownerId);
        if ( owner == null ) {
            setError(Util.OWNER_DOES_NOT_EXIST);
        }
        Program programToShare = programDao.load(programName, owner);
        if ( programToShare == null ) {
            setError(Util.PROGRAM_TO_SHARE_DOES_NOT_EXIST);
        }
        User userToShare = userDao.loadUser(userToShareName);
        if ( userToShare == null ) {
            setError(Util.USER_TO_SHARE_DOES_NOT_EXIST);
        }

        UserProgramDao userProgramDao = new UserProgramDao(this.dbSession);
        if ( right.equals("NONE") ) {
            int userProgram = userProgramDao.deleteUserProgram(userToShare, programToShare);
            setSuccess(Util.SERVER_ERROR);
        } else {
            UserProgram userProgram = userProgramDao.persistUserProgram(userToShare, programToShare, right);
            setSuccess(Util.SERVER_ERROR);
        }

    }

}
