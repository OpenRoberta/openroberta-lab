package de.fhg.iais.roberta.persistence.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Query;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.Project;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;

/**
 * DAO class to load and store project objects. A DAO object is always bound to a session. This session defines the transactional context, in which the
 * database access takes place.
 * 
 * @author rbudde
 */
public class ProjectDao extends AbstractDao<Project> {
    /**
     * create a new DAO for projectss. This creation is cheap.
     * 
     * @param session the session used to access the database.
     */
    public ProjectDao(SessionWrapper session) {
        super(Project.class, session);
    }

    /**
     * make a project object and persist it
     * 
     * @param name the name of the project, never null
     * @param email the email of this project, may be null
     * @param url the url of this project, may be null
     * @param text the text desc4ribing this project, may be null
     * @return the persisted project object
     */
    public Project makeAndPersist(String name, String email, String url, String text) {
        Project project = new Project(name, email, url, text);
        this.session.save(project);
        return project;
    }

    /**
     * load an project persisted in the database, identified by its name (the "business" key of a project)
     * 
     * @param name the name of the project, never null
     * @return the project, null if the program is not found
     */
    public Project load(String projectName) {
        Query hql = this.session.createQuery("from Project where name=:name");
        hql.setString("name", projectName);
        @SuppressWarnings("unchecked")
        List<Project> il = hql.list();
        Assert.isTrue(il.size() <= 1);
        return il.size() == 0 ? null : il.get(0);
    }

    /**
     * load all projects persisted in the database
     * 
     * @return the list of projects, may be an empty list, but never null
     */
    public List<Project> loadAll() {
        Query hql = this.session.createQuery("from Project");
        @SuppressWarnings("unchecked")
        List<Project> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}