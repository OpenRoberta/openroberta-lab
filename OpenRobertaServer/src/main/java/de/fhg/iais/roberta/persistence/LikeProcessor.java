package de.fhg.iais.roberta.persistence;

import java.util.HashMap;

import de.fhg.iais.roberta.persistence.bo.Like;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.LikeDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.basic.Pair;

public class LikeProcessor extends AbstractProcessor {
    public LikeProcessor(DbSession dbSession, int userId) {
        super(dbSession, userId);
    }

    public Like createLike(String programName, String robotName, String authorName) throws Exception {
        if ( isUserLoggedIn() ) {
            ProgramDao programDao = new ProgramDao(this.dbSession);
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            LikeDao likeDao = new LikeDao(this.dbSession);

            User gallery = userDao.loadUser(null, "Gallery");
            User author = userDao.loadUser(null, authorName);
            User userWhoLikes = userDao.loadUser(getIdOfLoggedInUser());

            Robot robot = robotDao.loadRobot(robotName);
            if ( robot == null ) {
                setStatus(ProcessorStatus.FAILED, Key.ROBOT_DOES_NOT_EXIST, new HashMap<>());
                return null;
            }
            Program program = programDao.load(programName, gallery, robot, author);
            if ( program == null ) {
                setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
                return null;
            }
            Pair<Key, Like> result = likeDao.persistsLike(userWhoLikes, program);

            // a bit strange, but necessary as Java has no N-tuple
            if ( result.getFirst() == Key.LIKE_SAVE_SUCCESS ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.LIKE_SAVE_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, result.getFirst(), new HashMap<>());
            }
            return result.getSecond();
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_NOT_LOGGED_IN, new HashMap<>());
            return null;
        }
    }

    public void deleteLike(String programName, String robotName, String authorName) throws Exception {
        ProgramDao programDao = new ProgramDao(this.dbSession);
        UserDao userDao = new UserDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        LikeDao likeDao = new LikeDao(this.dbSession);

        User gallery = userDao.loadUser(null, "Gallery");
        User author = userDao.loadUser(null, authorName);
        User userWhoLike = userDao.loadUser(getIdOfLoggedInUser());

        Robot robot = robotDao.loadRobot(robotName);
        if ( robot == null ) {
            setStatus(ProcessorStatus.FAILED, Key.ROBOT_DOES_NOT_EXIST, new HashMap<>());
            return;
        }
        Program program = programDao.load(programName, gallery, robot, author);
        if ( program == null ) {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
            return;
        }
        Like like = likeDao.loadLike(userWhoLike, program);
        if ( like != null ) {
            likeDao.deleteLike(like);
            setStatus(ProcessorStatus.SUCCEEDED, Key.LIKE_DELETE_SUCCESS, new HashMap<>());
            return;
        }
    }
}