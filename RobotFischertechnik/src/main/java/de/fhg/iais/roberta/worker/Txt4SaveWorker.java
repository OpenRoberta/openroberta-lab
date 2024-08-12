package de.fhg.iais.roberta.worker;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ZipHelper;

/**
 * Saves the generated program directly to the temporary directory for further processing. Used by robots/languages that need no compilation like ev3dev.
 */
public class Txt4SaveWorker implements IWorker {
    Logger LOG = LoggerFactory.getLogger(Txt4SaveWorker.class);

    @Override
    public final void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String tempDir = compilerWorkflowBean.getTempDir();
        final String token = project.getToken();
        final String programName = project.getProgramName();
        final String srcExtension = "." + project.getSourceCodeFileExtension();
        final String tgtExtension = "." + project.getBinaryFileExtension();
        Util.storeGeneratedProgram(tempDir, project.getSourceCodeBuilder().toString(), token, programName, srcExtension);
        File resourceDir = new File(project.getRobotFactory().getPluginProperties().getUpdateDir() + "/0-0-1/roberta");
        File sourceDir = new File(tempDir + token + "/" + programName + "/source");
        File targetFile = new File(tempDir + token + "/" + programName + "/target/" + programName + tgtExtension);
        File libDir = new File(tempDir + token + "/" + programName + "/source/lib");
        try {
            FileUtils.forceMkdir(libDir);
            FileUtils.copyDirectory(resourceDir, libDir);
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
            targetFile.delete();
            ZipHelper.zipDirectory(sourceDir, targetFile.toPath());
        } catch ( IOException e ) {
            LOG.error("auxiliary files could not be copied from " + resourceDir.getAbsolutePath());
            project.setResult(Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED);
        }
    }
}
