package com.eis0.smslibrary;

public abstract class CommunicationHandler<T extends  Message> {
    public abstract void sendMessage(T message);
    public abstract void addReceiveListener(ReceivedMessageListener<T> listener);
    public abstract void removeReceiveListener();
}
