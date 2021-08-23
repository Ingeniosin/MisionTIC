package reto4.view;

import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import reto4.App;
import reto4.screen.Screen;
import reto4.view.pane.DataPane;
import reto4.view.pane.ProcesarPane;
import reto4.view.pane.QueryPane;

import java.io.IOException;

public class PrincipalScreen extends Screen {

    @FXML
    private Button btnData, btnEliminar, btnProcesar;
    @FXML
    private AnchorPane divData, divEliminar, divProcesar;

    //1
    @FXML
    private TextField txtName, txtNota;
    @FXML
    private Label labelMessage;
    @FXML
    private Button btnGuardar;
    @FXML
    private ChoiceBox<String> selectMateria, selectGenero;

    //2
    @FXML
    private TextArea txtCargar2, txtProceso2;
    @FXML
    private Button btnProcesar2, btnReiniciar;
    @FXML
    private Label labelMessage2;

    //3
    @FXML
    private TextField txtNombre3;
    @FXML
    private TextArea txtResponse3;
    @FXML
    private Button btnConsultar3, btnEliminar3;
    @FXML
    private ChoiceBox<String> selectMateria3;
    @FXML
    private Label labelMessage3;


    private Pane currentPane;

    public PrincipalScreen() throws IOException {
        super(App.class.getResource("Ventana.fxml"), "School grading system", 600, 360);
        this.setResizable(false);
        this.setInitStyle(StageStyle.TRANSPARENT);
        this.setFill(Color.TRANSPARENT);
        this.enableMouseMoveEvent();
    }

    public void setCurrentContent(Pane pane) {
        if (currentPane != null && currentPane == pane) {
            pane.onReset();
            return;
        }
        reset();
        pane.onResume();
        this.currentPane = pane;
        pane.getDiv().setVisible(true);
    }

    public void reset() {
        divData.setVisible(false);
        divProcesar.setVisible(false);
        divEliminar.setVisible(false);
    }

    @Override
    public void initListeners() {
        DataPane initialPane = new DataPane(divData, txtName, txtNota, selectGenero, selectMateria, btnGuardar, labelMessage);
        addContentListener(btnData, initialPane);
        addContentListener(btnProcesar, new ProcesarPane(divProcesar, txtCargar2, txtProceso2, btnProcesar2, labelMessage2, btnReiniciar));
        addContentListener(btnEliminar, new QueryPane(divEliminar, txtNombre3, txtResponse3, btnConsultar3, btnEliminar3, selectMateria3, labelMessage3));
        setCurrentContent(initialPane);
    }

    private void addContentListener(Button button, Pane pane) {
        button.addEventHandler(EventType.ROOT, event -> {
            if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) setCurrentContent(pane);
        });
    }
}
