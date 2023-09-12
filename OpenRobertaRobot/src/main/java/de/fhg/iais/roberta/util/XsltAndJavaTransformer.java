package de.fhg.iais.roberta.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.common.base.Charsets;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.worker.IWorker;

public final class XsltAndJavaTransformer {
    private static final Pattern NAMESPACE_CHECK = Pattern.compile("<block_set", Pattern.LITERAL);
    private static final String HIGHEST_XML_VERSION_NUMBER = "4.0"; // if this version is detected, all transformations have been done already
    private static final String HIGHEST_XML_VERSION = "xmlversion=\"" + HIGHEST_XML_VERSION_NUMBER + "\"";

    private static final TransformerFactory factory = TransformerFactory.newInstance();
    private static final List<String> TRANSFORMER_SOURCES =
        Arrays.asList(Util.readResourceContent("/mappingBlockNames.xslt"), Util.readResourceContent("/mappingFieldsMutations.xslt"), Util.readResourceContent("/expansion.xslt"));

    private final ArrayList<Transformer> transformers = new ArrayList<>();

    public XsltAndJavaTransformer() {
        for ( String transformerSource : TRANSFORMER_SOURCES ) {
            try {
                InputStream inputStream = new ByteArrayInputStream(transformerSource.getBytes());
                Source source = new StreamSource(inputStream);
                final Transformer transformer = factory.newTransformer(source);
                this.transformers.add(transformer);
            } catch ( Exception e ) {
                throw new DbcException("Could not create XSLT transformer!", e);
            }
        }
    }

    /**
     * applies all transformations to the given program and configuration xml representation.<br>
     * - first: applies all xslt transformations<br>
     * - then: applies the Java transformations (if the "transform" workflow exists, currently mbed systems)
     * If the xmlversion is the highest one (currently "4.0"), it is assumed, that all transformations are already executed, and nothing is done.
     * <b>This may cause problems</b>
     *
     * @param factory the robot factory matching the robot as declared in program and configuration
     * @param programText the program xml
     * @param configText the configuration xml
     * @return a pair of (transformed program xml, transformed configuration xml), <b>never null, but the second member, the configuration, maybe null</b>
     */
    public Pair<String, String> transform(RobotFactory factory, String programText, String configText) {
        if ( programText.contains(HIGHEST_XML_VERSION) ) {
            return Pair.of(programText, configText);
        }
        boolean defaultConfig;
        String xmlVersion;
        try {
            if ( configText != null ) {
                defaultConfig = false;
                configText = transformXslt(configText);
            } else {
                defaultConfig = true;
                configText = factory.getConfigurationDefault();
            }
            programText = transformXslt(programText);
            BlockSet jaxbProgram = JaxbHelper.xml2Element(programText, BlockSet.class);
            xmlVersion = jaxbProgram.getXmlversion();
            Assert.notNull(jaxbProgram, "jaxb returns null for the program");
            checkAndAddNnDataForPlugin(factory.hasNN(), jaxbProgram);
            // jaxbProgram.setXmlversion("4.0"); // this is a bit too early, may cause problems, but later it is much more expensive
            programText = JaxbHelper.blockSet2xml(jaxbProgram);
        } catch ( Exception e ) {
            throw new DbcException("xslt transformation failed", e);
        }
        try {
            Pair<String, String> progConfPair = transformBetweenVersions(factory, xmlVersion, programText, configText);
            programText = progConfPair == null ? programText : progConfPair.getFirst();
            configText = progConfPair == null ? configText : progConfPair.getSecond();
            configText = defaultConfig ? null : configText;
            return Pair.of(programText, configText);
        } catch ( Exception e ) {
            throw new DbcException("Java transformation failed", e);
        }
    }

    /**
     * applies all xslt transformations to the given xml representation of a blockly program.<br>
     * <b>called by 'transform' implicitly. Explicit call ONLY in tests.</b>
     *
     * @param xml
     * @return
     */
    public String transformXslt(String xml) {
        // Sometimes the namespace is missing, it needs to be appended, otherwise the XSLT does not detect anything
        if ( !xml.contains("xmlns=\"http://de.fhg.iais.roberta.blockly\"") ) {
            xml = NAMESPACE_CHECK.matcher(xml).replaceAll(Matcher.quoteReplacement("<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" "));
        }
        String output = xml;
        for ( Transformer transformer : this.transformers ) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                StreamSource input = new StreamSource(new ByteArrayInputStream(output.getBytes(Charsets.UTF_8)));
                transformer.transform(input, new StreamResult(outputStream));
                input.getInputStream().close();
                output = outputStream.toString(Charsets.UTF_8);
            } catch ( Exception e ) {
                throw new DbcException("Could not transform program or configuration!", e);
            }
        }
        return output;
    }

    private void checkAndAddNnDataForPlugin(boolean hasNN, BlockSet jaxbImportExport) {
        if ( hasNN ) {
            for ( Instance instance : jaxbImportExport.getInstance() ) {
                if ( instance.getBlock().get(0).getType().contains("robControls_start") ) {
                    Block startBlock = instance.getBlock().get(0);
                    if ( startBlock.getData() == null ) {
                        Data data = new Data();
                        data.setValue(ServerProperties.DEFAULT_NEURAL_NETWORK);
                        startBlock.setData(data);
                    }
                    return;
                }
            }
        }
    }

    /**
     * transform programs with very old xml versions to new xml versions for plugins with a "transform" workflow. Currently this are "mbed" systems
     * (either Calliope* or Microbit*). Very old versions are those, which are neither "3.1" nor "4.*"
     */
    private static Pair<String, String> transformBetweenVersions(RobotFactory robotFactory, String xmlVersion, String programText, String configText) {
        if ( robotFactory.hasWorkflow("transform") && !xmlVersion.startsWith("4.") ) {
            AliveData.transformerExecutedTransformations.incrementAndGet();
            Assert.isTrue(robotFactory.getConfigurationType().equals("new"));
            Project project = new Project.Builder()
                .setFactory(robotFactory)
                .setProgramXml(programText)
                .setConfigurationXml(configText == null ? robotFactory.getConfigurationDefault() : configText)
                .build();
            executeTransformWorkflow(project);
            return Pair.of(project.getAnnotatedProgramAsXml(), configText == null ? null : project.getAnnotatedConfigurationAsXml());
        } else {
            return null;
        }
    }

    public static void executeTransformWorkflow(Project project) {
        List<IWorker> workflowPipe = project.getRobotFactory().getWorkerPipe("transform");
        if ( project.hasSucceeded() ) {
            for ( IWorker worker : workflowPipe ) {
                worker.execute(project);
                if ( !project.hasSucceeded() ) {
                    break;
                }
            }
        }
    }
}
