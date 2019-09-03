package com.cthesign.twitterapp.entity;

public class Tweet {

    private String userName;

    private String text;

    public Tweet() {
    }

    public Tweet(String name, String text) {
        this.userName = name;
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
