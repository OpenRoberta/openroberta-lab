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
     * create a new DAO for providers. This creation is cheap.
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
     * load an Provider-object persisted in the database, identified by its name (the "business" key of a provider)
     * 
     * @param name the name of the provider, never null
     * @return the Provider-object, never null
     */
    public Project load(String name) {
        Query hql = this.session.createQuery("from Project where name=:name");
        hql.setString("name", name);
        @SuppressWarnings("unchecked")
        List<Project> il = hql.list();
        Assert.isTrue(1 == il.size());
        return il.get(0);
    }

    /**
     * load all provider objects persisted in the database
     * 
     * @return the list of Provider-objects, may be an empty list, but never null
     */
    public List<Project> loadAll() {
        Query hql = this.session.createQuery("from Project");
        @SuppressWarnings("unchecked")
        List<Project> il = hql.list();
        return Collections.unmodifiableList(il);
    }
}
