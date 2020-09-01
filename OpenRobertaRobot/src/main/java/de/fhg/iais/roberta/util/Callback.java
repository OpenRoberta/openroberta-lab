package de.fhg.iais.roberta.util;

@FunctionalInterface
public interface Callback<V> {
    void call(V value);
}
