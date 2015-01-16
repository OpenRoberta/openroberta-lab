package de.fhg.iais.roberta.hardwarecomponents;

/**
 * All kinds of objects that we have in the AST (abstract syntax tree) are separated in these {@link Category}.
 */
public enum Category {
    EXPR, SENSOR, ACTOR, STMT, TASK, FUNCTION, METHOD;
}