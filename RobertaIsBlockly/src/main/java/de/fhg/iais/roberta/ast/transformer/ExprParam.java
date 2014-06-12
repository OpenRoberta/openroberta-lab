package de.fhg.iais.roberta.ast.transformer;

public class ExprParam {
    private String name;
    private Class<?> defaultValue;

    public ExprParam(String name, Class<?> defaultValue) {
        super();
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Class<?> defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "ExprParametar [name=" + name + ", defaultValue=" + defaultValue + "]";
    }
}
