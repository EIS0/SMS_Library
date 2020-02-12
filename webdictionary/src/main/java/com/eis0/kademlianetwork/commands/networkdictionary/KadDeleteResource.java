package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis0.kademlianetwork.KademliaFailReason;
import com.eis0.kademlianetwork.commands.KadFindId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.DeleteResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;
import com.eis0.netinterfaces.commands.CommandExecutor;

public class KadDeleteResource extends Command {

    protected final String key;
    private RequestsHandler requestsHandler;

    private KademliaFailReason failReason;
    private boolean hasSuccessfullyCompleted;

    /**
     * Constructor for the KadDeleteResource command
     *
     * @param key                     The key to Remove from the network
     * @param requestsHandler The RequestsHandler used by the command
     */
    public KadDeleteResource(@NonNull String key, @NonNull RequestsHandler requestsHandler) {
        this.key = key;
        this.requestsHandler = requestsHandler;
    }

    /**
     * Removes a node from the network dictionary
     * Search where it is. Remove it.
     *
     * @see {@link RequestsHandler} for more details
     */
    public void execute() {
        DeleteResourceRequest deleteRequest = requestsHandler.startDeleteResourceRequest(key);

        //Starts to search for the closest ID
        KadFindId findIdCommand = new KadFindId(deleteRequest.getKeyId(), requestsHandler);
        CommandExecutor.execute(findIdCommand);

        if(!findIdCommand.hasSuccessfullyCompleted()){
            failReason = findIdCommand.getFailReason();
            return;
        }

        //Send him a DeleteFromLocalDictionary request
        SMSMessage message = new KademliaMessage()
                .setPeer(findIdCommand.getPeerFound())
                .setRequestType(RequestTypes.RemoveFromDict)
                .setKey(key)
                .buildMessage();
        SMSManager.getInstance().sendMessage(message);

        //TODO: wait for him to acknowledge the reception
        requestsHandler.completeDeleteResourceRequest(key);

        hasSuccessfullyCompleted = true;

    }

    public boolean hasSuccessfullyCompleted(){
        return hasSuccessfullyCompleted;
    }

    public KademliaFailReason getFailReason(){
        return failReason;
    }
}
