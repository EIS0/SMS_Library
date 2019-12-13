package com.eis0.kademlianetwork;

/**
 * This class handle the resource requests, it allows the user to:
 * 1. Create a request for adding a resource to the dictionary
 * 2. Rack up multiple pending requests, waiting to be triggered
 * 3. Handle the response to the request, containing the node ID closest to the resource ID that the
 * request is trying to add to the network
 * 4. Send the resource, close the pending request, remove it from the list
 *
 * FEATURE: it needs a refresh method that removes from the list old requests, or try to solve them
 * (for a finite amount of attempts)
 */
public class ResourceExchangeHandler {



}
