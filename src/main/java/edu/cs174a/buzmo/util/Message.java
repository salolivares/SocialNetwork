package edu.cs174a.buzmo.util;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class Message {
    private String body;
    private String sender;

    public Message(String sender, String body) {
        this.body = body;
        this.sender = sender;
    }

    public String getBody() {return this.body;}
    public String getSender() {return this.sender;}

}
