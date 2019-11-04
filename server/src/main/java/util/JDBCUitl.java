package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JDBCUitl {

    private JDBCUitl(){
    }

    private static Logger logger = LoggerFactory.getLogger(JDBCUitl.class);

    private static Connection connection;
    private static Properties properties;
    
    private static Properties readFile() {

        InputStream is = JDBCUitl.class.getClassLoader().getResourceAsStream("JDBC.Properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    
    static {
        properties = readFile();
        try {
            Class.forName(properties.getProperty("driverClass"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Registration driver failure");
        }
    }
    public static Connection getConnection() {
        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("connection failure");
        }
        return connection;
    }


    public static void JDBCClose(Connection connection, java.sql.Statement statement, ResultSet rs) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}