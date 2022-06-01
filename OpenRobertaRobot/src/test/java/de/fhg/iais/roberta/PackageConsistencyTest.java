package de.fhg.iais.roberta;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.reflect.ClassPath;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.worker.IWorker;

/**
 * this tests checks,
 * - that all classes
 * - found in a package specified
 * - are subclasses of a class specified.
 * Used to guarantee for instance, that all classes from package "de.fhg.iais.roberta.syntax" are
 * subtypes of class Phrase.class
 * <p>
 * What is missing: check,
 * - that all classes
 * - which are subclasses of a class specified,
 * - are found in a package specified.
 * <p>
 * This can be checked in Intellij in the type hierarchy of the super class by exporting the whole hierarchy
 * into a text file and inspecting the package names
 */
public class PackageConsistencyTest {
    @Test
    public void testClassesInAstPackageAreAstClasses() throws IOException {
        Assert.assertTrue(checkAllClassesInPackage("de.fhg.iais.roberta.syntax.", Phrase.class));
    }

    @Test
    public void testClassesInWorkerPackageAreWorkerClasses() throws IOException {
        Assert.assertTrue(checkAllClassesInPackage("de.fhg.iais.roberta.worker.", IWorker.class));
    }

    @Test
    public void testClassesInVisitorPackageAreVisitorClasses() throws IOException {
        Assert.assertTrue(checkAllClassesInPackage("de.fhg.iais.roberta.visitor.", IVisitor.class));
    }

    private boolean checkAllClassesInPackage(String packageName, Class superClazz) throws IOException {
        boolean hasErrors = ClassPath.from(ClassLoader.getSystemClassLoader())
            .getAllClasses()
            .stream()
            .filter(clazz -> clazz.getName().startsWith(packageName))
            .map(clazzInfo -> clazzInfo.load())
            .map(clazz -> classIsValidInThePackage(clazz, superClazz, packageName))
            .reduce(Boolean.TRUE, Boolean::logicalAnd);
        return hasErrors;
    }

    private boolean classIsValidInThePackage(Class clazz, Class superClazz, String packageName) {
        if ( clazz.isMemberClass() ) {
            // System.out.println("member " + clazz);
            return true;
        } else if ( clazz.isAnonymousClass() ) {
            // System.out.println("anonymous " + clazz);
            return true;
        } else if ( superClazz.isAssignableFrom(clazz) ) {
            return true;
        } else {
            System.out.println("should be removed from package " + packageName + ": " + clazz);
            return false;
        }
    }
}
