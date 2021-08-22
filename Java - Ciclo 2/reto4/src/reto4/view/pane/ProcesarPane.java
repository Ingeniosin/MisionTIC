package reto4.view.pane;

import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import reto4.App;
import reto4.SchoolGradingSystem;
import reto4.entity.Estudiante;
import reto4.entity.Materia;
import reto4.view.Pane;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProcesarPane implements Pane {

    private AnchorPane div;

    private final TextArea txtCargar, txtProceso;
    private final Button btnReiniciar, btnProcesar;
    private final  Label labelMessage2;

    private Runnable currentRunnable;

    public ProcesarPane(AnchorPane div, TextArea txtCargar, TextArea txtProceso, Button btnProcesar, Label labelMessage2, Button btnReiniciar) {
        this.div = div;
        this.txtCargar = txtCargar;
        this.txtProceso = txtProceso;
        this.btnProcesar = btnProcesar;
        this.labelMessage2 = labelMessage2;
        this.btnReiniciar = btnReiniciar;
        onReset();

        this.btnReiniciar.addEventHandler(EventType.ROOT, e ->{
            if(e.getEventType().equals(MouseEvent.MOUSE_CLICKED))
                onReset();
        });



        this.btnProcesar.addEventHandler(EventType.ROOT, e -> {
            if(e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                setDisable(true);
                labelMessage2.setVisible(false);
                try {
                    long time = System.currentTimeMillis();
                    SchoolGradingSystem schoolGradingSystem = new SchoolGradingSystem(App.getEstudianteService().getAll(), App.getMateriaService().getMaterias());
                    schoolGradingSystem.loadData(readData());
                    this.txtProceso.setText(schoolGradingSystem.stat1()+"\n"+schoolGradingSystem.stat2()+"\n"+schoolGradingSystem.stat3()+"\n"+schoolGradingSystem.stat4());
                    setMessage(true, "Se realizo la consulta y el calculo correctamente en "+(System.currentTimeMillis()-time)+" ms");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    setMessage(false, "Ocurrio un error en la base de datos: "+ex.getMessage());
                } catch (NullPointerException ex) {
                    setMessage(false, "No se encontro un valor o es invalido, revisa los IDs estudiante y notas: linea "+ex.getMessage());
                }
                setDisable(false);
            }
        });
    }

    private void setMessage(boolean positive, String message) {
        labelMessage2.setText(message);
        labelMessage2.setTextFill(positive ? Color.GREEN : Color.RED);
        labelMessage2.setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentRunnable = this;
                try {
                    Thread.sleep(1000 * 8);
                    if(currentRunnable != this) return;
                    labelMessage2.setVisible(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onReset() {
        Pane.super.onReset();
        labelMessage2.setVisible(false);
    }

    private List<String> readData() {
       return Arrays.stream(txtCargar.getText().split("\n")).collect(Collectors.toList());
    }

    @Override
    public AnchorPane getDiv() {
        return div;
    }
}
