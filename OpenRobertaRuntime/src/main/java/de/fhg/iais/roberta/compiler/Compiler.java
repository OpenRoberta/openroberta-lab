package de.fhg.iais.roberta.compiler;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class Compiler {

    public static void main(String[] args) {
        runBuild("userProjects", "01234567", "Main");
    }

    /**
     * clean target directory<br>
     * compile Main.java<br>
     * make jar of Main.class with META-INF informations<br>
     * 
     * @param directory
     * @param token
     * @param programName
     */
    private static void runBuild(String directory, String token, String programName) {

        File buildFile = new File(directory + "\\build.xml");
        Project project = new Project();

        //project.setProperty("token", token);

        project.init();
        ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
        project.addReference("ant.projectHelper", projectHelper);
        projectHelper.parse(project, buildFile);

        project.executeTarget(project.getDefaultTarget());
    }

}
