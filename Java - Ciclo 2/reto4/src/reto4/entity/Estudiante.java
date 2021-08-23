package reto4.entity;

import java.util.ArrayList;
import java.util.List;

public class Estudiante {

    private final String nombre;
    private final String genero;
    private Integer id;
    private List<Nota> notas = new ArrayList<>();

    public Estudiante(String nombre, String genero) {
        this.nombre = nombre;
        this.genero = genero;
    }

    public Estudiante(String nombre, String genero, int id) {
        this.nombre = nombre;
        this.genero = genero;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getId() {
        return id;
    }

    public Estudiante setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public Estudiante setNotas(List<Nota> notas) {
        this.notas = notas;
        return this;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", id=" + id +
                '}';
    }
}
