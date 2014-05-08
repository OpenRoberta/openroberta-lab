package de.fhg.iais.roberta.javaServer.jetty;

import java.io.IOException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.servlet.ServletContainer;

import de.fhg.iais.roberta.persistence.connector.SessionFactoryWrapper;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.ProjectDao;

public class ServerStarter {
    private static final Logger LOG = LoggerFactory.getLogger(ServerStarter.class);

    public static void main(String[] args) throws Exception {
        Server server = new ServerStarter().start(1999);
        server.join();
    }

    public Server start(int port) throws IOException {

        Server server = new Server(port);

        ServletContextHandler jerseyHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        jerseyHandler.setContextPath("/");
        ServletHolder servletHolder = new ServletHolder(new ServletContainer());
        servletHolder.setInitParameter(
            "com.sun.jersey.config.property.packages",
            "de.fhg.iais.roberta.javaServer.resources,de.fhg.iais.roberta.javaServer.provider");
        servletHolder.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");
        jerseyHandler.addServlet(servletHolder, "/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("staticResources");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {
            resourceHandler, jerseyHandler
        });
        server.setHandler(handlers);

        try {
            server.start();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        SessionWrapper session = SessionFactoryWrapper.getSession();
        ProjectDao projectDao = new ProjectDao(session);
        int numberOfProjects = projectDao.loadAll().size();
        LOG.info("There are " + numberOfProjects + " projects stored in the database");
        session.close();

        return server;
    }
}
