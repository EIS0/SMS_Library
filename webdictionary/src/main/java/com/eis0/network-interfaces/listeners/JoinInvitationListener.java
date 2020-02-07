package com.eis.communication.network.listeners;

import com.eis.communication.network.Invitation;

/**
 * Listener for invitations to join a network.
 *
 * @param <I> The type of invitation used.
 * @author Luca Crema
 * @author Marco Mariotto
 * @author Alessandra Tonin
 */
public interface JoinInvitationListener<I extends Invitation> {

    /**
     * Callback for received invitation to join a network from another user.
     *
     * @param invitation The received invitation.
     */
    void onJoinInvitationReceived(I invitation);

}
