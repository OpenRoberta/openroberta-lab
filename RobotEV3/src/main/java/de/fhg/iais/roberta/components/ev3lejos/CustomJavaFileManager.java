package de.fhg.iais.roberta.components.ev3lejos;

import java.io.IOException;
import java.security.SecureClassLoader;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardJavaFileManager;

public class CustomJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    private ClassJavaFileObject classJavaFileObject;

    /**
     * Instance of ClassJavaFileObject that will store the compiled bytecode of our class
     */
    public ClassJavaFileObject getClassJavaFileObject() {
        return this.classJavaFileObject;
    }

    /**
     * Will initialize the manager with the specified standard java file manager
     *
     * @param standardManger
     */
    public CustomJavaFileManager(StandardJavaFileManager standardManager) {
        super(standardManager);
    }

    /**
     * Will be used by us to get the class loader for our compiled class. It creates an anonymous class extending the SecureClassLoader which uses the byte code
     * created by the compiler and stored in the JavaClassObject, and returns the Class for it
     */
    @Override
    public ClassLoader getClassLoader(Location location) {
        return new SecureClassLoader() {
            @Override
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                byte[] b = CustomJavaFileManager.this.classJavaFileObject.getBytes();
                return super.defineClass(name, CustomJavaFileManager.this.classJavaFileObject.getBytes(), 0, b.length);
            }
        };
    }

    /**
     * Gives the compiler an instance of the JavaClassObject so that the compiler can write the byte code into it.
     */
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
        this.classJavaFileObject = new ClassJavaFileObject(className);
        return this.classJavaFileObject;
    }
}
