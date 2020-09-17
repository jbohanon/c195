package application.dao;

import java.util.Optional;

public interface DAO<T> {
    Optional<T> lookup(int id);
    boolean insert(T t);
    boolean update(T t);
    boolean delete(T t);
    T GetOptionalOrThrow(Optional<T> optionalT) throws Exception;
}
