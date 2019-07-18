package de.fhg.iais.roberta.visitor.validate;

import java.util.Map;

import de.fhg.iais.roberta.visitor.IVisitor;

public interface IValidatorVisitor<V> extends IVisitor<Void> {
    void validate();

    Map<String, String> getResult();
}
