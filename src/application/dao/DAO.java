package application.dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> lookup(int id);
//    List<T> getAll();
    boolean insert(T t);
    boolean update(T t);
    boolean delete(T t);
}
