package com.academy;



public class LinkList {
    private final long listID;
    private String name;

    public LinkList(long listID, String name) {
        this.listID = listID;
        this.name = name;
    }

    public long getListID() {
        return listID;
    }

    public String getName() {
        return name;
    }
}
