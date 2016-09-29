package com.academy;

/**
 * Created by Emil Båth on 2016-09-28.
 */
public class User {
    String username;
    String name;
    long userID;

    public User(String username, String name, long userID) {
        this.username = username;
        this.name = name;
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public long getUserID() {
        return userID;
    }
}
