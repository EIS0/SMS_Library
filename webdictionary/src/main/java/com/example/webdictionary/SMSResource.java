package com.example.webdictionary;


public class SMSResource implements Resource<String> {
    private String resource;

    public SMSResource(String resource){
        this.resource = resource;
    }

    /**
     * Returns the Resource data (String in this case)
     */
    public String getResource(){
        return resource;
    }

    /**
     * An SMS Resource (meaning a resource that can be sent through an SMS),
     * has to be serialized as a String to be able to be sent as an SMS
     * @return A String definition of an SMSResource, to identify this object
     */
    public String serialize(){
        return resource;
    }
}
