package com.example.webdictionary;


public class SMSResource implements Resource<Object> {
    private Object resource;

    public SMSResource(Object resource){
        this.resource = resource;
    }

    /**
     * Returns the Resource data (String in this case)
     */
    public Object getResource(){
        return resource;
    }

    /**
     * An SMS Resource (meaning a resource that can be sent through an SMS),
     * has to be serialized as a String to be able to be sent as an SMS
     * @return A String definition of an SMSResource, to identify this object
     */
    public Object serialize(){
        return resource;
    }
}
