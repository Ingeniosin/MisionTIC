package reto4.entity;

public class Nota {

    private final double nota;
    private final Materia materia;
    private Integer id;
    private Estudiante estudiante;


    public Nota(double nota, Materia materia) {
        this.nota = nota;
        this.materia = materia;
    }

    public Nota(double nota, Materia materia, Estudiante estudiante) {
        this.nota = nota;
        this.materia = materia;
        this.estudiante = estudiante;
    }

    public Nota(Integer id, double nota, Materia materia, Estudiante estudiante) {
        this.id = id;
        this.nota = nota;
        this.materia = materia;
        this.estudiante = estudiante;
    }

    public Integer getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public double getNota() {
        return nota;
    }

    public Materia getMateria() {
        return materia;
    }
}
