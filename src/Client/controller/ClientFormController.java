package Client.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import static Client.controller.LoginClientFormController.username;

/**
 * @author : Kavishka Prabath
 * @since : 0.1.0
 **/

public class ClientFormController extends Thread {

    public Label lblUsername;
    public ImageView btnBackToLogin;
    public TextField txtClientMessage;
    public ImageView btnSend;
    public ImageView btnImage;
    public HBox hboxmessage;
    public VBox vboxmessage;

    Socket socket;
    BufferedReader bufferedReader;
    PrintWriter printWriter;

    FileChooser fileChooser;
    File filePath;
    URL url;

    public void initialize() {
        connectSocket();
        lblUsername.setText(username);
    }

    private void connectSocket() {
        try {
            socket = new Socket("localhost", 5000);
            System.out.println("Connect With Server");

            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);

            this.start();

        } catch (IOException e) {

        }
    }

    public void run() {
        try {
            while (true) {
                String msg = bufferedReader.readLine();
                System.out.println("Message : " + msg);
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];
                System.out.println("cmd : " + cmd);
                StringBuilder fulmsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fulmsg.append(tokens[i]);
                }
                System.out.println("fulmsg : " + fulmsg);
                System.out.println();
                if (cmd.equalsIgnoreCase(LoginClientFormController.username + ":")) {
                    continue;
                } else if (fulmsg.toString().equalsIgnoreCase("bye")) {
                    break;
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        HBox hBox = new HBox();

                        if (fulmsg.toString().startsWith("assets/emojis/") ) {
                            System.out.println("Emoji path "+fulmsg);
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 10, 5, 5));
                            Text text = new Text(cmd + " ");
                            ImageView imageView = new ImageView();
                            Image image = new Image(String.valueOf(fulmsg));
                            imageView.setImage(image);
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            TextFlow textFlow = new TextFlow(text, imageView);
                            VBox vBox = new VBox(textFlow);
                            vBox.setAlignment(Pos.CENTER_LEFT);
                            vBox.setPadding(new Insets(5, 10, 5, 5));
                            vboxmessage.getChildren().add(vBox);
                        } else if (fulmsg.toString().endsWith(".png") || fulmsg.toString().endsWith(".jpg") || fulmsg.toString().endsWith(".jpeg") || fulmsg.toString().endsWith(".gif")) {
                            boolean bool = fulmsg.toString().endsWith(".png");
                            System.out.println(bool);
                            hBox.setAlignment(Pos.TOP_LEFT);
                            hBox.setPadding(new Insets(5, 10, 5, 5));
                            Text text = new Text(cmd + " ");
                            ImageView imageView = new ImageView();
                            Image image = new Image(String.valueOf(fulmsg));
                            imageView.setImage(image);
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(100);
                            TextFlow textFlow = new TextFlow(text, imageView);
                            /*textFlow.setStyle("-fx-color:rgb(239,242,255);"
                                    + "-fx-background-color: rgb(182,182,182);" +
                                    "-fx-background-radius: 10px");*/
                            textFlow.setPadding(new Insets(5, 0, 5, 5));
                            VBox vBox1 = new VBox(textFlow);
//                            VBox vBox2 = new VBox(imageView);
                            vBox1.setAlignment(Pos.CENTER_LEFT);
//                            vBox1.setPadding(new Insets(5, 10, 5, 5));
//                            vBox2.setAlignment(Pos.CENTER_LEFT);
//                            vBox2.setPadding(new Insets(5, 10, 5, 5));
//                            hBox.getChildren().add(textFlow);
//                            vboxmessage.getChildren().add(vBox1);
//                            vboxmessage.getChildren().add(vBox2);
                            vboxmessage.getChildren().add(vBox1);
                        } else {
                            boolean bool = fulmsg.toString().endsWith(".png");
                            System.out.println(bool);
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 10, 5, 5));
                            Text text = new Text(msg);
                            TextFlow textFlow = new TextFlow(text);
                            textFlow.setStyle("-fx-color:rgb(239,242,255);"
                                    + "-fx-background-color: rgb(182,182,182);" +
                                    "-fx-background-radius: 10px;-fx-font-size: 15px");
                            textFlow.setPadding(new Insets(5, 0, 5, 5));
                            text.setFill(Color.color(0, 0, 0));
                            hBox.getChildren().add(textFlow);
                            vboxmessage.getChildren().add(hBox);
                        }
                    }
                });
                /*txtClientPane.appendText(msg + "\n");*/
            }
            bufferedReader.close();
            printWriter.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
            try {
                bufferedReader.close();
                printWriter.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }


    public void backToLoginOnAction(MouseEvent mouseEvent) throws IOException {
        Stage stg = (Stage) btnBackToLogin.getScene().getWindow();
        stg.close();
        URL resource = this.getClass().getResource("/Client/views/LoginClientForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene = new Scene(load);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Play Tech Chat Application");
        enableMove(scene, stage);
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

    public void sendMessageOnAction(MouseEvent mouseEvent) {
        send();
    }

    public void send() {
        String msg = txtClientMessage.getText();
        printWriter.println(LoginClientFormController.username + ":  " + msg + "  ");
//        txtClientPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        Text text = new Text(msg);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color:rgb(239,242,255);"
                + "-fx-background-color: rgb(15,125,242);" +
                "-fx-background-radius: 20px; -fx-font-size: 15px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));
        hBox.getChildren().add(textFlow);
        vboxmessage.getChildren().add(hBox);
        printWriter.flush();

//        txtClientPane.appendText("Me: " + msg + "\n");
        txtClientMessage.setText("");
        if (msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    public void sendMessageByKeyOnAction(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            send();
        }
    }

    public void chooseImageOnAction(MouseEvent mouseEvent) throws MalformedURLException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Image");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            printWriter.println(username + ": " + file.toURI().toURL());
        }
        /*printWriter.println(username + ": " + file.getPath());*/
        /*txtClientMessage.setText(filePath.getPath());*/
        if (file != null) {
            System.out.println("File Was Selected");
            url = file.toURI().toURL();
            System.out.println(url);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 10, 5, 5));
            ImageView imageView = new ImageView();
            Image image = new Image(String.valueOf(url));
            imageView.setImage(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            VBox vBox = new VBox(imageView);
            vBox.setAlignment(Pos.CENTER_RIGHT);
            vBox.setPadding(new Insets(5, 10, 5, 5));
            vboxmessage.getChildren().add(vBox);
        }
    }

}
