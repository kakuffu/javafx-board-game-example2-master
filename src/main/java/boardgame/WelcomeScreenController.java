package boardgame;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.tinylog.Logger;

public class WelcomeScreenController {

    @FXML
    private TextField enteredName1;

    @FXML
    private TextField enteredName2;

    @FXML
    private void initialize() {
        enteredName1.setText(System.getProperty("user1.name"));
        enteredName2.setText(System.getProperty("user2.name"));
    }

    @FXML
    private void switchScene(ActionEvent event) throws IOException {
        Logger.info("Names entered: {}", enteredName1.getText(), enteredName2.getText());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui.fxml"));
        Parent root = fxmlLoader.load();
        BoardGameController controller = fxmlLoader.<BoardGameController>getController();
        controller.setName1(enteredName1.getText());
        controller.setName2(enteredName2.getText());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.out.println("Exiting...");
        Platform.exit();
    }

}
