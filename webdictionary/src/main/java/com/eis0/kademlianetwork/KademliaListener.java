package com.eis0.kademlianetwork;

import com.eis0.kademlia.Contact;

public interface KademliaListener {

    void onNewContact(Contact contact);
}
