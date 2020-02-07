package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Like;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;

public class LikeDao extends AbstractDao<Like> {
    /**
     * create a new DAO for Like. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public LikeDao(DbSession session) {
        super(Like.class, session);
    }

    /**
     * Create {@link Like} object and persist it
     *
     * @param user likes the program
     * @param program liked by the user
     * @return the created object if the creation is successful o/w <b>null</b>
     * @throws Exception
     */
    public Pair<Key, Like> persistsLike(User user, Program program) throws Exception {
        Assert.notNull(user);
        Assert.notNull(program);
        lockTable();
        Like like = loadLike(user, program);

        if ( like == null ) {
            like = new Like(user, program);
            this.session.save(like);
            return Pair.of(Key.LIKE_SAVE_SUCCESS, like);
        } else {
            return Pair.of(Key.LIKE_SAVE_ERROR_EXISTS, null);
        }
    }

    /**
     * Load like
     *
     * @param user
     * @return {@link Like} if it exists o/w <b>null</b>
     */
    public Like loadLike(User user, Program program) {
        Assert.notNull(user);
        Assert.notNull(program);
        Query hql = this.session.createQuery("from Like where user=:user and program=:program");
        hql.setEntity("user", user);
        hql.setEntity("program", program);

        @SuppressWarnings("unchecked")
        List<Like> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

    public int deleteLike(Like like) {
        Assert.notNull(like);
        this.session.delete(like);
        return 1;
    }

    public List<Like> loadLikesByProgram(Program program) {
        Assert.notNull(program);
        Query hql = this.session.createQuery("from Like where program=:program");
        hql.setEntity("program", program);

        @SuppressWarnings("unchecked")
        List<Like> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    public List<Like> loadLikesByUser(User user) {
        Assert.notNull(user);
        Query hql = this.session.createQuery("from Like where user=:user");
        hql.setEntity("ussss", user);

        @SuppressWarnings("unchecked")
        List<Like> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * create a write lock for the table USER_PROGRAM_LIKE to avoid deadlocks. This is a no op if concurrency control is not 2PL, but MVCC
     */
    private void lockTable() {
        this.session.createSqlQuery("lock table USER_PROGRAM_LIKE write").executeUpdate();
        this.session.addToLog("lock", "is now aquired");
    }

}
