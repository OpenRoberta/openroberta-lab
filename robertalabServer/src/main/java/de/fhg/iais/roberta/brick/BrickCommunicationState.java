package de.fhg.iais.roberta.brick;

/**
 * the three states of communication between the brick (requesting a download or not) and the browser client (run button pressed).
 * See class {@link BrickCommunicationData} for
 * further explanations.<br>
 * 
 * @author rbudde
 */
public enum BrickCommunicationState {
    RUN_BUTTON_WAS_PRESSED, DOWNLOAD_REQUEST_FROM_BRICK_ARRIVED, NOTHING_TO_DO;
}
