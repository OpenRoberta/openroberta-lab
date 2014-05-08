package de.fhg.iais.roberta.javaServer.util;

import de.fhg.iais.roberta.dbc.DbcException;

/**
 * the different states of an ingest. An actual states can be transformed to a followup state by applying a (legal) transformation
 * 
 * @author rbudde
 */
public enum State {
    /**
     * ingest is ready for being transformed. This is the intial state.
     */
    ReadyForTransform,
    /**
     * ingest is being transformed. Until now no error occured.
     */
    Transforming,
    /**
     * ingest is being transformed, but during that process an error occured. Transforming continues.
     */
    TransformingButGotErrors,
    /**
     * ingest is ready for being submitted (sent) to a server. The transformation had no errors.
     */
    ReadyToSubmit,
    /**
     * ingest is ready for being submitted (sent) to a server. During transformation at least one error was reported.
     */
    ReadyToSubmitButGotErrors,
    /**
     * ingest is being submitted (sent) to a server. Until now no error occured.
     */
    Submitting,
    /**
     * ingest is being submitted (sent) to a server, but during that process an error occured. Submitting continues.
     */
    SubmittingButGotErrors,
    /**
     * ingest is submitted (sent) to a server completely. No error occured. This is a final state.
     */
    Sent,
    /**
     * ingest is submitted (sent) to a server completely. During submission at least one error was reported. This is a final state.
     */
    SentButGotErrors;

    /**
     * the different transitions that can be applied to a {@link State}
     * 
     * @author rbudde
     */
    public static enum Transition {
        /**
         * Start a transformation
         */
        Transform,
        /**
         * notification that during transformation or sending an error occured
         */
        ErrorsOccured,
        /**
         * Start sending the data
         */
        Send,
        /**
         * notification that sending has terminated
         */
        Finished;
    }

    /**
     * transform an actual state by applying a given transformation to a followup state.<br>
     * <b>This method defines whether a transformation in a given state is legal or not.</b><br>
     * If the transition is not legal, a RuntimeException is thrown.
     * 
     * @param transition the transition to apply
     * @param state the state to which the transition has to be applied
     * @return the followup state, never null.
     */
    public static State mkStep(Transition transition, State state) {
        if ( transition == Transition.Transform ) {
            if ( state == ReadyForTransform ) {
                return Transforming;
            }
        } else if ( transition == Transition.ErrorsOccured ) {
            if ( state == Transforming ) {
                return TransformingButGotErrors;
            } else if ( state == Submitting ) {
                return SubmittingButGotErrors;
            }
        } else if ( transition == Transition.Finished ) {
            if ( state == Transforming ) {
                return ReadyToSubmit;
            } else if ( state == TransformingButGotErrors ) {
                return ReadyToSubmitButGotErrors;
            } else if ( state == Submitting ) {
                return Sent;
            } else if ( state == SubmittingButGotErrors ) {
                return SentButGotErrors;
            }
        } else if ( transition == Transition.Send ) {
            if ( state == ReadyToSubmit || state == ReadyToSubmitButGotErrors ) {
                return Submitting;
            }
        }
        throw new DbcException("The transition \"" + transition + "\" is invalid in state \"" + state + "\"");
    }
}
