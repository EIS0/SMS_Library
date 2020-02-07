package com.eis0.netinterfaces;

import androidx.annotation.NonNull;

/**
 * Enumeration of reasons why a resource get or set wasn't performed correctly.
 * This class is a "TypeSafe enum" so that it can be extended in order to have more reasons.
 * <p>
 * If you want to extend this class the enum must be protected or private.
 *
 * @author Luca Crema
 * @see <a href="http://www.javacamp.org/designPattern/enum.html">JavaCamp TypeSafe enum pattern</a>
 */
public class FailReason {
    /**
     * When something in the network goes wrong.
     */
    public static final FailReason NETWORK_ERROR = new FailReason("NetworkError");

    /**
     * When there is no network to communicate.
     */
    public static final FailReason NO_NETWORK = new FailReason("NoNetwork");

    /**
     * Generic failure.
     */
    public static final FailReason GENERIC_FAIL = new FailReason("GenericFail");

    private String name;

    /**
     * Private constructor as suggested in the TypeSafe enum pattern.
     *
     * @param name The name of the enumeration.
     */
    protected FailReason(final @NonNull String name) {
        this.name = name;
    }

    /**
     * toString override.
     *
     * @return The name of the fail reason.
     */
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
