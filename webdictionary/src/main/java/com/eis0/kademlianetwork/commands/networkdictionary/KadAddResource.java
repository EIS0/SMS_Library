package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis0.kademlianetwork.KademliaFailReason;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.commands.KadFindId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.AddResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;
import com.eis0.netinterfaces.commands.CommandExecutor;

/**
 * Command to add a resource inside the network
 *
 * @author Marco Cognolato
 */
public class KadAddResource extends Command {

    protected final String key;
    protected final String resource;
    private final RequestsHandler requestsHandler;

    private KademliaFailReason failReason;
    private boolean hasSuccessfullyCompleted = false;

    /**
     * Set a <key, resource> pair in the network dictionary
     *
     * @param key      The key identifier of the resource
     * @param resource The resource to add to the net
     */
    public KadAddResource(@NonNull String key, @NonNull String resource, @NonNull RequestsHandler requestsHandler) {
        this.key = key;
        this.resource = resource;
        this.requestsHandler = requestsHandler;
    }

    /**
     * Search for the proper node to contain the resource. Add it.
     *
     * @see {@link RequestsHandler} for more details
     */
    public void execute() {
        //1. starts an add resource request
        AddResourceRequest addRequest = requestsHandler.startAddResourceRequest(key, resource);

        //2. Search for the closest ID to the resource
        KadFindId findIdCommand = new KadFindId(addRequest.getKeyId(), requestsHandler);
        CommandExecutor.execute(findIdCommand);

        if (!findIdCommand.hasSuccessfullyCompleted()) {
            failReason = findIdCommand.getFailReason();
            return;
        }

        //3. Send him a AddToLocalDictionary request
        SMSMessage message = new KademliaMessage()
                .setPeer(findIdCommand.getPeerFound())
                .setRequestType(RequestTypes.AddToDict)
                .setKey(key)
                .setResource(resource)
                .buildMessage();
        SMSManager.getInstance().sendMessage(message);

        //If I'm here it means that the closest node found couldn't complete the task
        if (!KademliaJoinableNetwork.getInstance().isAlive(findIdCommand.getPeerFound())) {
            failReason = KademliaFailReason.REQUEST_EXPIRED;
            return;
        }

        requestsHandler.completeAddResourceRequest(key);
        hasSuccessfullyCompleted = true;
    }

    /**
     * @return Returns true if the command successfully completed and found a resource
     */
    public boolean hasSuccessfullyCompleted() {
        return hasSuccessfullyCompleted;
    }

    /**
     * @return Returns a KademliaFailReason for this request. If the Request
     * completed successfully, or is still going, null is returned.
     */
    public KademliaFailReason getFailReason() {
        return failReason;
    }
}
