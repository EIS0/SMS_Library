package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis0.kademlianetwork.listener.SMSKademliaListener;

/**
 * This enum contains the Request Types, which are the CODE placed at the beginning of every message
 * sent through the network; they identify the Request contained in each message, depending upon
 * the RequestType received nodes act differently, they recognize the nature of the request with the
 * {@link SMSKademliaListener} class
 */
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
    ResultAddRequest,
    //Asking for an element in the Dictionary
    FindIdForGetRequest,
    ResultGetRequest,
    //Deleting an element from the Dictionary
    FindIdForDeleteRequest,
    ResultDeleteRequest,

    //Refreshing operation
    Ping,
    Pong,
    FindIdRefresh,
    SearchResultReplacement
}
