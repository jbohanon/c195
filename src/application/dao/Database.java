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
    public static Connection connectToDatabase() {
        MysqlDataSource ds = loadDataSource();
        Connection conn;
        try {
            conn = ds.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        return conn;
    }

    public static boolean testDbConn(Connection conn) {
        ResultSet rs;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from city");
            return rs.next();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean dbUpdate(String s) {
        try {
            Statement stmt = Main.dbConn.createStatement();
            System.out.println("Executing " + s);
            int res = stmt.executeUpdate(s);

            return res > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
//
//    protected static ResultSet dbQuery(String s) {
//        try {
//            Statement stmt = Main.dbConn.createStatement();
//            System.out.println("Executing " + s);
//            return stmt.executeQuery(s);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

}
