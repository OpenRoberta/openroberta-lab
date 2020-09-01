package de.fhg.iais.roberta.persistence.util;

public interface DbUpgraderInterface {

    /**
     * test, whether an upgrade has been done in the past. In most cases this is tested by a SQL stmt, that checks, whether some DDL has been applied to the
     * database, usually by checking that a table exists.
     *
     * @return true, if this upgrade is already done; false, otherwise
     */
    boolean isUpgradeDone();

    /**
     * run the upgrade of the database<br>
     */
    void run();

}