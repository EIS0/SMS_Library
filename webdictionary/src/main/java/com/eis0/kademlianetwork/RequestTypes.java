package com.eis0.kademlianetwork;

public enum RequestTypes {
    //Requests for joining the network
    JoinPermission,
    AcceptJoin,

    //Requests for leaving the network
    LeavePermission,
    AcceptLeave,

    //Requests for updating the network
    AddPeers,
    RemovePeers,
    UpdatePeers,

    //Acknowledge message
    AcknowledgeMessage,

    //Requests to handle the resource exchange
    AddToDict,
    RemoveFromDict,
    UpdateDict,

    NodeLookup,

    //Requests to handle the research closest ID
    FindId,
    SearchResult,

    //Requests to handle the research for the closest ID in the resource exchange:
    //Adding an element to the Dictionary
    FindIdForAddRequest,
    AddRequestResult
    //Asking for an element in the Dictionary

    //Deleting an element from the Dictionary

}
