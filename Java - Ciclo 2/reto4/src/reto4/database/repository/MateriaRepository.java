package reto4.database.repository;

import reto4.entity.Estudiante;
import reto4.entity.Materia;
import reto4.entity.Nota;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MateriaRepository implements Repository<Integer, Materia>{

    private Connection connection;
    private String table;

    public MateriaRepository(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }

    @Override
    public Optional<Materia> getById(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table+" WHERE id=? LIMIT 1");
        preparedStatement.setInt(1, id);
        return Optional.ofNullable(extractMateria(preparedStatement));
    }

    private Materia extractMateria(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        Materia materia = null;
        while (resultSet.next()) {
            materia = new Materia(resultSet.getString(2), resultSet.getInt(1));
        }
        preparedStatement.close();
        return materia;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+table+" WHERE id=?");
        preparedStatement.setInt(1, id);
        return preparedStatement.execute();
    }

    public Optional<Materia> getByName(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table+" WHERE nombre=? LIMIT 1");
        preparedStatement.setString(1, name);
        return Optional.ofNullable(extractMateria(preparedStatement));
    }

    @Override
    public Materia createOrUpdate(Materia newObject) throws SQLException {

        boolean hasId = newObject.getId() != null;
        Optional<Materia> searchByName = getByName(newObject.getNombre()), searchById = hasId ? getById(newObject.getId()) : Optional.empty();
        if(searchById.isPresent() || searchByName.isPresent()) return update(searchById.orElseGet(searchByName::get));

        PreparedStatement createStatement = connection.prepareStatement("INSERT INTO "+table+" (nombre"+(hasId ? ", id" : "")+") VALUES (?"+(hasId ? ", ?" : "")+")");
        createStatement.setString(1, newObject.getNombre());
        if(hasId) createStatement.setInt(2, newObject.getId());
        createStatement.execute();
        createStatement.close();
        return extractMateria(connection.prepareStatement("SELECT * FROM "+table+" WHERE rowid = (SELECT MAX(rowid) FROM "+table+");"));
    }

    @Override
    public Materia update(Materia putObject) throws SQLException {
        Objects.requireNonNull(putObject.getId());
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+table+" SET nombre= ? WHERE id = ?");
        preparedStatement.setString(1, putObject.getNombre());
        preparedStatement.setInt(2, putObject.getId());
        preparedStatement.execute();
        return getById(putObject.getId()).orElseThrow();
    }

    @Override
    public List<Materia> getAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Materia> materias = new ArrayList<>();
        while (resultSet.next()) {
            materias.add(new Materia(resultSet.getString(2), resultSet.getInt(1)));
        }
        return materias;
    }
}
