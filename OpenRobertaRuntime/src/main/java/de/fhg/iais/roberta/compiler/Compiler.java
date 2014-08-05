package de.fhg.iais.roberta.compiler;

import java.io.File;
import java.util.Properties;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class Compiler {

    private static Properties p = new Properties();

    public static void main(String[] args) {
        runBuild("userProjects", "01234567", "Main");
    }

    /**
     * three parameters to retrieve the correct directory structure for saved projects
     * 
     * @param directory
     * @param token
     * @param programName
     */
    private static void runBuild(String directory, String token, String programName) {

        File buildFile = new File(directory + "\\build.xml");
        Project project = new Project();

        //project.setProperty("custom", "true");
        project.setProperty("token", token);
        project.setProperty("main.name", programName);
        project.setProperty("input.dir", token);
        project.setProperty("output.dir", project.getProperty("input.dir") + "\\build");
        project.setProperty("lejos.home", "/home/root/lejos");
        project.setProperty("program.jar", project.getProperty("output.dir") + "\\" + programName + ".jar");
        project.setProperty("lib.dir", "c:\\installations\\.m2\\repository\\de\\fhg\\iais\\roberta\\ev3classes\\0.8.1-beta");
        project.setProperty("java.home", "C:\\Program Files\\Java\\jdk1.7.0_51");

        project.init();
        ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
        project.addReference("ant.projectHelper", projectHelper);
        projectHelper.parse(project, buildFile);

        project.executeTarget(project.getDefaultTarget());
    }

}
