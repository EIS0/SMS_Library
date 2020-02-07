package com.eis.communication.network.listeners;

import com.eis.communication.network.FailReason;

/**
 * Listener for resource removing requests, often passed as parameter when making a remove resource request.
 * <p>If the resource has been correctly removed {@link #onResourceRemoved(Object)} is called, {@link #onResourceRemoveFail(Object, FailReason)} is called otherwise.
 *
 * @param <RK> Resource key type.
 * @param <FR> FailedReason type, could be {@link FailReason} class too, without having to extend it.
 * @author Luca Crema
 */
public interface RemoveResourceListener<RK, FR extends FailReason> {

    /**
     * Callback for completed resource removal.
     *
     * @param key The removed key.
     */
    void onResourceRemoved(RK key);

    /**
     * Callback for failed resource removal.
     *
     * @param key    The key that was to be removed.
     * @param reason The reason of the failure.
     */
    void onResourceRemoveFail(RK key, FR reason);
}
