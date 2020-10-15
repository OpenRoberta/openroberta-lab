package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.ProgramAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.ITransformerVisitor;

/**
 * Used to transform ASTs between different versions.
 * Uses a {@link ITransformerVisitor} to generate a deep copy of the input AST(s) and replaces them in the project.
 * It is only applied up to a specific version range of the AST, the version of the new AST can also be determined.
 */
public abstract class AbstractTransformerWorker implements IWorker {

    private final String minXmlVersion;
    private final String maxXmlVersion;
    private final String newXmlVersion;

    private boolean wasTransformed = false;

    /**
     * Creates a new TransformerWorker.
     * 
     * @param maxXmlVersion the maximum XML version of AST this transformer should apply to
     * @param newXmlVersion the new XML version of the output
     */
    protected AbstractTransformerWorker(String minXmlVersion, String maxXmlVersion, String newXmlVersion) {
        Assert.isTrue(Util.versionCompare(minXmlVersion, maxXmlVersion) <= 0, "Min version cannot be higher than max version!");
        this.minXmlVersion = minXmlVersion;
        this.maxXmlVersion = maxXmlVersion;
        this.newXmlVersion = newXmlVersion;
    }

    private static List<List<Phrase<Void>>> deepCopyAst(List<List<Phrase<Void>>> tree, ITransformerVisitor<Void> visitor) {
        List<List<Phrase<Void>>> newTree = new ArrayList<>(tree.size());
        for ( List<Phrase<Void>> phrases : tree ) {
            List<Phrase<Void>> newPhrases = new ArrayList<>(phrases.size());
            for ( Phrase<Void> phrase : phrases ) {
                Phrase<Void> newPhrase = phrase.modify(visitor);
                newPhrases.add(newPhrase);
            }
            newTree.add(newPhrases);
        }
        return newTree;
    }

    @Override
    public final void execute(Project project) {
        String progXmlVersion = project.getProgramAst().getXmlVersion();
        if ( (Util.versionCompare(progXmlVersion, this.minXmlVersion) == -1) || (Util.versionCompare(progXmlVersion, this.maxXmlVersion) == 1) ) {
            return; // nothing needs to be done
        }

        NewUsedHardwareBean.Builder usedHardwareBeanBuilder = new NewUsedHardwareBean.Builder();

        // TODO usedHardwareBeanBuilder should probably be extracted into its own collect visitor, combined for now so only 1 visitor needs to run
        ITransformerVisitor<Void> visitor = getVisitor(project, usedHardwareBeanBuilder, project.getConfigurationAst());

        // Most of the time no transformation other than the version is necessary
        List<List<Phrase<Void>>> lists;
        if ( visitor != null ) {
            lists = deepCopyAst(project.getProgramAst().getTree(), visitor);
            this.wasTransformed = true;
        } else {
            lists = project.getProgramAst().getTree();
        }
        updateProgram(project, lists);

        if ( project.getConfigurationAst() != null ) {
            updateConfiguration(project, usedHardwareBeanBuilder);
        }
    }

    private void updateProgram(Project project, List<List<Phrase<Void>>> lists) {
        ProgramAst.Builder<Void> progBuilder = new ProgramAst.Builder<>();
        progBuilder.setXmlVersion(this.newXmlVersion);
        progBuilder.setTags(project.getProgramAst().getTags());
        progBuilder.setRobotType(project.getProgramAst().getRobotType());
        progBuilder.setDescription(project.getProgramAst().getDescription());
        progBuilder.addTree(lists);
        project.setProgramAst(progBuilder.build());
    }

    private void updateConfiguration(Project project, NewUsedHardwareBean.Builder usedHardwareBeanBuilder) {
        ConfigurationAst.Builder confBuilder = new ConfigurationAst.Builder();
        confBuilder.setXmlVersion(this.newXmlVersion);
        confBuilder.setTags(project.getConfigurationAst().getTags());
        confBuilder.setRobotType(project.getConfigurationAst().getRobotType());
        confBuilder.setDescription(project.getConfigurationAst().getDescription());
        if ( !this.wasTransformed ) {
            fillHardwareBeanWithOldConf(usedHardwareBeanBuilder, project.getConfigurationAst().getConfigurationComponentsValues());
        }
        confBuilder.addComponents(usedHardwareBeanBuilder.build().getUsedConfigurationComponents());
        project.setConfigurationAst(confBuilder.build());
    }

    private static void fillHardwareBeanWithOldConf(
        NewUsedHardwareBean.Builder usedHardwareBeanBuilder, Collection<ConfigurationComponent> confComps) {
        for ( ConfigurationComponent cc : confComps ) {
            usedHardwareBeanBuilder.addUsedConfigurationComponent(cc);
        }
    }

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic.
     * The returned visitor may be null if no transformation other than the version is necessary.
     * Could be removed in the future, when visitors are specified in the properties as well, or inferred from the worker name.
     *
     * @param project the project
     * @param builder the bean for used hardware
     * @param configuration the configuration
     * @return the appropriate visitor for the current robot, null if no transform is needed
     */
    protected abstract ITransformerVisitor<Void> getVisitor(Project project, NewUsedHardwareBean.Builder builder, ConfigurationAst configuration);
}
