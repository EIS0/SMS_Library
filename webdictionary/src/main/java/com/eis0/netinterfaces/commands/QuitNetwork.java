package com.eis0.netinterfaces.commands;

import androidx.annotation.NonNull;

import com.eis.communication.Peer;
import com.eis0.netinterfaces.NetSubscriberList;

/**
 * Command to quit a network, which is, removing myself from the network I'm in
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato (edited comments)
 * @author Giovanni Velludo (added NonNull)
 */
public abstract class QuitNetwork<T extends Peer> extends Command {

    protected final NetSubscriberList<T> netSubscribers;

    /**
     * Constructor for the QuitNetwork command, needs the data to operate
     *
     * @param netSubscribers The subscribers currently in the network
     */
    public QuitNetwork(@NonNull NetSubscriberList<T> netSubscribers) {
        this.netSubscribers = netSubscribers;
    }

    /**
     * Removes myself from the subscribers list and broadcasts it to the net
     */
    protected abstract void execute();

}
