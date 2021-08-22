package reto4.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements Database<Connection>{

    private final String DATABASE_URL;
    private Connection connection;

    public SQLite(String DATABASE_URL) {
        this.DATABASE_URL = DATABASE_URL;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void open() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL);

    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public boolean isOpen() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
