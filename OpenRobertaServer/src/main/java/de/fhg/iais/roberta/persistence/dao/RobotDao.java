package de.fhg.iais.roberta.persistence.dao;

import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

public class RobotDao extends AbstractDao<Robot> {
    /**
     * create a new DAO for robots. This creation is cheap.
     *
     * @param session the session used to access the database.
     */

    public RobotDao(DbSession session) {
        super(Robot.class, session);
    }

    public Robot loadRobot(String name) {
        Assert.notNull(name);
        Query hql = this.session.createQuery("from Robot where name=:name");
        hql.setString("name", name);

        @SuppressWarnings("unchecked")
        List<Robot> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        if ( il.size() == 0 ) {
            return null;
        } else {
            return il.get(0);
        }
    }
}