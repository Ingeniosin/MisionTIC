package reto4.database.repository;

import reto4.entity.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EstudianteRepository implements Repository<Integer, Estudiante>{

    private Connection connection;
    private String table;

    public EstudianteRepository(Connection connection, String table) {
        this.connection = connection;
        this.table = table;
    }


    @Override
    public Optional<Estudiante> getById(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table+" WHERE id=? LIMIT 1");
        preparedStatement.setInt(1, id);
        return Optional.ofNullable(extractEstudiante(preparedStatement));
    }

    private Estudiante extractEstudiante(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        Estudiante estudiante = null;
        while (resultSet.next()) estudiante = new Estudiante(resultSet.getString(2), resultSet.getString(3), resultSet.getInt(1));
        preparedStatement.close();
        return estudiante;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+table+" WHERE id=?");
        preparedStatement.setInt(1, id);
        return preparedStatement.execute();
    }

    public Optional<Estudiante> getByName(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table+" WHERE nombre=? LIMIT 1");
        preparedStatement.setString(1, name);
        return Optional.ofNullable(extractEstudiante(preparedStatement));
    }

    @Override
    public Estudiante createOrUpdate(Estudiante newObject) throws SQLException {
        boolean hasId = newObject.getId() != null;
        Optional<Estudiante> searchByName = getByName(newObject.getNombre()), searchById = hasId ? getById(newObject.getId()) : Optional.empty();
        if(searchById.isPresent() || searchByName.isPresent()) return update(newObject.setId(searchById.orElseGet(searchByName::get).getId()));
        PreparedStatement createStatement = connection.prepareStatement("INSERT INTO "+table+" (nombre, genero"+(hasId ? ", id" : "")+") VALUES (?, ?"+(hasId ? ", ?" : "")+")");
        createStatement.setString(1, newObject.getNombre());
        createStatement.setString(2, newObject.getGenero());
        if(hasId) createStatement.setInt(3, newObject.getId());
        createStatement.execute();
        createStatement.close();
        return extractEstudiante(connection.prepareStatement("SELECT * FROM " + table + " WHERE rowid = (SELECT MAX(rowid) FROM " + table + ");"));
    }

    @Override
    public Estudiante update(Estudiante putObject) throws SQLException {
        Objects.requireNonNull(putObject.getId());
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+table+" SET nombre= ?, genero = ? WHERE id = ?");
        preparedStatement.setString(1, putObject.getNombre());
        preparedStatement.setString(2, putObject.getGenero());
        preparedStatement.setInt(3, putObject.getId());
        preparedStatement.execute();
        return getById(putObject.getId()).orElseThrow();
    }

    @Override
    public List<Estudiante> getAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Estudiante> estudiantes = new ArrayList<>();
        while (resultSet.next()) {
            estudiantes.add(new Estudiante(resultSet.getString(2), resultSet.getString(3), resultSet.getInt(1)));
        }
        return estudiantes;
    }
}
