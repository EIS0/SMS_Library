package com.eis.communication.network.listeners;

import com.eis.communication.network.FailReason;

/**
 * Listener for resource saving requests, often passed as parameter when making a set resource request.
 * <p>If the resource has been correctly set {@link #onResourceSet(Object, Object)} is called, {@link #onResourceSetFail(Object, Object, FailReason)} is called otherwise.
 *
 * @param <RK> Resource key type.
 * @param <RV> Resource value type.
 * @param <FR> FailedReason type, could be {@link FailReason} class too, without having to extend it.
 * @author Luca Crema
 * @author Marco Mariotto
 */
public interface SetResourceListener<RK, RV, FR extends FailReason> {

    /**
     * Callback for completed resource insertion.
     *
     * @param key   The inserted key.
     * @param value The inserted value.
     */
    void onResourceSet(RK key, RV value);

    /**
     * Callback for failed resource insertion.
     *
     * @param key    The key that was to be added.
     * @param value  The value that was to be added.
     * @param reason The reason of the failure.
     */
    void onResourceSetFail(RK key, RV value, FR reason);

}
