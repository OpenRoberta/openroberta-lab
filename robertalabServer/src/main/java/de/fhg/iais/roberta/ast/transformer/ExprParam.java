package de.fhg.iais.roberta.ast.transformer;

public class ExprParam {
    private final String name;
    private final Class<?> defaultValue;

    public ExprParam(String name, Class<?> defaultValue) {
        super();
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public String toString() {
        return "ExprParametar [name=" + this.name + ", defaultValue=" + this.defaultValue + "]";
    }
}
