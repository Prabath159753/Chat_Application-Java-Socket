package Client.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : Kavishka Prabath
 * @since : 0.1.0
 **/

public class LoginClientFormController {

    public AnchorPane root;
    public JFXButton btnLogin;
    public TextField txtUsername;

    public static String username;

    public void loginOnAction(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER){
            ActionEvent mouseevent = null;
            loginToChatRoomOnAction(mouseevent);
        }
    }

    public void loginToChatRoomOnAction(ActionEvent actionEvent) throws IOException {

        username = txtUsername.getText();
        loadChat();

    }

    private void loadChat() throws IOException {
        Stage exitstage = (Stage) btnLogin.getScene().getWindow();
        exitstage.close();
        URL resource = this.getClass().getResource("/Client/views/ClientForm.fxml");

        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = new Stage();

        stage.setScene(scene);
        enableMove(scene,stage);
        stage.show();
    }

    private void enableMove(Scene scene, Stage stage) {
        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);
        scene.setOnMousePressed(event -> {
            xOffset.set(stage.getX() - event.getScreenX());
            yOffset.set(stage.getY() - event.getScreenY());
        });
        //Lambda mouse event handler
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + xOffset.get());
            stage.setY(event.getScreenY() + yOffset.get());
        });
    }


}
