package de.fhg.iais.roberta.brick;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Clock;

public class TokenRegistrationData {
    private static final Logger LOG = LoggerFactory.getLogger(TokenRegistrationData.class);
    private static final int TIMEOUT_MSEC = 300000;

    private final Clock clock = Clock.start();
    private TokenRegistrationState state = TokenRegistrationState.AGREEMENT_REQUEST;

    public TokenRegistrationData() {
    }

    public synchronized boolean brickTokenAgreementRequest() {
        LOG.debug("BRICK starts waiting for the client to submit a matching token");
        while ( this.clock.elapsedMsec() < TIMEOUT_MSEC && this.state == TokenRegistrationState.AGREEMENT_REQUEST ) {
            try {
                wait();
            } catch ( InterruptedException e ) {
                // try again
            }
        }
        LOG.debug("BRICK request for token aggreement terminated. Time elapsed: " + this.clock.elapsedMsec() + ", state: " + this.state);
        return this.state == TokenRegistrationState.AGREED;
    }

    public synchronized void clientAgreedOnTheBrickToken() {
        LOG.debug("CLIENT agreed on token. Request scheduled " + this.clock.elapsedSecFormatted() + " ago");
        this.state = TokenRegistrationState.AGREED;
        notifyAll();
    }

    public synchronized void abortRequest() {
        LOG.debug("Token agreement request aborted. Was schedule " + this.clock.elapsedSecFormatted() + " ago");
        this.state = TokenRegistrationState.ABORTED;
        notifyAll();
    }

    /**
     * the three states of communication between the brick (requesting an agreement on a token) and the browser client (agreed [or not by timeout]).
     */
    public enum TokenRegistrationState {
        AGREEMENT_REQUEST, AGREED, ABORTED;
    }
}