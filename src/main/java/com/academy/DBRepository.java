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
            List<Link> linkList = rsLink(rs);
            return linkList;
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in getLinks in DBRepository, could probably not execute query");
        }
    }

    private List<Link> rsLink(ResultSet rs) throws SQLException {
        List<Link> links = new ArrayList<>();
        while (rs.next()) {
            links.add(new Link(rs.getLong("LinkID"), rs.getString("Url"),
                    rs.getInt("IsFavorite") == 1, rs.getLong("List_ID"),
                    rs.getString("Name"), rs.getString("Description"),
                    rs.getTimestamp("AddedDate").toLocalDateTime(), rs.getInt("isDeleted") == 1));
        }
        return links;
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
        return new LinkList(rs.getLong("listID"), rs.getString("name"), rs.getBoolean("isPublic"));
    }

    @Override
    public User getUser(String username) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("EXEC getUser ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            User user = null;
            while (rs.next()) {
                user = new User(rs.getString("UserName"), rs.getString("Name"), rs.getLong("UserID"));
            }
            return user;
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in getUser in DBRepository, could probably not execute query");
        }
    }

    @Override
    public boolean isPasswordValid(String username, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("EXEC loginAttempt ?, ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            boolean res = false;
            if (rs.next()) {
                res = true;
            }
            return res;
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in isPasswordValid in DBRepository, could probably not execute query");
        }
    }
    @Override
    public void createNewList(long userID, String listName, String description) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("EXEC AddList ?, ?, ?")) {
            ps.setLong(1,userID);
            ps.setString(2, listName);
            ps.setString(3, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in createNewList in DBRepository, could probably not execute query");
        }
    }

    @Override
    public List<User> getUsers(String searchString) {
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("EXEC getUsers ?")) {
            ps.setString(1,searchString);
            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                String username = rs.getString("UserName");
                User user = getUser(username);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in getUsers in DBRepository, could probably not execute query");
        }
    }

    @Override
    public void addUser(String name, String username, String password) {
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("EXEC CreateUser ?, ?, ?")) {
            ps.setString(1,name);
            ps.setString(2,username);
            ps.setString(3,password);
            ps.executeUpdate();
        }catch (SQLException e) {
            throw new DBRepositoryException("Error in addUser in DBRepository, could probably not execute query");
        }
    }

    @Override
    public boolean validUserName(String username) {
        try (Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("EXEC validUserName ?")) {
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            boolean res = true;
            if (rs.next()) {
                res = false;
            }
            return res;
        } catch (SQLException e) {
            throw new DBRepositoryException("Error in validUserName in DBRepository, could probably not execute query");
        }
    }
    public void addLink(Link link) {
        try(Connection conn=dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("EXEC AddLink ?, ?, ?, ?, ?")) {
            int favorite = link.favourite ? 1 : 0;
            String[] partUrl=link.url.split("=");
            String url=partUrl[1]; //för att få slutet på url (unikt för varje video)

            ps.setLong(1, link.listID);
            ps.setString(2, url);
            ps.setString(3, link.linkName);
            ps.setString(4, link.description);
            ps.setInt(5, favorite);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new DBRepositoryException("Error in addLink in DBRepository, could probably not execute query");
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
