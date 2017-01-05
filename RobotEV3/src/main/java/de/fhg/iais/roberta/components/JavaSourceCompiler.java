package de.fhg.iais.roberta.components;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class JavaSourceCompiler {
    private CustomJavaFileManager fileManager;
    private final JavaCompiler compiler;
    private final String fullName;
    private final String sourceCode;

    /**
     * @param fullName
     *        Full name of the class that will be compiled.
     *        If class should be in some package, fullName should contain it too (ex. "generated.NEPOprog")
     * @param sourceCode
     *        Here we specify the source code of the class to be compiled
     */
    public JavaSourceCompiler(String fullName, String sourceCode) {
        this.fullName = fullName;
        this.sourceCode = sourceCode;
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.fileManager = initFileManager();
    }

    private CustomJavaFileManager initFileManager() {
        if ( this.fileManager != null ) {
            return this.fileManager;
        } else {
            this.fileManager = new CustomJavaFileManager(this.compiler.getStandardFileManager(null, null, null));
            return this.fileManager;
        }

    }

    public CompilerFeedback compile() {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
        javaFiles.add(new SourceJavaFileObject(this.fullName, this.sourceCode));

        List<String> compilationOptions = new ArrayList<String>();
        compilationOptions.add("-source");
        compilationOptions.add("1.7");
        compilationOptions.add("-target");
        compilationOptions.add("1.7");

        CompilationTask task = this.compiler.getTask(null, this.fileManager, diagnostics, compilationOptions, null, javaFiles);
        Boolean isSuccess = task.call();
        if ( isSuccess ) {
            byte[] classToBeJared = this.fileManager.getClassJavaFileObject().getBytes();
            ByteArrayOutputStream tt = createJarArchive(classToBeJared);

            try {
                FileOutputStream fw = new FileOutputStream("./NEPOProg.jar");
                fw.write(tt.toByteArray());
                fw.close();
                tt.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
        return new CompilerFeedback(isSuccess, diagnostics);
    }

    private ByteArrayOutputStream createJarArchive(byte[] tobeJared) {

        // Open archive file
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JarOutputStream out;
        try {
            Manifest mf = new Manifest();
            mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            mf.getMainAttributes().put(Attributes.Name.MAIN_CLASS, "generated.main.NEPOprog.class");

            out = new JarOutputStream(stream, mf);
            // Add archive entry
            JarEntry jarAdd = new JarEntry("/generated/main/NEPOprog.class");
            out.putNextEntry(jarAdd);
            out.write(tobeJared);
            out.close();
            stream.close();
            return stream;
        } catch ( IOException e ) {

            e.printStackTrace();
        }
        return stream;

    }

    public byte[] getCompiledClass() {
        return this.fileManager.getClassJavaFileObject().getBytes();
    }
}
