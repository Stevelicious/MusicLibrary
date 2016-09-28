package com.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Administrator on 2016-09-28.
 */
public class Link {
    public long linkID;
    public String url;
    public boolean favourite;
    public long listID;
    public String linkName;
    public String description;
    public LocalDateTime addDate;
    public boolean isDeleted;

    public Link(long linkID, String url, boolean favourite, long listID, String linkName, String description, LocalDateTime addDate, boolean isDeleted) {
        this.linkID = linkID;
        this.url = url;
        this.favourite = favourite;
        this.listID = listID;
        this.linkName = linkName;
        this.description = description;
        this.addDate = addDate;
        this.isDeleted = isDeleted;
    }
}
