package reto4;

import javafx.application.Application;
import javafx.stage.Stage;
import reto4.database.SQLite;
import reto4.database.repository.EstudianteRepository;
import reto4.database.repository.MateriaRepository;
import reto4.database.repository.NotaRepository;
import reto4.database.repository.Repository;
import reto4.entity.Estudiante;
import reto4.entity.Materia;
import reto4.entity.Nota;
import reto4.service.EstudianteService;
import reto4.service.MateriaService;
import reto4.service.NotaService;
import reto4.view.PrincipalScreen;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class App extends Application {

    private static SQLite sqLite;

    private static EstudianteRepository estudianteRepository;
    private static MateriaRepository materiaRepository;
    private static NotaRepository notaRepository;

    private static MateriaService materiaService;
    private static EstudianteService estudianteService;
    private static NotaService notaService;

    public static MateriaService getMateriaService() {
        return materiaService;
    }

    public static EstudianteService getEstudianteService() {
        return estudianteService;
    }

    public static NotaService getNotaService() {
        return notaService;
    }

    public static void main(String[] args) {
        sqLite = new SQLite("jdbc:sqlite:src/reto4/database.db");
        try {
            sqLite.open();
            sqLite.getConnection().prepareStatement("PRAGMA foreign_keys=ON").execute();
            estudianteRepository = new EstudianteRepository(sqLite.getConnection(), "Estudiantes");
            materiaRepository = new MateriaRepository(sqLite.getConnection(), "Materia");
            notaRepository = new NotaRepository(sqLite.getConnection(), "Notas", materiaRepository,  estudianteRepository);

            materiaService = new MateriaService(materiaRepository);
            notaService = new NotaService(notaRepository);
            estudianteService = new EstudianteService(estudianteRepository, notaService);

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(404);
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        try {
            materiaService.createIfNotExist("fisica", 1);
            materiaService.createIfNotExist("idiomas", 3);
            materiaService.createIfNotExist("quimica", 2);
            materiaService.createIfNotExist("matematicas", 4);

            estudianteService.createOrUpdate(new Estudiante("armando", "M", 1));
            estudianteService.createOrUpdate(new Estudiante("nicolas", "M", 2));
            estudianteService.createOrUpdate(new Estudiante("daniel", "M", 3));
            estudianteService.createOrUpdate(new Estudiante("maria", "F", 4));
            estudianteService.createOrUpdate(new Estudiante("marcela", "F", 5));
            estudianteService.createOrUpdate(new Estudiante("alexandra", "F", 6));

            registrarMaterias("fisica", "quimica", "idiomas", "matematicas");
            new Estudiante("armando", "m", 1);
            new Estudiante("nicolas", "m", 2);
            new Estudiante("daniel", "m", 3);
            new Estudiante("maria", "f", 4);
            new Estudiante("marcela", "f", 5);
            new Estudiante("alexandra", "f", 6);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        new PrincipalScreen().show();
    }

    private void registrarMaterias(String... materias) {
        AtomicInteger index = new AtomicInteger(1);
        Arrays.asList(materias).forEach(s -> new Materia(s, index.getAndIncrement()));
    }

}