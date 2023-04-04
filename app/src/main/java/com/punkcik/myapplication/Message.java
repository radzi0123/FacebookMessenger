package com.punkcik.myapplication;

public class Message {
    private String title;
    private String text;
    private String groupName;

    public Message(String title, String text, String groupName) {
        this.title = title;
        this.text = text;
        this.groupName = groupName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

