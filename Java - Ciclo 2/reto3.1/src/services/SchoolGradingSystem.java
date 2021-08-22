package services;

import entity.Nota;
import entity.Student;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SchoolGradingSystem extends GradingSystem {

    private static final HashMap<Integer, String> staticClasses = new HashMap<>() {
        {
            put(1, "fisica");
            put(2, "quimica");
            put(3, "idiomas");
        }
    };
    private static final HashMap<Integer, Student> staticStudents = new HashMap<>() {
        {
            put(1, new Student("armando", "m"));
            put(2, new Student("nicolas", "m"));
            put(3, new Student("daniel", "m"));
            put(4, new Student("maria", "f"));
            put(5, new Student("marcela", "f"));
            put(6, new Student("alexandra", "f"));
        }
    };

    public SchoolGradingSystem(HashMap<Integer, Student> students, HashMap<Integer, String> classes) {
        super(students, classes);
    }

    public static HashMap<Integer, Student> getStaticStudents() {
        return staticStudents;
    }

    public static HashMap<Integer, String> getStaticClasses() {
        return staticClasses;
    }

    public static void loadFromConsole() {
        final Scanner scanner = new Scanner(System.in);
        int lines = scanner.nextInt();
        scanner.nextLine();
        SchoolGradingSystem schoolGradingSystem = new SchoolGradingSystem(staticStudents, staticClasses);
        schoolGradingSystem.loadData(IntStream.range(0, lines).mapToObj(o -> scanner.nextLine()).collect(Collectors.toList()));
        scanner.close();
        System.out.printf("%.2f%n", schoolGradingSystem.stat1());
        System.out.println(schoolGradingSystem.stat2());
        System.out.println(schoolGradingSystem.stat3());
        System.out.println(schoolGradingSystem.stat4());
    }

    public void loadData(List<String> lines) {
        for (String line : lines) {
            List<Double> data = Arrays.stream(line.split(" ")).map(Double::parseDouble).collect(Collectors.toList());
            int studentId = (int) Math.round(data.get(0)), classId = (int) Math.round(data.get(2));
            double note = data.get(3);
            getStudents().get(studentId).getNotas().add(new Nota(note, getClasses().get(classId)));
        }
    }
}
