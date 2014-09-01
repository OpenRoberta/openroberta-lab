package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.UserProgram;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.UserProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;

public class UserProgramProcessor{
	
    public UserProgram getUserProgram(SessionWrapper session, int userId, String userProgramName) {

        UserProgramDao userProgramDao = new UserProgramDao(session);
        UserDao userDao = new UserDao(session);
        User user = userDao.load(userId);
        UserProgram userProgram = userProgramDao.loadUserProgram(user, userProgramName);
        return userProgram;
    }

    public UserProgram updateUserProgram(SessionWrapper session, int userId, String userProgramName, String userProgramText) {

        UserProgramDao userProgramDao = new UserProgramDao(session);
        UserDao userDao = new UserDao(session);
        User user = userDao.load(userId);
        UserProgram userProgram = userProgramDao.persistUserProgramText(user, userProgramName, userProgramText);
        return userProgram;
    }

    public List<String> getUserProgramNames(SessionWrapper session, User userId) {
        UserProgramDao userProgramDao = new UserProgramDao(session);
        List<UserProgram> userPrograms = userProgramDao.loadAllUserPrograms(userId);
        List<String> userProgramNames = new ArrayList<>();
        for ( UserProgram userProgram : userPrograms ) {
            userProgramNames.add(userProgram.getProgramName());
        }
        return userProgramNames;
    }

    public int deleteByName(SessionWrapper session, int userId, String userProgramName) {
        UserProgramDao userProgramDao = new UserProgramDao(session);
        return userProgramDao.deleteUserProgramByName(userId, userProgramName);
    }

}