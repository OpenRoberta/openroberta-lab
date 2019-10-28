package de.fhg.iais.roberta.util;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import de.fhg.iais.roberta.blockly.generated.Export;

public class XsdTest {
    @Test
    public void test1() throws Exception {
        Export jaxb = jaxb("/blockly.xsd", "n1.xml", Export.class);
        Assert.assertNotNull(jaxb.getConfig());
        Assert.assertNotNull(jaxb.getProgram());
    }

    @Test(expected = SAXParseException.class)
    public void test2() throws Exception {
        jaxb("/blockly.xsd", "n2.xml", Export.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T jaxb(String xsdResourceName, String xmlResourceName, Class<T> clazz) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try (InputStream resourceXsd = XsdTest.class.getResourceAsStream(xsdResourceName);
            InputStream resourceXml1 = XsdTest.class.getResourceAsStream(xmlResourceName);
            InputStream resourceXml2 = XsdTest.class.getResourceAsStream(xmlResourceName)) //
        {
            Schema schema = sf.newSchema(new StreamSource(resourceXsd));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(resourceXml1));

            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbUnmarshaller.setSchema(schema);
            return (T) jaxbUnmarshaller.unmarshal(new InputSource(resourceXml2));
        }
    }
}
