package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;

/**
 * DAO class to load and store programs objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 *
 * @author rbudde
 */
public class ProgramDao extends AbstractDao<Program> {
    /**
     * create a new DAO for programs. This creation is cheap.
     *
     * @param session the session used to access the database.
     */
    public ProgramDao(DbSession session) {
        super(Program.class, session);
    }

    /**
     * make a program object and persist it (if the program, identified by owner&name, does not exist) or update it (if the program exists)
     *
     * @param name the name of the program, never null
     * @param owner the user who owns the program, never null
     * @param programText the program text, maybe null
     * @return true if the program could be persisted successfully
     */
    public boolean persistProgramText(String name, User owner, String programText, boolean overwriteAllowed) {
        Assert.notNull(name);
        Assert.notNull(owner);
        Program program = load(name, owner);
        if ( program == null ) {
            program = new Program(name, owner);
            program.setProgramText(programText);
            this.session.save(program);
            return true;
        } else if ( overwriteAllowed ) {
            program.setProgramText(programText);
            return true;
        } else {
            return false;
        }
    }

    /**
     * load a program from the database, identified by its owner and its name (both make up the "business" key of a program)
     *
     * @param name the name of the program, never null
     * @param owner user who owns the program, never null
     * @return the program, null if the program is not found
     */
    public Program load(String name, User owner) {
        Assert.notNull(name);
        Assert.notNull(owner);
        Query hql = this.session.createQuery("from Program where name=:name and owner=:owner");
        hql.setString("name", name);
        hql.setEntity("owner", owner);
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    public int deleteByName(String name, User owner) {
        Program toBeDeleted = load(name, owner);
        if ( toBeDeleted == null ) {
            return 0;
        } else {
            this.session.delete(toBeDeleted);
            return 1;
        }
    }

    /**
     * load all programs persisted in the database which are owned by a user given
     *
     * @return the list of all programs, may be an empty list, but never null
     */
    public List<Program> loadAll(User owner) {
        Query hql = this.session.createQuery("from Program where owner=:owner");
        hql.setEntity("owner", owner);
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        return Collections.unmodifiableList(il);
    }

    /**
     * load all programs persisted in the database
     *
     * @return the list of all programs, may be an empty list, but never null
     */
    public List<Program> loadAll() {
        Query hql = this.session.createQuery("from Program");
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}
