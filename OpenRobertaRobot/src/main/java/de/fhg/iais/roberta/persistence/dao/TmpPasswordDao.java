package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.TmpPassword;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class TmpPasswordDao extends AbstractDao<TmpPassword> {
    /**
     * create a new DAO for TmpPassword. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public TmpPasswordDao(DbSession session) {
        super(TmpPassword.class, session);
    }

    /**
     * Create {@link TmpPassword} object and persist it
     *
     * @param userId of the user asking for temporary password
     * @return the created object if the creation is successful o/w <b>null</b>
     * @throws Exception
     */
    public TmpPassword persistTmpPassword(int userId) throws Exception {
        Assert.notNull(userId);
        TmpPassword tmpPassword = loadTmpPassword(userId);
        if ( tmpPassword == null ) {
            tmpPassword = new TmpPassword(userId);
            this.session.save(tmpPassword);
            return tmpPassword;
        } else {
            return null;
        }
    }

    /**
     * Load temporary password for a user
     *
     * @param userId
     * @return {@link TmpPassword} if it exists o/w <b>null</b>
     */
    public TmpPassword loadTmpPassword(int userId) {
        Assert.notNull(userId);
        Query hql = this.session.createQuery("from TmpPassword where userID=:userId");
        hql.setInteger("userId", userId);

        @SuppressWarnings("unchecked")
        List<TmpPassword> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

    public int deleteTmpPassword(TmpPassword tmpPassword) {
        Assert.notNull(tmpPassword);
        this.session.delete(tmpPassword);
        return 1;
    }

}
