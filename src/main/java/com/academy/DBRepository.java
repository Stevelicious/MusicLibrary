package com.academy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class DBRepository implements Repository {

    @Autowired
    DataSource dataSource;

    @Override
    public List<Link> getLinks(long listID) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("EXEC getListOfLinks ?")) {
            ps.setLong(1, listID);
            ResultSet rs = ps.executeQuery();
            List<Link> linkList = new ArrayList<>();
            while (rs.next()) {
                linkList.add(rsLink(rs));
            }
            return linkList;

        } catch (SQLException e) {
            throw new DBRepositoryException("Error in getLinks in DBRepository, could probably not execute query");
        }
    }

    private Link rsLink(ResultSet rs) throws SQLException {
        return new Link(rs.getLong("LinkID"), rs.getString("Url"),
                rs.getBoolean("IsFavourite"), rs.getLong("ListID"),
                rs.getString("Name"), rs.getString("Description"),
                rs.getTimestamp("AddedDate").toLocalDateTime(), rs.getBoolean("isDeleted"));
    }

    @Override
    public List<LinkList> getLists(long userID) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("EXEC getLists ?")) {
            ps.setLong(1, userID);
            ResultSet rs = ps.executeQuery();
            List<LinkList> listOfLinkLists = new ArrayList<>();
            while (rs.next()) {
                listOfLinkLists.add(rsLinkList(rs));
            }
            return listOfLinkLists;
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in getList in DBRepository, could probably not execute query");
        }
    }

    private LinkList rsLinkList(ResultSet rs) throws SQLException {
        return new LinkList(rs.getLong("listID"), rs.getString("name"));
    }

    @Override
    public User getUser(String username) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM [User] WHERE UserName=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            User user = new User("emil", "emil", 2);
            while (rs.next()) {
                user = new User(rs.getString("UserName"),rs.getString("Name"),rs.getLong("UserID"));
            }
            return user;
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in getUser in DBRepository, could probably not execute query");
        }
    }

    public int db() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1+1")) {
            rs.next();
            int two = rs.getInt(1);
            return two;
        }
    }
}
