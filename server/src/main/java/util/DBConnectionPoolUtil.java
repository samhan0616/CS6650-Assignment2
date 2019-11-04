package util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnectionPoolUtil {
    private static volatile DBConnectionPoolUtil dbConnection;
    private static ComboPooledDataSource cpds;
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
        try{
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(properties.getProperty("driverClass"));
            cpds.setJdbcUrl(properties.getProperty("url"));
            cpds.setUser(properties.getProperty("user"));
            cpds.setPassword(properties.getProperty("password"));

            cpds.setInitialPoolSize(16);
            cpds.setMaxPoolSize(128);
            cpds.setAcquireIncrement(16);
            cpds.setIdleConnectionTestPeriod(60);
            cpds.setMaxIdleTime(30000);
            cpds.setTestConnectionOnCheckout(false);
            cpds.setTestConnectionOnCheckin(false);
            cpds.setAcquireRetryAttempts(30);
            cpds.setAcquireRetryDelay(1000);
            cpds.setBreakAfterAcquireFailure(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static void releaseAll(Connection conn, Statement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            conn = null;
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ps = null;
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            rs = null;
        }
    }

    /**
     *
     * @return
     */
    public static DBConnectionPoolUtil getInstance() {
        if (dbConnection == null) {
            synchronized (DBConnectionPoolUtil.class) {
                if (dbConnection == null) {
                    dbConnection = new DBConnectionPoolUtil();
                }
            }
        }
        return dbConnection;
    }

    /**
     *
     * @return
     */
    public final synchronized Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    /**
     * finalize()。
     *
     * @throws Throwable
     */
    protected void finalize() throws Throwable {
        DataSources.destroy(cpds);
        super.finalize();
    }
}
