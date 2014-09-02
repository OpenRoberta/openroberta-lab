package de.fhg.iais.roberta.compiler;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

/**
 * Class which compiles the generated user programs via ant script (build.xml).<br>
 * 
 * @author dpyka
 */
@Deprecated
public class Compiler {

    @Deprecated
    public static void main(String[] args) {
        runBuild("userProjects", "01234567", "blinker2", "generated.main");
    }

    /**
     * 1. Make target folder (if not exists).<br>
     * 2. Clean target folder (everything inside).<br>
     * 3. Compile .java files to .class.<br>
     * 4. Make jar from class files and add META-INF entries.<br>
     * 
     * @param userProjectsDir
     * @param token
     * @param mainFile
     * @param mainPackage
     */
    @Deprecated
    private static void runBuild(String userProjectsDir, String token, String mainFile, String mainPackage) {

        File buildFile = new File(userProjectsDir + "\\build.xml");
        Project project = new Project();

        project.setProperty("user.projects.dir", userProjectsDir);
        project.setProperty("token.dir", token);
        project.setProperty("main.name", mainFile);
        project.setProperty("main.package", mainPackage);

        project.init();

        ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
        projectHelper.parse(project, buildFile);

        project.executeTarget(project.getDefaultTarget());
    }

}
