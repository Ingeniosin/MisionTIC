package reto4.database;

import java.sql.SQLException;

public interface Database<C> {

    C getConnection();

    void open() throws SQLException;

    void close() throws SQLException;

    boolean isOpen();

}
