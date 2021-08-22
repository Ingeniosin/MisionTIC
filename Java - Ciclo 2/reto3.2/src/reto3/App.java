package reto3;

import javafx.application.Application;
import javafx.stage.Stage;
import reto3.screen.Main;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new Main().show();
    }
}
