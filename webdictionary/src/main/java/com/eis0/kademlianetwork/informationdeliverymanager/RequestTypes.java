package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis0.kademlianetwork.listener.SMSKademliaListener;

/**
 * This enum contains the DeleteResourceRequest Types, which are the CODE placed at the beginning of every message
 * sent through the network; they identify the DeleteResourceRequest contained in each message, depending upon
 * the RequestType received nodes act differently, they recognize the nature of the request with the
 * {@link SMSKademliaListener} class
 *
 * @author Enrico Cestaro
 * @author Marco Cognolato
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
    ResultGetRequest,
    RemoveFromDict,

    //Requests to handle the research closest ID
    FindId,
    FindIdSearchResult,

    //Refreshing operation
    Ping,
    Pong,
}
