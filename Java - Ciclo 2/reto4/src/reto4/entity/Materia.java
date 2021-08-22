package reto4.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Materia {

    private final String nombre;
    private Integer id;

    public Materia(String nombre) {
        this.nombre = nombre;
    }



    public Materia(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getId() {
        return id;
    }
}
