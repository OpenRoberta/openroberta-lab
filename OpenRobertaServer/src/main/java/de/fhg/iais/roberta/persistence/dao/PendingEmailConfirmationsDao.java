package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.PendingEmailConfirmations;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class PendingEmailConfirmationsDao extends AbstractDao<PendingEmailConfirmations> {
    /**
     * create a new DAO for e-mail confirmation. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public PendingEmailConfirmationsDao(DbSession session) {
        super(PendingEmailConfirmations.class, session);
    }

    /**
     * Create {@link PendingEmailConfirmationsDao} object and persist it
     *
     * @param userId of the user asking for url
     * @return the created object if the creation is successful o/w <b>null</b>
     * @throws Exception
     */
    public PendingEmailConfirmations persistConfirmation(int userId) throws Exception {
        Assert.notNull(userId);
        PendingEmailConfirmations confirmation = new PendingEmailConfirmations(userId);
        this.session.save(confirmation);
        return confirmation;
    }

    public int deleteConfirmation(PendingEmailConfirmations confirmation) {
        Assert.notNull(confirmation);
        this.session.delete(confirmation);
        return 1;
    }

    /**
     * Load url postfix for a user
     *
     * @param userId
     * @return {@link PendingEmailConfirmations} if it exists o/w <b>null</b>
     */
    public PendingEmailConfirmations loadConfirmationByUser(int userId) {
        Assert.notNull(userId);
        Query hql = this.session.createQuery("from PendingEmailConfirmations where userID=:userId");
        hql.setInteger("userId", userId);

        @SuppressWarnings("unchecked")
        List<PendingEmailConfirmations> il = hql.list();
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
     * @return {@link PendingEmailConfirmations} if it exists o/w <b>null</b>
     */
    public PendingEmailConfirmations loadConfirmationByUrl(String urlPostfix) {
        Assert.notNull(urlPostfix);
        Query hql = this.session.createQuery("from PendingEmailConfirmations where urlPostfix=:urlPostfix");
        hql.setString("urlPostfix", urlPostfix);

        @SuppressWarnings("unchecked")
        List<PendingEmailConfirmations> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }

}
