/*
 * Author MD. Sakibur Reza
 */

package Database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class OracleConnectionPool {
    private static final String DB_driver = "Driver.class.name";
    private static final String DB_connection = "URL";
    private static final String user = "user.name";
    private static final String password = "user.password";
    private static ComboPooledDataSource dataSource;

    static
    {
        try {
            dataSource = new ComboPooledDataSource();
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/Database/Database.properties"));

            dataSource.setDriverClass(properties.getProperty(DB_driver));
            dataSource.setJdbcUrl(properties.getProperty(DB_connection));
            dataSource.setUser(properties.getProperty(user));
            dataSource.setPassword(properties.getProperty(password));

            dataSource.setMinPoolSize(3);
            dataSource.setMaxPoolSize(300);
            dataSource.setAcquireIncrement(10);

            dataSource.setTestConnectionOnCheckout(true);
        }
        catch (PropertyVetoException e) {
            System.out.println("Driver class not found :: " + e);
        }
        catch (FileNotFoundException e) {
            System.out.println("Database.properties file is has not found :: " + e);;
        }
        catch (IOException e) {
            System.out.println("Database.properties is not loaded :: " + e);;
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
