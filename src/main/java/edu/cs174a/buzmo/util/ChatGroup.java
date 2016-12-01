package edu.cs174a.buzmo.util;


public class ChatGroup {
    private String groupName;
    private int duration;
    private String owner;

    public ChatGroup(String groupName, int duration, String owner) {
        this.groupName = groupName;
        this.duration = duration;
        this.owner = owner;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getDuration() {
        return duration;
    }

    public String getOwner() {
        return owner;
    }
}
