package reto3.utils;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public abstract class Screen implements Controller {
    public static HashMap<String, Screen> screensAvailable = new HashMap<>();
    private final Stage stage;
    private Scene scene;
    private Controller controller;
    private EventHandler<Event> eventHandler;
    private Parent root;
    private double yMouse, xMouse;

    public Screen(Stage stage) {
        this.stage = stage;
        screensAvailable.put(stage.getTitle().toLowerCase(), this);
    }

    public Screen(URL file, String name, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(file);
        fxmlLoader.setController(this);
        this.root = fxmlLoader.load();
        this.stage = new Stage();
        this.controller = this;
        this.scene = new Scene(root, width, height);

        stage.setTitle(name);
        screensAvailable.put(name.toLowerCase(), this);
    }

    public static Screen getScreen(String name) {
        return screensAvailable.getOrDefault(name.toLowerCase(), null);
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public Screen setFill(Paint value) {
        this.scene.setFill(value);
        return this;
    }

    public Screen setInitStyle(StageStyle value) {

        this.stage.initStyle(value);
        return this;
    }

    public Screen setLocation(double X, double Y) {
        if (X <= -1 || Y <= -1) {
            stage.centerOnScreen();
            return this;
        }
        stage.setX(X);
        stage.setY(Y);
        return this;
    }

    public Screen setResizable(boolean boResizable) {
        stage.setResizable(boResizable);
        return this;
    }

    public Screen show() {
        stage.setScene(scene);
        stage.show();
        this.initInterListeners();
        controller.initListeners();
        return this;
    }

    public Screen enableMouseMoveEvent() {
        this.root.setOnMousePressed(event -> {
            yMouse = event.getSceneY();
            xMouse = event.getSceneX();
        });
        this.root.setOnMouseDragged(event -> {
            this.stage.setX(event.getScreenX() - xMouse);
            this.stage.setY(event.getScreenY() - yMouse);
        });
        return this;
    }

    public Screen setAlwaysOnTop(boolean value) {
        stage.setAlwaysOnTop(value);
        return this;
    }

    public Screen close() {
        screensAvailable.remove(stage.getTitle(), this);
        stage.removeEventHandler(EventType.ROOT, this.eventHandler);
        stage.show();
        return this;
    }

    public void initInterListeners() {
        this.eventHandler = (event -> {
            if (event instanceof WindowEvent) {
                WindowEvent windowEvent = (WindowEvent) event;
                EventType<WindowEvent> eventType = windowEvent.getEventType();
                if (WindowEvent.WINDOW_CLOSE_REQUEST.equals(eventType)) {
                    onWindowClose();
                } else if (WindowEvent.WINDOW_HIDDEN.equals(eventType)) {
                    onWindowHide();
                } else if (WindowEvent.WINDOW_SHOWN.equals(eventType)) {
                    onWindowShown();
                }
            }
        });
        stage.addEventHandler(EventType.ROOT, this.eventHandler);
    }

    public void onWindowClose() {
        close();
    }

    public void onWindowHide() {

    }

    public <T> T getChildByID(String id) {
        return (T) scene.lookup("#" + id);
    }

    public void onWindowShown() {

    }

}