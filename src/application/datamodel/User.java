package application.datamodel;

import java.lang.reflect.Constructor;

public class User {
    private int _userId;
    private String _userName;
    private String _password;
    private boolean _active;

    User(int userId, String userName, String password, boolean active) {
        _userId = userId;
        _userName = userName;
        _password = password;
        _active = active;
    }

    private User() {
        _userId = -1;
        _userName = "Err";
        _password = "Err";
        _active = false;
    }

    public int getUserId() {
        return _userId;
    }
    public void setUserId(int userId) {
        _userId = userId;
    }

    public String getUserName() {
        return _userName;
    }
    public void setUserName(String userName) {
        _userName = userName;
    }

    public String getPassword() {
        return _password;
    }
    public void setPassword(String password) {
        _password = password;
    }

    public boolean getActive() {
        return _active;
    }
    public void setActive(boolean active) {
        _active = active;
    }

    public static User nullUser() {
        return new User();
    }
}
