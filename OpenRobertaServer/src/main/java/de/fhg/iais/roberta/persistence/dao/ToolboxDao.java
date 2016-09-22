package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.Toolbox;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * DAO class to load and store toolbox objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author rbudde
 */
public class ToolboxDao extends AbstractDao<Toolbox> {
    /**
     * create a new DAO for toolboxs. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public ToolboxDao(DbSession session) {
        super(Toolbox.class, session);
    }

    /**
     * make a toolbox object and persist it (if the toolbox, identified by owner&name, does not exist) or update it (if the toolbox exists)
     *
     * @param name the name of the toolbox, never null
     * @param owner the user who owns the toolbox, never null
     * @param toolboxText the toolbox text, maybe null
     * @return the persisted toolbox object
     */
    public boolean persistToolboxText(String name, User owner, Robot robot, String toolboxText, boolean mayExist) {
        Assert.notNull(name);
        Assert.notNull(owner);
        Toolbox toolbox = load(name, owner, robot);
        if ( toolbox == null ) {
            toolbox = new Toolbox(name, owner);
            toolbox.setToolboxText(toolboxText);
            this.session.save(toolbox);
            return true;
        } else if ( mayExist ) {
            toolbox.setToolboxText(toolboxText);
            return true;
        } else {
            return false;
        }
    }

    /**
     * load a toolbox from the database, identified by its owner and its name (both make up the "business" key of a toolbox)
     *
     * @param toolboxName the name of the toolbox, never null
     * @param user the user if he is logged in, if not null
     * @return the toolbox, null if the toolbox is not found
     */
    public Toolbox load(String name, User user, Robot robot) {
        Assert.notNull(name);
        Assert.notNull(robot);
        Query hql;
        if ( user != null ) {
            //TODO What happens, if a user uses the same name for a toolbox than one of our standard names?
            hql = this.session.createQuery("from Toolbox where name=:name and (owner is null or owner=:owner) and robot=:robot");
            hql.setString("name", name);
            hql.setEntity("owner", user);
            hql.setEntity("robot", robot);
        } else {
            hql = this.session.createQuery("from Toolbox where name=:name and owner is null and robot=:robot");
            hql.setString("name", name);
            hql.setEntity("robot", robot);
        }
        @SuppressWarnings("unchecked")
        List<Toolbox> il = hql.list();
        // System.out.println(il.get(0));
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    public int deleteByName(String name, User owner, Robot robot) {
        Toolbox toBeDeleted = load(name, owner, robot);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    /**
     * load all toolboxs persisted in the database which are owned by a user given
     *
     * @return the list of all toolboxs, may be an empty list, but never null
     */
    public List<Toolbox> loadAll(User owner) {
        Query hql = this.session.createQuery("from Toolbox where owner=:owner");
        hql.setEntity("owner", owner);
        @SuppressWarnings("unchecked")
        List<Toolbox> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * load all Toolboxs persisted in the database
     *
     * @return the list of all toolboxs, may be an empty list, but never null
     */
    public List<Toolbox> loadAll() {
        Query hql = this.session.createQuery("from Toolbox");
        @SuppressWarnings("unchecked")
        List<Toolbox> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}
