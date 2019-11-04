package util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class HikaricpUtils {

    public static DataSource dataSource;

    static {
        dataSource = getDataSource();
    }

    /**
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static DataSource getDataSource() {
        try {
            if (dataSource == null) {
                InputStream is = HikaricpUtils.class.getClassLoader().getResourceAsStream("hikaricp.properties");
                Properties props = new Properties();
                props.load(is);
                HikariConfig config = new HikariConfig(props);
                dataSource = new HikariDataSource(config);
            }
            return dataSource;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void releaseResources(Connection connection, Statement statement, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
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

        if (connection!= null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
