package de.fhg.iais.roberta.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.common.base.Charsets;

import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public final class XsltTransformer {
    private static final Pattern NAMESPACE_CHECK = Pattern.compile("<block_set", Pattern.LITERAL);

    private static final List<String> TRANSFORMER_SOURCES = Arrays.asList("classpath:/mapping.xslt", "classpath:/expansion.xslt");

    private final ArrayList<Transformer> transformers = new ArrayList<>();

    @Inject
    public XsltTransformer() {
        for ( String transformerSource : TRANSFORMER_SOURCES ) {
            try (InputStream stream = Util.getInputStream(false, transformerSource)) {
                TransformerFactory factory = TransformerFactory.newInstance();

                Source source = new StreamSource(stream);
                this.transformers.add(factory.newTransformer(source));
            } catch ( TransformerConfigurationException | IOException e ) {
                throw new DbcException("Could not create XSLT transformer!", e);
            }
        }
    }

    public String transform(String xml) {
        // Sometimes the namespace is missing, it needs to be appended, otherwise the XSLT does not detect anything
        if ( !xml.contains("xmlns=\"http://de.fhg.iais.roberta.blockly\"") ) {
            xml = NAMESPACE_CHECK.matcher(xml).replaceAll(Matcher.quoteReplacement("<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" "));
        }
        Source input = new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8)));
        String output = null;
        for ( Transformer transformer : this.transformers ) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                transformer.transform(input, new StreamResult(outputStream));
                output = outputStream.toString(Charsets.UTF_8);
                input = new StreamSource(new ByteArrayInputStream(output.getBytes(Charsets.UTF_8)));
            } catch ( TransformerException | IOException e ) {
                throw new DbcException("Could not transform program or configuration!", e);
            }
        }
        Assert.notNull(output, "Something went wrong while transforming program!");
        return output;
    }
}
