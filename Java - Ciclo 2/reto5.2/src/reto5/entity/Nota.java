package reto5.entity;

public class Nota {
    private final double nota;
    private final String materia;

    public Nota(double nota, String materia) {
        this.nota = nota;
        this.materia = materia;
    }

    public double getNota() {
        return this.nota;
    }

    public String getMateria() {
        return this.materia;
    }

}
