package com.eis.communication.network.listeners;

import com.eis.communication.network.FailReason;

/**
 * Listener for resource retrieval requests, often passed as parameter when making a get resource request.
 * <p>
 * If the resource has been correctly retrieved {@link #onGetResource(Object, Object)} is called,
 * {@link #onGetResourceFailed(Object, FailReason)} is called otherwise.
 *
 * @param <RK> Resource key type.
 * @param <RV> Resource value type.
 * @param <FR> FailedReason type, could be {@link FailReason} class too, without having to extend it.
 * @author Luca Crema
 * @author Marco Mariotto
 */
public interface GetResourceListener<RK, RV, FR extends FailReason> {

    /**
     * Callback for completed resource retrieval.
     *
     * @param key   The requested key.
     * @param value The retrieved value.
     */
    void onGetResource(RK key, RV value);

    /**
     * Callback for failed resource retrieval.
     *
     * @param key    The requested key.
     * @param reason The reason of the failure. The possible values can be found on {@link FailReason}.
     */
    void onGetResourceFailed(RK key, FR reason);

}
