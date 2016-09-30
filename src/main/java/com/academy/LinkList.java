package com.academy;



public class LinkList {
    private final long listID;
    private String name;
    private boolean isPublic;

    public LinkList(long listID, String name, boolean isPublic) {
        this.listID = listID;
        this.name = name;
        this.isPublic = isPublic;
    }

    public long getListID() {
        return listID;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPublic() {
        return isPublic;
    }
}
