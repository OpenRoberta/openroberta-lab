package de.fhg.iais.roberta.bean;

import de.fhg.iais.roberta.util.dbc.DbcException;

public interface IProjectBean {
    default void merge(IProjectBean bean) {
        throw new DbcException("Functionality not implemented!");
    }

    @FunctionalInterface
    interface IBuilder<T extends IProjectBean> {
        T build();
    }
}
