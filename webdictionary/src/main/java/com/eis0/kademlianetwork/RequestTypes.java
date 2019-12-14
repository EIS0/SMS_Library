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
    AddToDictRequest,
    AddToDict,
    RemoveFromDict,
    UpdateDict,

    NodeLookup,

    //Request to handle the research closest ID
    FindId,
    SearchResult,

    //Request to handle the research for the closest ID in the resource exchange
    FindIdForAddRequest,
    AddRequestResult
}
