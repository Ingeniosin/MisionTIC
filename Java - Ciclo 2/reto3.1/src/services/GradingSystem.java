package services;


import entity.Nota;
import entity.Student;

import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

public class GradingSystem {

    private final HashMap<Integer, Student> students;
    private final HashMap<Integer, String> classes;

    public GradingSystem(HashMap<Integer, Student> students, HashMap<Integer, String> classes) {
        this.students = new HashMap<>() {{
            students.forEach((integer, student) -> this.put(integer, student.getClone()));
        }};
        this.classes = classes;
    }

    public HashMap<Integer, Student> getStudents() {
        return students;
    }

    public HashMap<Integer, String> getClasses() {
        return classes;
    }

    public double stat1() {
        return students.values().stream().mapToDouble(e -> e.getNotas().isEmpty() ? 0 : e.getNotas().stream().mapToDouble(Nota::getNota).sum() / e.getNotas().size()).sum() / students.size();
    }

    public int stat2() {
        return students.values().stream().mapToInt(e -> (int) e.getNotas().stream().filter(n -> n.getNota() <= 30).count()).sum();
    }

    public String stat3() {
        return classes.values().stream().max(Comparator.comparingInt(o -> students.values().stream().mapToInt(e -> (int) e.getNotas().stream().filter(n -> n.getMateria().equals(o) && n.getNota() >= 60).count()).sum())).orElseThrow();
    }

    public String stat4() {
        Predicate<Nota> condition = n -> n.getMateria().equals("fisica");
        return students.values().stream().max(Comparator.comparingDouble(o -> o.getNotas().stream().filter(condition).mapToDouble(Nota::getNota).sum())).orElseThrow().getName();
    }
}
