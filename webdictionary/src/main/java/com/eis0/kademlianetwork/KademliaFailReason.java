package com.eis0.kademlianetwork;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.FailReason;

public class KademliaFailReason extends FailReason {

    /**
     * Private constructor as suggested in the TypeSafe enum pattern.
     *
     * @param name The name of the enumeration.
     */
    protected KademliaFailReason(final @NonNull String name) {
        super(name);
    }
}
