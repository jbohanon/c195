package application.datamodel;

public class User {
    private int _userId;
    private String _userName;
    private String _password;
    private boolean _active;

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
}
