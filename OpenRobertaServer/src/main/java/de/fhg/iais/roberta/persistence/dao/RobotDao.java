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

    public List<Robot> loadRobotList(String sortBy, int offset, String tagFilter) {

        if ( tagFilter == null ) {
            Query hql = this.session.createQuery("from Robot where order by " + sortBy);
            hql.setFirstResult(offset);
            hql.setMaxResults(10);

            @SuppressWarnings("unchecked")
            List<Robot> il = hql.list();
            if ( il.size() == 0 ) {
                return null;
            } else {
                return il;
            }
        } else {

            //    		Query hql = this.session.createQuery("from Robot");
            Query hql = this.session.createQuery("from Robot where tags=:tag order by " + sortBy);
            //
            //    		("SELECT Name, Day FROM Customers LEFT JOIN Reservations "
            //    				+ "ON Customers.CustomerId = Reservations.CustomerId where relation.program =: program;");

            //    		Query hql = this.session.createQuery("select u.id, u.account, up.relation "
            //    				+ "from Robot as u "
            //    				+ "left join u.relation RobotProgram "
            //    				+ "where up.program =: program order by "+sortBy);
            //

            //            List<Object[]> list = query.list();
            //            for(Object[] arr : list){
            //                System.out.println(Arrays.toString(arr));
            //            }

            hql.setFirstResult(offset);
            hql.setMaxResults(10);
            hql.setString("tag", tagFilter);
            @SuppressWarnings("unchecked")
            List<Robot> il = hql.list();
            if ( il.size() == 0 ) {
                return null;
            } else {
                return il;
            }
        }

    }

    public int deleteRobot(Robot robotToBeDeleted) {
        Assert.notNull(robotToBeDeleted);
        this.session.delete(robotToBeDeleted);
        return 1;
    }
}