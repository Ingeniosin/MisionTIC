package reto4.service;

import reto4.database.repository.EstudianteRepository;
import reto4.entity.Estudiante;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final NotaService notaService;

    public EstudianteService(EstudianteRepository estudianteRepository, NotaService notaService) {
        this.estudianteRepository = estudianteRepository;
        this.notaService = notaService;
    }

    public void remove(int id) throws SQLException {
        estudianteRepository.deleteById(id);
    }

    public Estudiante getEstudiante(int id) throws SQLException {
        Estudiante estudiante = estudianteRepository.getById(id).orElse(null);
        if (estudiante == null) return null;
        estudiante.setNotas(notaService.getNotasOfStudent(estudiante));
        return estudiante;
    }

    public Estudiante getEstudiante(String name) throws SQLException {
        Estudiante estudiante = estudianteRepository.getByName(name).orElse(null);
        if (estudiante == null) return null;
        estudiante.setNotas(notaService.getNotasOfStudent(estudiante));
        return estudiante;
    }

    public void createOrUpdate(Estudiante estudiante) throws SQLException {
        Objects.requireNonNull(estudiante.getNotas(), "Las notas no pueden ser nulas");
        estudiante.setId(estudianteRepository.createOrUpdate(estudiante).getId());
        if (!estudiante.getNotas().isEmpty()) {
            notaService.setNotasOfStudent(estudiante);
        }

    }

    public List<Estudiante> getAll() throws SQLException {
        return estudianteRepository.getAll();
    }


}
