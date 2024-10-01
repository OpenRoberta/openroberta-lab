package de.fhg.iais.roberta.worker;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractRegenerateTextlyJavaVisitor;

public class RegenerateNepoWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(RegenerateNepoWorker.class);

    public boolean mustRunEvenIfPreviousWorkerFailed() {
        return true;
    }

    @Override
    public void execute(Project project) {
        try {
            ProgramAst programAst = project.getProgramAst();

            String programXML = jaxbToXml(astToJaxb(programAst));
            project.setProgramAsBlocklyXML(programXML);

            AbstractRegenerateTextlyJavaVisitor textlyVisitor = getVisitorForTextlyJava(project);
            if ( textlyVisitor == null ) {
                project.setProgramAsTextly("- no textly -");
            } else {

                String oldSource = project.getSourceCodeBuilder().toString();
                StringBuilder oldIndentBuilder = project.getIndentationBuilder();
                StringBuilder textlyBuilder = new StringBuilder();
                StringBuilder indentBuilder = new StringBuilder();
                textlyVisitor.setStringBuilders(textlyBuilder, indentBuilder);
                textlyVisitor.generateCode(true);

                project.setProgramAsTextly(textlyBuilder.toString());
                project.setSourceCode(oldSource);
                project.setIndentationBuilder(oldIndentBuilder);
            }

            ConfigurationAst configurationAst = project.getConfigurationAst();
            String configurationXML = jaxbToXml(astToJaxb(configurationAst));
            project.setConfigurationAsBlocklyXML(configurationXML);

            // this worker executes independent of success or failure of preceding workers.
            // thus, this worker should NEVER change the result. The result should stay as it is :-)
        } catch ( Throwable e ) {
            LOG.error("program & configuration ast -> blockly failed", e);
            project.setResult(Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED);
        }
    }

    protected AbstractRegenerateTextlyJavaVisitor getVisitorForTextlyJava(Project project) {
        // TODO: refactor. Each plugin must support NEPO regeneration. Make class and method abstract
        return null;
    }

    private static String jaxbToXml(BlockSet blockSet) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(blockSet, writer);
        return writer.toString();
    }

    private static BlockSet astToJaxb(ProgramAst program) {
        Assert.notNull(program);
        List<List<Phrase>> astProgram = program.getTree();
        BlockSet blockSet = new BlockSet();
        blockSet.setDescription(program.getDescription());
        blockSet.setRobottype(program.getRobotType());
        blockSet.setTags(program.getTags());
        blockSet.setXmlversion(program.getXmlVersion());

        for ( List<Phrase> tree : astProgram ) {
            Instance instance = new Instance();
            blockSet.getInstance().add(instance);
            for ( Phrase phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    instance.setX(((Location) phrase).x);
                    instance.setY(((Location) phrase).y);
                }
                List<Block> blockList = phrase.ast2xml();
                if ( blockList != null ) { // TODO: textlyJava - ast2xml should never return null, but does (e.g. Location)
                    instance.getBlock().addAll(blockList);
                }
            }
        }
        return blockSet;
    }

    private static BlockSet astToJaxb(ConfigurationAst config) {
        Assert.notNull(config);
        Map<String, ConfigurationComponent> configurationComponents = config.getConfigurationComponents();
        BlockSet blockSet = new BlockSet();
        blockSet.setRobottype(config.getRobotType());
        blockSet.setXmlversion(config.getXmlVersion());
        blockSet.setDescription(config.getDescription());
        blockSet.setTags(config.getTags());
        for ( ConfigurationComponent configComp : configurationComponents.values() ) {
            Instance instance = new Instance();
            blockSet.getInstance().add(instance);
            instance.setX(String.valueOf(configComp.x));
            instance.setY(String.valueOf(configComp.y));
            instance.getBlock().addAll(configComp.ast2xml());
        }
        return blockSet;
    }
}
