package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Project;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;

/**
 * DAO class to load and store programs objects.A DAO object is always bound to a session. This session defines the transactional context, in which the database
 * access takes place.
 * 
 * @author rbudde
 */
public class ProgramDao extends AbstractDao<Program> {
    /**
     * create a new DAO for ingests. This creation is cheap.
     * 
     * @param session the session used to access the database.
     */
    public ProgramDao(SessionWrapper session) {
        super(Program.class, session);
    }

    /**
     * make an program object and persist it (if the program, identified by project&name, does not exist) or update it (if the program exists)
     * 
     * @param project the project the program belongs to, never null
     * @param name the name of the program, never null
     * @param programText the program text, maybe null
     * @return the persisted program object
     */
    public Program persistProgramText(Project project, String name, String programText) {
        Assert.notNull(project);
        Assert.notNull(name);
        Program program = load(project, name);
        if ( program == null ) {
            program = new Program(name, project, "-", programText);
            this.session.save(program);
        }
        program.setProgramText(programText);
        return program;
    }

    /**
     * load an Ingest-object from the database, identified by the name of its provider and its name (both make up the "business" key of an ingest)
     * 
     * @param providerName the name of the provider, never null
     * @param name the name of the ingest, never null
     * @return the ingest object, never null
     */
    public Program load(Project project, String name) {
        Query hql = this.session.createQuery("from Program where project=:project and name=:name");
        hql.setEntity("project", project);
        hql.setString("name", name);
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * load all ingest-objects persisted in the database
     * 
     * @return the list of all ingest objects, may be an empty list, but never null
     */
    public List<Program> loadAll() {
        Query hql = this.session.createQuery("from Program");
        @SuppressWarnings("unchecked")
        List<Program> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}
