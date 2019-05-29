package de.fhg.iais.roberta.components.ev3lejos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class ClassJavaFileObject extends SimpleJavaFileObject {
    /**
     * Byte code created by the compiler will be stored in this ByteArrayOutputStream so that we can later get the byte array out of it and put it in the memory
     * as an instance of our class.
     */
    protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    /**
     * Registers the compiled class object under URI containing the class full name
     *
     * @param name Full name of the compiled class
     */
    public ClassJavaFileObject(String name) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
    }

    /**
     * Will be used by our file manager to get the byte code that can be put into memory to instantiate our class
     *
     * @return compiled byte code
     */
    public byte[] getBytes() {
        return this.bos.toByteArray();
    }

    /**
     * Will provide the compiler with an output stream that leads to our byte array. This way the compiler will write everything into the byte array that we
     * will instantiate later
     */
    @Override
    public OutputStream openOutputStream() throws IOException {
        return this.bos;
    }

}
