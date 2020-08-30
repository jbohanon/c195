package application.dao;

import application.datamodel.User;

import java.util.List;
import java.util.Optional;

public class UserDAO implements DAO<User> {
    @Override
    public Optional<User> lookup(int id) {
        return Optional.empty();
    }

    @Override
    public boolean insert(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }
}
