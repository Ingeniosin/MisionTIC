package reto4.view.pane;

import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import reto4.App;
import reto4.entity.Estudiante;
import reto4.entity.Materia;
import reto4.entity.Nota;
import reto4.service.EstudianteService;
import reto4.service.MateriaService;
import reto4.service.NotaService;
import reto4.view.Pane;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class QueryPane implements Pane {

    private final AnchorPane div;
    private final TextField txtNombre;
    private final TextArea txtresponse;
    private final Button btnConsultar, btnEliminar;
    private final ChoiceBox<String> selectMateria;
    private final Label labelMessage3;

    private Runnable currentRunnable;


    public QueryPane(AnchorPane div, TextField txtNombre, TextArea txtresponse, Button btnConsultar, Button btnEliminar, ChoiceBox<String> selectMateria3, Label labelMessage3) {
        this.div = div;
        this.txtNombre = txtNombre;
        this.txtresponse = txtresponse;
        this.btnConsultar = btnConsultar;
        this.btnEliminar = btnEliminar;
        this.selectMateria = selectMateria3;
        this.labelMessage3 = labelMessage3;
        onReset();
        EstudianteService estudianteService = App.getEstudianteService();
        MateriaService materiaService = App.getMateriaService();
        NotaService notaService = App.getNotaService();

        this.btnEliminar.addEventHandler(EventType.ROOT, e -> {
            if(e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                if(txtNombre.getText() != null && txtNombre.getText().isEmpty()) txtNombre.setText(null);
                setDisable(true);
                try {
                    String consultaPrevia = consultar(txtNombre, selectMateria);
                    StringBuilder consulta = new StringBuilder("PROCESO DE ELIMINACIÓN:\n");
                    String materiaSelected = selectMateria.getValue();
                    boolean hasName = txtNombre.getText() != null, hasMateriaSelected = !materiaSelected.equalsIgnoreCase("ninguna");
                    Estudiante estudiante = hasName ? estudianteService.getEstudiante(txtNombre.getText()) : null;
                    Materia materia = hasMateriaSelected ? materiaService.getByName(materiaSelected).orElseThrow() : null;
                    boolean hasEstudiante = estudiante != null, hasMateria = materia != null;

                    if(hasEstudiante && hasMateria) {
                        Predicate<Nota> notaPredicate = nota -> nota.getMateria().getNombre().equalsIgnoreCase(materiaSelected);
                        for (Nota n : estudiante.getNotas()) {
                            if (notaPredicate.test(n)) {
                                notaService.remove(n.getId());
                                consulta.append("  ● Se elimino la nota con id: ").append(n.getId()).append(" de la materia ").append(n.getMateria().getNombre()).append(" de el estudiante ").append(n.getEstudiante().getNombre());
                            }
                        }
                    } else if (hasEstudiante) {
                        estudianteService.remove(estudiante.getId());
                        consulta.append("  ● Se elimino el registro y notas totales del estudiante con id: ").append(estudiante.getId()).append(", nombre: ").append(estudiante.getNombre());
                    } else if(hasMateria) {
                        materiaService.remove(materia.getId());
                        consulta.append("  ● Se eliminaron todos los registros de la materia con id: ").append(materia.getId()).append(", nombre: ").append(materia.getNombre());
                    } else {
                        consulta.append("  ● No se encontraron registros relacionados a la solicitud.");
                        this.txtresponse.setText(consulta.toString());
                        setDisable(false);
                        return;
                    }
                    consulta.append("\n \nCONSULTA PREVIA A LA ELIMINACIÓN: \n \n").append(consultaPrevia);

                    resetSelector();
                    this.txtresponse.setText(consulta.toString());

                } catch (Exception ex) {
                    setMessage(false, "Ocurrió un error: "+ex.getMessage());
                }
                setDisable(false);
            }
        });

        this.btnConsultar.addEventHandler(EventType.ROOT, e -> {
            if(e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                if(txtNombre.getText() != null && txtNombre.getText().isEmpty()) txtNombre.setText(null);
                setDisable(true);
                try {
                    long time = System.currentTimeMillis();
                    this.txtresponse.setText(consultar(txtNombre, selectMateria));
                    setMessage(true, "Se realizo la consulta con exito en: "+(System.currentTimeMillis()-time)+" ms");
                } catch (Exception ex) {
                    setMessage(false, "Ocurrió un error: "+ex.getMessage());
                }
                setDisable(false);
            }
        });


    }

    private void resetSelector() throws SQLException {
        List<String> materias = App.getMateriaService().getMateriasCapitalize();
        materias.add("Ninguna");
        selectMateria.setItems(FXCollections.observableList(materias));
        selectMateria.setValue(materias.isEmpty() ? null : materias.get(materias.size()-1));
    }

    private String consultar(TextField txtNombre, ChoiceBox<String> selectMateria) throws Exception {
        EstudianteService estudianteService = App.getEstudianteService();
        String materiaSelected = selectMateria.getValue();
        StringBuilder stringBuilder = new StringBuilder();
        if(txtNombre.getText() == null && materiaSelected.equalsIgnoreCase("ninguna")) {
            stringBuilder.append("LISTA DE TODOS LOS ESTUDIANTES:").append("\n");
            estudianteService.getAll().forEach(e -> stringBuilder.append("  ● Nombre ").append(e.getNombre()).append("  |  Genero: ").append(e.getGenero()).append("  |  Id: ").append(e.getId()).append("\n"));
            stringBuilder.append("\n");
            return stringBuilder.toString();
        }
        boolean hasName = txtNombre.getText() != null, hasMateria = !materiaSelected.equalsIgnoreCase("ninguna");

        Estudiante estudiante = hasName ? estudianteService.getEstudiante(txtNombre.getText()) : null;
        String nombre = estudiante != null ? estudiante.getNombre() : txtNombre.getText();
        boolean hasEstudiante = estudiante != null;

        if(hasName && hasMateria) stringBuilder.append("REGISTROS ENCONTRADOS RELACIONADOS CON EL ESTUDIANTE ").append(nombre).append(" Y LA MATERIA ").append(materiaSelected);
        else if(hasName) stringBuilder.append("REGISTROS ENCONTRADOS RELACIONADOS CON EL ESTUDIANTE ").append(nombre);
        else if (hasMateria) stringBuilder.append("REGISTROS ENCONTRADOS RELACIONADOS LA ASIGNATURA ").append(materiaSelected);

        stringBuilder.append("\n \n");
        if(hasName) {
            stringBuilder.append("INFORMACIÓN DEL ESTUDIANTE:").append("\n");
            if(hasEstudiante)
                stringBuilder.append("  ● Nombre: ").append(nombre).append("\n").append("  ● Genero: ").append(estudiante.getGenero()).append("\n")
                        .append("  ● ID: ").append(estudiante.getId()).append("\n \n");
            else {
                stringBuilder.append("  ● No se encontró un registro de este estudiante");
                return stringBuilder.toString();
            }
        }

        Predicate<Nota> notaPredicate = nota -> !hasMateria || nota.getMateria().getNombre().equalsIgnoreCase(materiaSelected);
        List<Nota> notas = hasEstudiante ? estudiante.getNotas().stream().filter(notaPredicate).collect(Collectors.toList())  : App.getNotaService().getAll().stream().filter(notaPredicate).collect(Collectors.toList());

        stringBuilder.append("INFORMACIÓN CALIFICACIONES ").append(hasEstudiante ? "DE '"+estudiante.getNombre()+"' " : "DE TODOS LOS ESTUDIANTES").append(hasMateria ? " (" + materiaSelected + ")" : "").append(": ").append("\n");
        if(!notas.isEmpty())
            notas.forEach(nota -> {
                stringBuilder.append("  ● Calificacion: ").append(nota.getNota()).append("   |   Materia: ").append(nota.getMateria().getNombre());
                if(!hasEstudiante) stringBuilder.append("   |   Estudiante: ").append(nota.getEstudiante().getNombre());
                stringBuilder.append("\n");
            });
        else stringBuilder.append("  ● No se encontró ninguna nota");
        return stringBuilder.toString();
    }

    private void setMessage(boolean positive, String message) {
        labelMessage3.setText(message);
        labelMessage3.setTextFill(positive ? Color.GREEN : Color.RED);
        labelMessage3.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentRunnable = this;
                try {
                    Thread.sleep(1000 * 8);
                    if(currentRunnable != this) return;
                    labelMessage3.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onReset() {
        Pane.super.onReset();
        try {
            resetSelector();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public AnchorPane getDiv() {
        return div;
    }
}
