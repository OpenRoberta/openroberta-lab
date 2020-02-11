package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.LostPassword;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class LostPasswordDao extends AbstractDao<LostPassword> {
    /**
     * create a new DAO for LostPassword. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public LostPasswordDao(DbSession session) {
        super(LostPassword.class, session);
    }

    /**
     * Create {@link LostPassword} object and persist it
     *
     * @param userId of the user asking for url
     * @return the created object if the creation is successful o/w <b>null</b>
     * @throws Exception
     */
    public LostPassword persistLostPassword(int userId) throws Exception {
        Assert.notNull(userId);
        LostPassword lostPassword = new LostPassword(userId);
        this.session.save(lostPassword);
        return lostPassword;
    }

    public int deleteLostPassword(LostPassword lostPassword) {
        Assert.notNull(lostPassword);
        this.session.delete(lostPassword);
        return 1;
    }

    /**
     * Load url postfix for a user
     *
     * @param userId
     * @return {@link LostPassword} if it exists o/w <b>null</b>
     */
    public LostPassword loadLostPassword(int userId) {
        Assert.notNull(userId);
        Query hql = this.session.createQuery("from LostPassword where userID=:userId");
        hql.setInteger("userId", userId);

        @SuppressWarnings("unchecked")
        List<LostPassword> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

    /**
     * Load url postfix for a user
     *
     * @param userId
     * @return {@link LostPassword} if it exists o/w <b>null</b>
     */
    public LostPassword loadLostPassword(String urlPostfix) {
        Assert.notNull(urlPostfix);
        Query hql = this.session.createQuery("from LostPassword where urlPostfix=:urlPostfix");
        hql.setString("urlPostfix", urlPostfix);

        @SuppressWarnings("unchecked")
        List<LostPassword> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

}
