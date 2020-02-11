package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis0.kademlianetwork.KademliaFailReason;
import com.eis0.kademlianetwork.commands.KadFindId;
import com.eis0.kademlianetwork.informationdeliverymanager.AddResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;
import com.eis0.netinterfaces.commands.CommandExecutor;

public class KadAddResource extends Command {

    protected final String key;
    protected final String resource;
    private final RequestsHandler requestsHandler;

    private KademliaFailReason failReason;
    private boolean hasSuccessfullyCompleted = false;

    /**
     * Set a <key, resource> pair in the network dictionary
     *
     * @param key
     * @param resource
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

        if(!findIdCommand.hasSuccessfullyCompleted()){
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

        //TODO: wait for him to acknowledge the reception
        requestsHandler.completeAddResourceRequest(key);
        hasSuccessfullyCompleted = true;
    }

    public boolean hasSuccessfullyCompleted(){
        return hasSuccessfullyCompleted;
    }

    public KademliaFailReason getFailReason(){
        return failReason;
    }
}
