package de.fhg.iais.roberta.persistence;

import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.UserDao;

public class UserProcessor {

    public User getUser(SessionWrapper session, String account, String password) {
        UserDao userDao = new UserDao(session);
        User user = userDao.loadUser(account);
        if ( user.isPasswordCorrect(password) ) {
            return user;
        } else {
            return null;
        }
    }

    public User saveUser(SessionWrapper session, String account, String password, String roleAsString, String email, String tags) {
        UserDao userDao = new UserDao(session);
        if ( userDao.loadUser(account) == null ) {
            User user = userDao.persistUser(account, password, roleAsString);
            if ( user == null ) {
                return null;
            } else {
                if ( email != null ) {
                    user.setEmail(email);
                }
                if ( tags != null ) {
                    user.setTags(tags);
                }
            }
            return user;
        } else {
            return null;
        }
    }

    public User checkUser(SessionWrapper session, String account) {
        UserDao userDao = new UserDao(session);
        return userDao.loadUser(account);
    }

    public int deleteUserByAccount(SessionWrapper session, int userIdOfUserLoggedIn, String account) {
        if ( userIdOfUserLoggedIn <= 1 ) {
            return 0;
        } else {
            UserDao userDao = new UserDao(session);
            User user = userDao.loadUser(account);
            if ( user == null || user.getId() != userIdOfUserLoggedIn ) {
                return 0;
            } else {
                return userDao.deleteUser(user);
            }
        }
    }

}
