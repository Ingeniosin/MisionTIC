package reto4.database.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public interface Repository<K, V> {

    Optional<V> getById(K id) throws SQLException;

    boolean deleteById(K id) throws SQLException;

    V createOrUpdate(V newObject) throws SQLException;

    V update(V putObject) throws SQLException;

    List<V> getAll() throws SQLException;

    default List<V> getByCriteria(Predicate<V> predicate) throws SQLException {
        return getAll().stream().filter(predicate).collect(Collectors.toList());
    }

    default Optional<V> getByCriteriaFirst(Predicate<V> predicate) throws SQLException {
        return getAll().stream().filter(predicate).findFirst();
    }

}
