package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.Like;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.LikeDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;

public class LikeProcessor extends AbstractProcessor {
    public LikeProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public Like createLike(String programName, String robotName, String authorName) throws Exception {
        if ( this.httpSessionState.isUserLoggedIn() ) {
            ProgramDao programDao = new ProgramDao(this.dbSession);
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            LikeDao likeDao = new LikeDao(this.dbSession);

            User gallery = userDao.loadUser("Gallery");
            User author = userDao.loadUser(authorName);
            User userWhoLike = userDao.loadUser(this.httpSessionState.getUserId());

            Robot robot = robotDao.loadRobot(robotName);
            if ( robot == null ) {
                setError(Key.ROBOT_DOES_NOT_EXIST);
                return null;
            }
            Program program = programDao.load(programName, gallery, robot, author);
            if ( program == null ) {
                setError(Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
                return null;
            }
            Pair<Key, Like> result = likeDao.persistsLike(userWhoLike, program);

            // a bit strange, but necessary as Java has no N-tuple
            if ( result.getFirst() == Key.LIKE_SAVE_SUCCESS ) {
                setSuccess(Key.LIKE_SAVE_SUCCESS);
            } else {
                setError(result.getFirst());
            }
            return result.getSecond();
        } else {
            setError(Key.USER_ERROR_NOT_LOGGED_IN);
            return null;
        }
    }

    public Like loadLike(User user, Program program) throws Exception {
        LikeDao likeDao = new LikeDao(this.dbSession);
        Like like = likeDao.loadLike(user, program);
        if ( like != null ) {
            setSuccess(Key.LIKE_GET_ONE_SUCCESS);
            return like;
        } else {
            setError(Key.LIKE_GET_ONE_ERROR_NOT_FOUND);
            return null;
        }
    }

    public JSONArray getLikesPerProgram(Program program) {
        LikeDao likeDao = new LikeDao(this.dbSession);
        JSONArray likes = new JSONArray();
        List<Like> likesList = likeDao.loadLikesByProgram(program);
        for ( Like like : likesList ) {
            likes.put(like.getUser().getAccount());
        }
        setSuccess(Key.LIKE_GET_ALL_SUCCESS);
        return likes;
    }

    public int getNumberOfLikesPerProgram(Program program) {
        JSONArray likes = getLikesPerProgram(program);
        return likes.length();
    }

    public List<Like> getLikesPerUser(User user) {
        LikeDao likeDao = new LikeDao(this.dbSession);
        List<Like> likesList = likeDao.loadLikesByUser(user);
        return likesList;
    }

    public void deleteLike(String programName, String robotName, String authorName) throws Exception {
        ProgramDao programDao = new ProgramDao(this.dbSession);
        UserDao userDao = new UserDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        LikeDao likeDao = new LikeDao(this.dbSession);

        User gallery = userDao.loadUser("Gallery");
        User author = userDao.loadUser(authorName);
        User userWhoLike = userDao.loadUser(this.httpSessionState.getUserId());

        Robot robot = robotDao.loadRobot(robotName);
        if ( robot == null ) {
            setError(Key.ROBOT_DOES_NOT_EXIST);
            return;
        }
        Program program = programDao.load(programName, gallery, robot, author);
        if ( program == null ) {
            setError(Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
            return;
        }
        Like like = likeDao.loadLike(userWhoLike, program);
        if ( like != null ) {
            likeDao.deleteLike(like);
            setSuccess(Key.LIKE_DELETE_SUCCESS);
            return;
        }
    }
}