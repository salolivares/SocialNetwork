package edu.cs174a.buzmo.util;


public class ChatGroup {
    private String groupName;
    private int duration;
    private String owner;
    private int memberStatus;

    public ChatGroup(String groupName, int duration, String owner, int memberStatus) {
        this.groupName = groupName;
        this.duration = duration;
        this.owner = owner;
        this.memberStatus = memberStatus;
    }

    public int getMemberStatus() {
        return memberStatus;
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
