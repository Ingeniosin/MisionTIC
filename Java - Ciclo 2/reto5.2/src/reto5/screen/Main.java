package reto5.screen;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import reto5.App;
import reto5.services.SchoolGradingSystem;
import reto5.utils.Screen;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main extends Screen {

    @FXML private Button btnCalcular;
    @FXML private TextArea txtResponse, txtCalcular;
    @FXML private Label labelStatus;

    public Main() throws IOException {
        super(App.class.getResource("main.fxml"), "Sistema estadístico", 455, 390);
        this.enableMouseMoveEvent().setFill(Color.TRANSPARENT).setResizable(false).setInitStyle(StageStyle.TRANSPARENT);
    }

    @Override
    public void initListeners() {
        this.btnCalcular.addEventHandler(EventType.ROOT, e -> {
            if (!e.getEventType().equals(MouseEvent.MOUSE_CLICKED)) return;
            try {
                Objects.requireNonNull(this.txtCalcular.getText(), "Input invalido");
                if (this.txtCalcular.getText().isEmpty()) throw new Exception("Input invalido");
                SchoolGradingSystem schoolGradingSystem = new SchoolGradingSystem(SchoolGradingSystem.getStaticStudents(), SchoolGradingSystem.getStaticClasses());
                schoolGradingSystem.loadData(Arrays.stream(this.txtCalcular.getText().split("\n")).collect(Collectors.toList()));
                this.txtResponse.setText(String.format("%.2f%n", schoolGradingSystem.stat1()) + schoolGradingSystem.stat2() + "\n" + schoolGradingSystem.stat3() + "\n" + schoolGradingSystem.stat4());
                setStatus(true, "Consulta realizada con exito!");
            } catch (Exception ex) {
                setStatus(false, "ERROR: " + ex.getMessage());
                this.txtResponse.setText(null);
            }
        });
    }

    private void setStatus(boolean positive, String message) {
        this.labelStatus.setTextFill(positive ? Color.GREEN : Color.RED);
        this.labelStatus.setText(message);
        this.labelStatus.setVisible(true);
    }
}
