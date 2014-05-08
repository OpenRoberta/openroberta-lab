package de.fhg.iais.roberta.javaServer.provider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Project;

public class AscJAXBProviderFactory {
    private static final Logger LOG = LoggerFactory.getLogger(AscJAXBProviderFactory.class);

    private static final AscJAXBProviderFactory instance = new AscJAXBProviderFactory();

    private JAXBContext context = null;
    private AscNamespaceMapper nsMapper = null;

    private AscJAXBProviderFactory() {
        try {
            this.context = JAXBContext.newInstance("de.budde.generated.xml.asc:");
            this.nsMapper = new AscNamespaceMapper();
        } catch ( JAXBException e ) {
            LOG.error("JAXBContext could not be build ...", e);
        }
    }

    public static AscJAXBProviderFactory getInstance() {
        return instance;
    }

    public JAXBContext getJAXBContext(Class<?> type) {
        if ( type == Project.class ) {
            return this.context;
        } else {
            LOG.error("context not valid for type " + type.getName());
            return null;
        }
    }

    public Marshaller getMarshaller(Class<?> type) {
        if ( type == Project.class ) {
            try {
                Marshaller marshaller = this.context.createMarshaller();
                marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", this.nsMapper);
                return marshaller;
            } catch ( JAXBException e ) {
                LOG.error("marshaller not build", e);
                return null;
            }
        } else {
            LOG.error("marshaller not valid for type " + type.getName());
            return null;
        }
    }

    public Unmarshaller getUnmarshaller(Class<?> type) {
        if ( type == Project.class ) {
            try {
                Unmarshaller unmarshaller = this.context.createUnmarshaller();
                return unmarshaller;
            } catch ( JAXBException e ) {
                LOG.error("unmarshaller not build", e);
                return null;
            }
        } else {
            LOG.error("unmarshaller not valid for type " + type.getName());
            return null;
        }

    }
}