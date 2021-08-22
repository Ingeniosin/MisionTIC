package reto4.service;

import reto4.App;
import reto4.database.repository.Repository;
import reto4.entity.Materia;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MateriaService {

    private final Repository<Integer, Materia> materiaRepository;

    public MateriaService(Repository<Integer, Materia> materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    public void remove(int id) throws SQLException {
        materiaRepository.deleteById(id);
    }

    public void createIfNotExist(String name, int id) throws SQLException {
        materiaRepository.createOrUpdate(new Materia(name, id));
    }

    public List<Materia> getMaterias() throws SQLException {
        return materiaRepository.getAll().stream().sorted(Comparator.comparingInt(Materia::getId)).collect(Collectors.toList());
    }

    public List<String> getMateriasCapitalize() throws SQLException {
        return getMaterias().stream().map(e -> {
            String nombre = e.getNombre();
            return nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
        }).collect(Collectors.toList());
    }

    public Optional<Materia> getById(int id) throws SQLException {
        return getMaterias().stream().filter(materia -> materia.getId() == id).findFirst();
    }

    public Optional<Materia> getByName(String nombre) throws SQLException {
        return getMaterias().stream().filter(materia -> materia.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

}
