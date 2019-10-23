package com.eis0.sms_library;

public interface SMSReceivedListener {
    /*
    * Implementare questo metodo e poi passare questo
    * oggetto a SMSLib.addOnReceiveListener per poter creare metodi di callback
    * che vengono chiamati quando viene ricevuto un messaggio.
    *
    * Riceve in input una String con il mittente del messaggio
    * e una String con il messaggio ricevuto.
    * */
    void SMSOnReceive(String from, String message);
}