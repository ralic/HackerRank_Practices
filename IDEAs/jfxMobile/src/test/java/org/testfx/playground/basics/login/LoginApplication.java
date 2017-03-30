package org.testfx.playground.basics.login;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;

public class LoginApplication extends Application {

    public static void main(String[] args) {
        launch(LoginApplication.class, args);
    }

    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label messageLabel;
    @FXML
    Button loginButton;
    @FXML
    Button logoutButton;

    @Override
    public void start(Stage stage) throws Exception {
//        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
//        Scene scene = new Scene(new StackPane(
//                new Label(Paths.get("").toAbsolutePath().toString())),
//                bounds.getWidth(), bounds.getHeight());
//        stage.setScene(scene);
//        stage.show();
        String path = Paths.get("").toAbsolutePath().toString() +
                "/jfxMobile/src/test/resources/org/testfx/playground/basics/login" +
                "/loginForm.fxml";
        URL resource = getClass().getResource(
                path);
        Parent parent = FXMLLoader.load(resource);
        stage.setTitle("Login Form");
        stage.setScene(new Scene(parent));
        stage.show();
    }

}
