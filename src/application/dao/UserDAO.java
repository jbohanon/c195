package application.dao;

import application.Main;
import application.datamodel.Appointment;
import application.datamodel.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class UserDAO implements DAO<User> {
    @Override
    public Optional<User> lookup(int id) {
        return lookupHelper("SELECT userId, userName, password, active FROM user WHERE userId=" + id);
    }

    public Optional<User> lookup(String userName) {
        return lookupHelper("SELECT userId, userName, password, active FROM user WHERE userName=" + userName);
    }

    private Optional<User> lookupHelper(String s) {
        try{
            Statement stmt = Main.dbConn.createStatement();
            System.out.println("Executing " + s);
            ResultSet rs = stmt.executeQuery(s);

            if(rs.next()) {
                User u = new User(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getBoolean("active")
                );
                System.out.println("Found user with userId " + u.getUserId() + " and userName " + u.getUserName());
                return Optional.of(u);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();

    }

    @Override
    // Not implemented
    public boolean insert(User user) {
        return false;
    }

    @Override
    // Not implemented
    public boolean update(User user) {
        return false;
    }

    @Override
    // Not implemented
    public boolean delete(User user) {
        return false;
    }

    @Override
    public User GetOptionalOrThrow(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new RuntimeException("No user contained in Optional<User>"));
    }
}
