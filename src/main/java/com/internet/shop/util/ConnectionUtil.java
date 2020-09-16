package com.internet.shop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.jdbc.driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find MySQL Driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user","root");
        connectionProps.put("password", 12345678);
        String url = "jdbc:mysql://localhost:3306/internet_shop?useSSL=false";

        conn = DriverManager.getConnection(url, connectionProps);
        return conn;
    }
}
