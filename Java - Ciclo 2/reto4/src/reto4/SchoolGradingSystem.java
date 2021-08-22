package reto4;

import reto4.entity.Estudiante;
import reto4.entity.Materia;
import reto4.entity.Nota;
import reto4.service.EstudianteService;
import reto4.service.MateriaService;

import java.lang.ref.PhantomReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SchoolGradingSystem extends GradingSystem {

    public SchoolGradingSystem(List<Estudiante> estudiantes, List<Materia> materias) {
        super(estudiantes, materias);
    }

    public void loadData(List<String> lines) throws NullPointerException {
        int index = 1;
        for (String line : lines) {
            try {
                loadExportedData(Arrays.stream(line.split(" ")).mapToDouble(Double::parseDouble).boxed().collect(Collectors.toList()));
            } catch (Exception e) {
                throw new NullPointerException(""+index);
            }
            index++;
        }
    }

    public void loadDataByScanner()  throws NullPointerException {
        Scanner scanner = new Scanner(System.in);
        int lineCount = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < lineCount; i++) loadExportedData(Arrays.stream(scanner.nextLine().split(" ")).mapToDouble(Double::parseDouble).boxed().collect(Collectors.toList()));
    }

    private void loadExportedData(List<Double> data) throws NullPointerException{
        int nombreId = (int) Math.round(data.get(0)), generoId = (int) Math.round(data.get(1)), materiaId = (int) Math.round(data.get(2));
        double calificacion = data.get(3);
        Estudiante searchById = getEstudiantes().stream().filter(estudiante -> estudiante.getId() == nombreId).findFirst().orElseThrow();
        searchById.getNotas().add(new Nota(calificacion, getMaterias().stream().filter(m -> m.getId() == materiaId).findFirst().orElseThrow()));
    }

}
