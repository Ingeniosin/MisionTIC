import java.util.*;
import java.util.stream.Collectors;

public class SchoolGradingSystem {
    final String[]
            generos = new String[]{"m", "f"},
            nombres = new String[]{"armando", "nicolas", "daniel", "maria", "marcela", "alexandra"},
            materias = new String[]{"fisica", "quimica", "idiomas"};
    private final HashMap<String, Integer> materiasAltas;
    private final List<Estudiante> estudiantes;

    public SchoolGradingSystem() { //Initializer
        materiasAltas = new HashMap<>();
        estudiantes = new ArrayList<>();
        for (String materia : materias) materiasAltas.put(materia, 0);
    }

    public void readData() {
        final Scanner scanner = new Scanner(System.in);
        int howStudents = scanner.nextInt();
        scanner.nextLine();
        for (int i = 1; i <= howStudents; i++) {
            List<String> line = Arrays.stream(scanner.nextLine().split(" ")).map(s -> s.replace(".0", "")).collect(Collectors.toList());
            double nota = Double.parseDouble(line.get(3));
            String nombre = nombres[Integer.parseInt(line.get(0)) - 1], genero = generos[Integer.parseInt(line.get(1))], materia = materias[Integer.parseInt(line.get(2)) - 1];
            if (nota > 60)
                materiasAltas.put(materia, materiasAltas.get(materia) + 1);
            estudiantes.add(new Estudiante(nombre, genero, materia, nota));
        }
    }

    public double question1() {
        return estudiantes.stream().mapToDouble(value -> value.nota).sum() / estudiantes.size();
    }

    public long question2() {
        return estudiantes.stream().filter(value -> value.nota < 30).count();
    }

    public String question3() {
        return materiasAltas.keySet().stream().filter(s -> materiasAltas.get(s).equals(Collections.max(materiasAltas.values()))).collect(Collectors.toList()).get(0);
    }

    public String question4() {
        Comparator<Estudiante> comparator = Comparator.comparingDouble(o -> o.nota);
        return estudiantes.stream().filter(estudiante -> estudiante.materia.equals("fisica")).sorted(comparator.reversed()).collect(Collectors.toList()).get(0).nombre;
    }

    public static class Estudiante {
        private final String nombre, genero, materia;
        private final double nota;

        public Estudiante(String nombre, String genero, String materia, double nota) {
            this.nombre = nombre;
            this.genero = genero;
            this.materia = materia;
            this.nota = nota;
        }
    }
}