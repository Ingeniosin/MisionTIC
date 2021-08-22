package reto4;

import reto4.entity.Estudiante;
import reto4.entity.Materia;
import reto4.entity.Nota;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GradingSystem {

    private List<Estudiante> estudiantes;
    private List<Materia> materias;

    public GradingSystem(List<Estudiante> estudiantes, List<Materia> materias) {
        this.estudiantes = estudiantes;
        this.materias = materias;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public double stat1() {
        return estudiantes.stream().mapToDouble(e -> e.getNotas().isEmpty() ? 0 : e.getNotas().stream().mapToDouble(Nota::getNota).sum() / e.getNotas().size()).sum() / estudiantes.size();
    }

    public int stat2() {
        return estudiantes.stream().mapToInt(estudiante -> estudiante.getNotas().stream().filter(nota -> nota.getNota() <= 30).mapToInt(m -> 1).sum()).sum();
    }

    public String stat3()  {
        Map<Materia, DoubleSummaryStatistics> collect = materias.stream().collect(Collectors.groupingBy(materia -> materia,Collectors.summarizingDouble(value -> estudiantes.stream().mapToDouble(value1 -> value1.getNotas().stream().filter(nota -> nota.getMateria().equals(value)).mapToDouble(Nota::getNota).sum()).sum())));
        return collect.entrySet().stream().max(Map.Entry.comparingByValue(Comparator.comparingDouble(DoubleSummaryStatistics::getSum))).orElseThrow().getKey().getNombre();
    }

    public String stat4() {
        Predicate<Nota> fisicaFilter = nota -> nota.getMateria().getNombre().equals("fisica");
        return estudiantes.stream().filter(e -> e.getNotas().stream().anyMatch(fisicaFilter)).max(Comparator.comparingDouble(o -> o.getNotas().stream().filter(fisicaFilter).mapToDouble(Nota::getNota).sum())).orElseThrow().getNombre();
    }



}
