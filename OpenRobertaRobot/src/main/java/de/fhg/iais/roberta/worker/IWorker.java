package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.components.Project;

/**
 * The general interface for workers.
 * Workers are embedded into workflows, many of them depending on the result of previous workers.
 * Robot-specific workers and their position in the workflows are declared in the robots .properties file.
 */
public interface IWorker {
    /**
     * Executes the worker. Adds its results, or gets previous results for further processing from the {@link Project}.
     * @param project the project in the necessary state for the worker
     */
    void execute(Project project);
}
