package de.fhg.iais.roberta.javaServer.provider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;

public class JAXBProviderFactory {
    private static final Logger LOG = LoggerFactory.getLogger(JAXBProviderFactory.class);

    private static final JAXBProviderFactory instance = new JAXBProviderFactory();

    private JAXBContext context = null;
    private RobertaNamespaceMapper nsMapper = null;

    private JAXBProviderFactory() {
        try {
            this.context = JAXBContext.newInstance("de.fhg.iais.roberta.blockly.generated:");
            this.nsMapper = new RobertaNamespaceMapper();
        } catch ( JAXBException e ) {
            LOG.error("JAXBContext could not be build ...", e);
        }
    }

    public static JAXBProviderFactory getInstance() {
        return instance;
    }

    public JAXBContext getJAXBContext(Class<?> type) {
        if ( type == BlockSet.class ) {
            return this.context;
        } else {
            LOG.error("context not valid for type " + type.getName());
            return null;
        }
    }

    public Marshaller getMarshaller(Class<?> type) {
        if ( type == BlockSet.class ) {
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
        if ( type == BlockSet.class ) {
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