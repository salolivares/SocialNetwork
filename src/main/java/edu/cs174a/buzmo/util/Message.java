package edu.cs174a.buzmo.util;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class Message {
    private int mid;
    private String body;
    private String sender;
    private String timestamp;

    public Message(int mid, String sender, String body, String timestamp) {
        this.mid = mid;
        this.body = body;
        this.sender = sender;
        this.timestamp =timestamp;
    }

    public int getMid() {return this.mid;}
    public String getBody() {return this.body;}
    public String getSender() {return this.sender;}
    public String getTimestamp() {return this.timestamp;}

}
