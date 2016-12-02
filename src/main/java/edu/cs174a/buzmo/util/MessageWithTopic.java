package edu.cs174a.buzmo.util;

/**
 * Created by jordannguyen on 12/1/16.
 */
public class MessageWithTopic extends Message {
    private String topic;
    public MessageWithTopic(int mid, String sender, String body, String timestamp, String topic) {
        super(mid, sender, body, timestamp);
        this.topic = topic;
    }

    public String getTopic() {return this.topic;}
}
