package com.eis0.kademlianetwork;

public enum RequestTypes {
    //Requests for joining the network
    JoinPermission,
    AcceptJoin,

    //Acknowledge message
    AcknowledgeMessage,

    //Requests to handle the resource exchange
    AddToDict,
    GetFromDict,
    RemoveFromDict,

    //Requests to handle the research closest ID
    FindId,
    SearchResult,

    //Requests to handle the research for the closest ID in the resource exchange process:
    //Adding an element to the Dictionary
    FindIdForAddRequest,
    AddRequestResult,
    //Asking for an element in the Dictionary
    FindIdForGetRequest,
    GetRequestResult
    //Deleting an element from the Dictionary

}
