package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.components.Project;

/**
 * The general interface for workers. Workers are embedded into workflows, many of them depending on the result of previous workers.
 * Workers in workflows are executed one after the other (see class ProjectService).
 * Each worker stores in the Project object whether it succeeded or failed.
 * After the first failed worker only those workers are ececuted, which declare that (see below)
 * Workflows, workers and their position in the workflows are declared in the .properties file of each robot plugin.
 */
public interface IWorker {
    /**
     * @return true, if this worker should run in case of a preceding failing worker. DEFAULT IS FALSE
     */
    default boolean mustRunEvenIfPreviousWorkerFailed() {
        return false;
    }

    /**
     * Executes the worker. Adds its results, or gets previous results for further processing from the {@link Project}.
     *
     * @param project the project in the necessary state for the worker
     */
    void execute(Project project);
}
