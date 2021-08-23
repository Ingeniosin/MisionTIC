package reto4.view.pane;

import javafx.collections.FXCollections;
import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import reto4.App;
import reto4.entity.Estudiante;
import reto4.entity.Generos;
import reto4.entity.Nota;
import reto4.view.Pane;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class DataPane implements Pane {

    private final AnchorPane div;
    private final TextField txtName, txtNota;
    private final ChoiceBox<String> selectMateria, selectGenero;
    private final Button btnGuardar;
    private final Label txtMessage;

    private Runnable currentRunnable;

    public DataPane(AnchorPane div, TextField txtName, TextField txtNota, ChoiceBox<String> selectGenero, ChoiceBox<String> selectMateria, Button btnGuardar, Label txtMessage) {
        this.div = div;
        this.txtName = txtName;
        this.txtNota = txtNota;
        this.selectGenero = selectGenero;
        this.selectMateria = selectMateria;
        this.btnGuardar = btnGuardar;
        Objects.requireNonNull(txtMessage);
        this.txtMessage = txtMessage;
        this.onReset();

        btnGuardar.addEventHandler(EventType.ROOT, e -> {
            if (e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                setDisable(true);
                txtMessage.setVisible(false);
                if (txtName.getText() != null && txtName.getText().isEmpty()) txtName.setText(null);
                if (txtNota.getText() != null && txtNota.getText().isEmpty()) txtNota.setText(null);
                try {
                    Objects.requireNonNull(txtName.getText(), "Campo nombre invalido");
                    Objects.requireNonNull(txtNota.getText(), "Campo nota invalido");
                    double nota = Double.parseDouble(txtNota.getText());
                    Estudiante estudianteNuevo = new Estudiante(txtName.getText().trim(), selectGenero.getValue()), estudianteAntiguo = App.getEstudianteService().getEstudiante(estudianteNuevo.getNombre());
                    if (estudianteAntiguo != null) estudianteNuevo.getNotas().addAll(estudianteAntiguo.getNotas());
                    estudianteNuevo.getNotas().add(new Nota(nota, App.getMateriaService().getByName(selectMateria.getValue()).orElseThrow()));
                    App.getEstudianteService().createOrUpdate(estudianteNuevo);
                    setMessage(true, estudianteAntiguo != null ? "El registro del usuario de nombre " + estudianteAntiguo.getNombre() + " ya existía, se encontró y se añadió la nota correspondiente" : "El usuario " + estudianteNuevo.getNombre() + " se creo con exito!");
                } catch (NullPointerException | SQLException ex) {
                    setMessage(false, "Ocurrió un error: " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    setMessage(false, "Error al convertir los datos, el campo nota es invalido: " + ex.getMessage());
                } catch (Exception ex) {
                    setMessage(false, "Error, los datos son invalidos " + ex.getMessage());
                }
                setDisable(false);
            }
        });

    }

    @Override
    public void onResume() {
        try {
            List<String> materias = App.getMateriaService().getMateriasCapitalize();
            selectMateria.setItems(FXCollections.observableList(materias));
            selectMateria.setValue(materias.isEmpty() ? null : materias.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void setMessage(boolean positive, String message) {
        txtMessage.setText(message);
        txtMessage.setTextFill(positive ? Color.GREEN : Color.RED);
        txtMessage.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentRunnable = this;
                try {
                    Thread.sleep(1000 * 8);
                    if (currentRunnable != this) return;
                    txtMessage.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onReset() {
        Pane.super.onReset();

        selectGenero.setItems(FXCollections.observableList(Generos.getGeneros()));
        selectGenero.setValue(Generos.getGeneros().get(0));
        try {
            List<String> materias = App.getMateriaService().getMateriasCapitalize();
            selectMateria.setItems(FXCollections.observableList(materias));
            selectMateria.setValue(materias.isEmpty() ? null : materias.get(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public AnchorPane getDiv() {
        return div;
    }
}