package com.academy;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * Created by Emil Båth on 2016-09-28.
 */
public class DBRepository implements Repository {
    DataSource dataSource;
    Connection conn = dataSource.getConnection();

    public DBRepository() throws SQLException {
    }
}
