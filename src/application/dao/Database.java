package application.dao;

import application.Main;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.jdbc.result.ResultSetImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {

    private static Connection dbConn = null;

    private static MysqlDataSource loadDataSource() {
        MysqlDataSource mysqlDS = null;
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/resources/dbinfo");
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            System.out.println("URL: " + props.getProperty("URL") + "\nUSER: " + props.getProperty("USER") + "\nPASS: " + props.getProperty("PASS"));
            mysqlDS.setURL(props.getProperty("URL"));
            mysqlDS.setUser(props.getProperty("USER"));
            mysqlDS.setPassword(props.getProperty("PASS"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return mysqlDS;
    }

    public static Connection getConnection() {
        return dbConn;
    }

    public static void connect() throws Exception {
        makeAndHoldConnection();
    }

    private static void makeAndHoldConnection() throws Exception {
        MysqlDataSource ds = loadDataSource();

        dbConn = ds.getConnection();

        while (true) {
            if (dbConn.isClosed()) {
                dbConn = ds.getConnection();
                Thread.sleep(30000);
            }
        }
    }

    public static boolean dbUpdate(String s) {
        try {
            Statement stmt = Database.getConnection().createStatement();
            System.out.println("Executing " + s);
            int res = stmt.executeUpdate(s);

            return res > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
