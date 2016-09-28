package com.academy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import static jdk.nashorn.internal.runtime.ScriptingFunctions.EXEC;

/**
 * Created by Emil Båth on 2016-09-28..
 */

@Component
public class DBRepository implements Repository {
    @Autowired
    DataSource dataSource;

    @Override
    public List<Link> getList(long listID) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("EXEC getList ?")) {
            ps.setLong(1, listID);
            ResultSet rs = ps.executeQuery();
            List<Link> linkList = new ArrayList<>();
            while(rs.next()){
                linkList.add(rsLink(rs));
            }
            return linkList;

        } catch (SQLException e) {
            throw new DBRepositoryException("Error in getList in DBRepository, could probably not execute query");
        }

    }

    private Link rsLink(ResultSet rs) throws SQLException {
       return new Link(rs.getLong("LinkID"), rs.getString("Url"),
               rs.getBoolean("IsFavourite"), rs.getLong("ListID"),
               rs.getString("Name"), rs.getString("Description"),
               rs.getTimestamp("AddedDate").toLocalDateTime(), rs.getBoolean("isDeleted") );
    }
}
