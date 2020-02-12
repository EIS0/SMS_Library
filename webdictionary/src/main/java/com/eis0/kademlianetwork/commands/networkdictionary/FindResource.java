package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.KademliaFailReason;
import com.eis0.kademlianetwork.activitystatus.GetResourceTimer;
import com.eis0.kademlianetwork.commands.KadFindId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;
import com.eis0.netinterfaces.commands.CommandExecutor;

public class FindResource extends Command {

    protected final String key;
    private RequestsHandler requestsHandler;

    private String resource;
    private KademliaFailReason failReason;
    private boolean hasSuccessfullyCompleted = false;

    /**
     * Constructor for the FindResource command
     *
     * @param key                     The key to find in the network
     * @param requestsHandler the RequestsHandler used by this command
     */
    public FindResource(@NonNull String key, @NonNull RequestsHandler requestsHandler) {
        this.key = key;
        this.requestsHandler = requestsHandler;
    }

    /**
     * Finds a resource from the network dictionary
     *
     * @see {@link RequestsHandler} for more details
     */
    public void execute() {
        //Finding a resource goes as follows:
        //1. Start a new DeleteResourceRequest
        FindResourceRequest resourceRequest = requestsHandler.startFindResourceRequest(key);

        //2. Search the closest id to the resource you're searching
        KademliaId idToFind = resourceRequest.getKeyId();
        KadFindId findIdCommand = new KadFindId(idToFind, requestsHandler);
        CommandExecutor.execute(findIdCommand);
        if(!findIdCommand.hasSuccessfullyCompleted()){
            failReason = findIdCommand.getFailReason();
            return;
        }

        //if I'm here it means that I've found the closest person to the id to find
        //3. Ask that person for the resource
        SMSMessage message = new KademliaMessage()
                .setPeer(findIdCommand.getPeerFound())
                .setRequestType(RequestTypes.GetFromDict)
                .setKey(key)
                .buildMessage();
        SMSManager.getInstance().sendMessage(message);

        //4. Wait for that person to answer
        new GetResourceTimer(resourceRequest).run();

        //If I'm here it means that I've found the resource of the request took too long
        if(!resourceRequest.isCompleted()){
            failReason = KademliaFailReason.REQUEST_EXPIRED;
            return;
        }

        //If I'm here it means that I've found the resource, and completed this Command
        requestsHandler.completeFindResourceRequest(key, resourceRequest.getResource());
        hasSuccessfullyCompleted = true;
        resource = resourceRequest.getResource();
    }

    /**
     * @return Return the resource found by the FindResource Command. If no resource was found
     * null is returned instead.
     */
    public String getResource(){
        return resource;
    }

    /**
     * @return Returns a KademliaFailReason for this request. If the Request
     * completed successfully, or is still going, null is returned.
     */
    public KademliaFailReason getFailReason(){
        return failReason;
    }

    /**
     * @return Returns true if the command successfully completed and found a resource
     */
    public boolean hasSuccessfullyCompleted(){
        return hasSuccessfullyCompleted;
    }
}
