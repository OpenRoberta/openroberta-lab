package de.fhg.iais.roberta.transformer;

/**
 * This class is parameter class for finding expression or creating empty expression if the expression is missing in the XML. Client must provide type of the
 * value the expression should have
 * and the name of the location of the
 * expression in the XML.
 * 
 * @author kcvejoski
 */
public class ExprParam {
    private final String name;
    private final Class<?> defaultValue;

    /**
     * @param name of the location of the expression in the XML
     * @param defaultValue type if the expression is missing
     */
    public ExprParam(String name, Class<?> defaultValue) {
        super();
        this.name = name;
        this.defaultValue = defaultValue;
    }

    /**
     * @return name of the location of the expression in the XML.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return type of the value the expression should have if it is missing.
     */
    public Class<?> getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String toString() {
        return "ExprParametar [name=" + this.name + ", defaultValue=" + this.defaultValue + "]";
    }
}
