package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    //init database constants
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/student";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";
    private static final String MAX_POOL = "250";
    private static ConnectionFactory instance;

    // init connection object
    private Connection connection;

    //init properties object
    private Properties properties;

    public ConnectionFactory() {
        super();
    }

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    //connect database
    public Connection connect() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
                System.out.println("Successfully connected to the PostgreSQL Server ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    //disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Successfully disconected to the PostgreSQL Server");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
