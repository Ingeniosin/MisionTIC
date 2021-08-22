package reto4.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generos {

    private static final List<String> generos = new ArrayList<>(Arrays.asList("F", "M"));

    public static List<String> getGeneros() {
        return generos;
    }
}
