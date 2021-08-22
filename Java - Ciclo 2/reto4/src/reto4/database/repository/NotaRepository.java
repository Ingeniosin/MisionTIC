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

public class NotaRepository implements Repository<Integer, Nota> {

    private Connection connection;
    private String table;
    private Repository<Integer, Materia> materiaRepository;
    private Repository<Integer, Estudiante> estudianteRepository;

    public NotaRepository(Connection connection, String table, Repository<Integer, Materia> materiaRepository, Repository<Integer, Estudiante> estudianteRepository) {
        this.connection = connection;
        this.table = table;
        this.materiaRepository = materiaRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @Override
    public Optional<Nota> getById(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table+" WHERE id=? LIMIT 1");
        preparedStatement.setInt(1, id);
        return Optional.ofNullable(extractNota(preparedStatement));
    }

    private Nota extractNota(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        Nota nota = null;
        while (resultSet.next()) {
            nota = new Nota(resultSet.getDouble(4), materiaRepository.getById(resultSet.getInt(3)).orElseThrow(), estudianteRepository.getById(resultSet.getInt(2)).orElseThrow());
        }
        preparedStatement.close();
        return nota;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "+table+" WHERE id=?");
        preparedStatement.setInt(1, id);
        return preparedStatement.execute();
    }

    @Override
    public Nota createOrUpdate(Nota newObject) throws SQLException {
        Objects.requireNonNull(newObject.getMateria());
        Objects.requireNonNull(newObject.getMateria().getId());
        Objects.requireNonNull(newObject.getEstudiante());
        Objects.requireNonNull(newObject.getEstudiante().getId());
        boolean hasId = newObject.getId() != null;
        Optional<Nota> searchById = hasId ? getById(newObject.getId()) : Optional.empty();
        if(searchById.isPresent()) return update(searchById.get());
        PreparedStatement createStatement = connection.prepareStatement("INSERT INTO "+table+" (nota, idMateria, idEstudiante"+(hasId ? ", id" : "")+") VALUES (?, ?, ?"+(hasId ? ", ?" : "")+")");
        createStatement.setDouble(1, newObject.getNota());
        createStatement.setInt(2, newObject.getMateria().getId());
        createStatement.setInt(3, newObject.getEstudiante().getId());
        if(hasId) createStatement.setInt(4, newObject.getId());
        createStatement.execute();
        createStatement.close();
        return extractNota(connection.prepareStatement("SELECT * FROM "+table+" WHERE rowid = (SELECT MAX(rowid) FROM "+table+");"));
    }

    @Override
    public Nota update(Nota putObject) throws SQLException {
        Objects.requireNonNull(putObject.getId());
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE "+table+" SET nota=?, idMateria=?, idEstudiante=? WHERE id = ?");
        preparedStatement.setDouble(1, putObject.getNota());
        preparedStatement.setInt(2, putObject.getMateria().getId());
        preparedStatement.setInt(3, putObject.getEstudiante().getId());
        preparedStatement.setInt(4, putObject.getId());
        preparedStatement.execute();
        return getById(putObject.getId()).orElseThrow();
    }

    @Override
    public List<Nota> getAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Nota> notas = new ArrayList<>();
        while (resultSet.next()) {
            notas.add(new Nota(resultSet.getInt(1), resultSet.getDouble(4), materiaRepository.getById(resultSet.getInt(3)).orElseThrow(), estudianteRepository.getById(resultSet.getInt(2)).orElseThrow()));
        }
        return notas;
    }

    public List<Nota> getNotasForStudent(Estudiante estudiante) throws SQLException {
        Objects.requireNonNull(estudiante.getId());
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+table+" WHERE idEstudiante=?");
        preparedStatement.setInt(1, estudiante.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Nota> notas = new ArrayList<>();
        while (resultSet.next()) {
            notas.add(new Nota(resultSet.getInt(1), resultSet.getDouble(4), materiaRepository.getById(resultSet.getInt(3)).orElseThrow(), estudiante));
        }
        return notas;
    }
}
