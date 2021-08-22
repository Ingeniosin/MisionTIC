import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final HashMap<String, Integer> materias = new HashMap<>();

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        final String[] generos = new String[]{"m", "f"}, nombres = new String[]{"armando", "nicolas", "daniel", "maria", "marcela", "alexandra"}, materias = new String[]{"fisica", "quimica", "idiomas"};
        for (String materia : materias) Main.materias.put(materia, 0);
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        int i1 = scanner.nextInt();
        scanner.nextLine();
        for (int i = 1; i <= i1; i++) {
            List<String> line = new ArrayList<>();
            for (String s : scanner.nextLine().split(" ")) line.add(s.replace(".0", ""));
            estudiantes.add(new Estudiante(nombres[Integer.parseInt(line.get(0)) - 1], generos[Integer.parseInt(line.get(1))], materias[Integer.parseInt(line.get(2)) - 1], Double.parseDouble(line.get(3))));
        }
        System.out.printf("%.2f%n", estudiantes.stream().mapToDouble(value -> value.nota).sum() / estudiantes.size());
        System.out.println(estudiantes.stream().filter(value -> value.nota < 30).count());
        System.out.println(Main.materias.keySet().stream().filter(s -> Main.materias.get(s).equals(Collections.max(Main.materias.values()))).collect(Collectors.toList()).get(0));
        Comparator<Estudiante> comparator = Comparator.comparingDouble(o -> o.nota);
        System.out.println(estudiantes.stream().filter(estudiante -> estudiante.materia.equals("fisica")).sorted(comparator.reversed()).collect(Collectors.toList()).get(0).nombre);
    }

    public static class Estudiante {
        private final String nombre, genero, materia;
        private final double nota;

        public Estudiante(String nombre, String genero, String materia, double nota) {
            this.nombre = nombre;
            this.genero = genero;
            this.materia = materia;
            this.nota = nota;
            if (this.nota > 60)
                materias.put(this.materia, materias.get(this.materia) + 1);
        }
    }
}