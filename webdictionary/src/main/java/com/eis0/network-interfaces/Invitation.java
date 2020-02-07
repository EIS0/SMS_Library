package com.eis.communication.network;

import com.eis.communication.Peer;

/**
 * Represents a received invitation to join a network.
 * <p>
 * Could contain network name or invitation message if needed.
 *
 * @param <P> The type of address used.
 * @author Luca Crema
 */
public interface Invitation<P extends Peer> {

    /**
     * @return The address of who sent this invitation.
     */
    P getInviterPeer();

}
