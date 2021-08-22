package reto4.service;

import reto4.database.repository.NotaRepository;
import reto4.database.repository.Repository;
import reto4.entity.Estudiante;
import reto4.entity.Nota;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class NotaService {

    private NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public List<Nota> getNotasOfStudent(Estudiante estudiante) throws SQLException {
        return notaRepository.getNotasForStudent(estudiante);
    }

    public void remove(int id) throws SQLException {
        notaRepository.deleteById(id);
    }

    public List<Nota> getAll() throws SQLException {
        return notaRepository.getAll();
    }

    public void saveNota(Nota nota) throws SQLException {
        notaRepository.createOrUpdate(nota);
    }

    public void removeNotasOfStudent(Estudiante estudiante) throws SQLException {
        for (Nota nota : getNotasOfStudent(estudiante)) notaRepository.deleteById(nota.getId());
    }

    public void setNotasOfStudent(Estudiante estudiante) throws SQLException {
        Objects.requireNonNull(estudiante.getId());
        removeNotasOfStudent(estudiante);
        for (Nota nota : estudiante.getNotas()) {
            nota.setEstudiante(estudiante);
            saveNota(nota);
        }
    }

}
