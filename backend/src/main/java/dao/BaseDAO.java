package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Métodos genéricos CRUD
public interface BaseDAO<T, ID> {
    T insert(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(ID id) throws SQLException;
    Optional<T> findById(ID id) throws SQLException;
    List<T> findAll() throws SQLException;
}
