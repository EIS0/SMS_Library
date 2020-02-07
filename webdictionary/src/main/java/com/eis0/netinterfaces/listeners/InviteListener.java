package com.eis0.netinterfaces.listeners;

import com.eis.communication.Peer;
import com.eis0.netinterfaces.NetworkManager;
import com.eis0.netinterfaces.FailReason;

/**
 * Listener for sent invitations to join the network, often passed as parameter in {@link NetworkManager#invite(Peer, InviteListener)}.
 * <p>
 * If the invitation was correctly sent {@link #onInvitationSent(Peer)} will be called,
 * {@link #onInvitationNotSent(Peer, FR)} otherwise.
 *
 * @param <P> The type of address used.
 * @author Luca Crema
 */
public interface InviteListener<P extends Peer, FR extends FailReason> {

    /**
     * Callback for successful sent invitation.
     *
     * @param invitedPeer Who has been invited.
     */
    void onInvitationSent(P invitedPeer);

    /**
     * Callback for failed sending of invitation.
     *
     * @param notInvitedPeer Who were to be invited.
     * @param failReason     The reason for the failed invitation send.
     */
    void onInvitationNotSent(P notInvitedPeer, FR failReason);

}
