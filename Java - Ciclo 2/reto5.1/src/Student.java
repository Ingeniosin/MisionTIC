import java.util.ArrayList;
import java.util.List;

public class Student {
    private final String name;
    private final String gender;
    private final List<Nota> notas = new ArrayList<>();

    public Student(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return this.name;
    }

    public String getGender() {
        return this.gender;
    }

    public List<Nota> getNotas() {
        return this.notas;
    }

}