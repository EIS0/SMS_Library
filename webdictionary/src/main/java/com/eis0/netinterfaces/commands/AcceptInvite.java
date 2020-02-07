package com.eis0.netinterfaces.commands;

import com.eis0.netinterfaces.Invitation;

/**
 * Command to accept an Invitation to the network
 *
 * @author Marco Cognolato
 * @author Giovanni Velludo
 */
public abstract class AcceptInvite<I extends Invitation> extends Command {

    protected final I invitation;

    /**
     * Constructor for AcceptInvite command, requires data to work
     *
     * @param invitation The Invitation to a network
     */
    public AcceptInvite(I invitation) {
        this.invitation = invitation;
    }

    /**
     * Invites a peer to join a network
     */
    protected abstract void execute();
}
