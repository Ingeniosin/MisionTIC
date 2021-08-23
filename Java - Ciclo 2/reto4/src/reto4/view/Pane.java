package reto4.view;

import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;

public interface Pane {

    default void setDisable(boolean bol) {
        getDiv().getChildren().forEach(node -> node.setDisable(bol));
    }

    default void onResume() {
    }

    default void onReset() {
        getDiv().getChildren().stream().filter(node -> node instanceof TextInputControl).map(node -> (TextInputControl) node).forEach(textField -> textField.setText(null));
    }

    AnchorPane getDiv();

}
