package boardgame;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import boardgame.model.BoardGameModel;
import org.tinylog.Logger;

public class BoardGameController {

    @FXML
    private void switchScene(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/WelcomeScreen.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.out.println("Exiting...");
        Platform.exit();
    }

    @FXML
    private GridPane board;

    @FXML
    private TextField numberOfTailsField;

    @FXML
    private TextField playerName1;

    @FXML
    private TextField playerName2;

    private StringProperty name1 = new SimpleStringProperty();
    private StringProperty name2 = new SimpleStringProperty();

    private BoardGameModel model = new BoardGameModel();


    @FXML
    private void initialize() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
        numberOfTailsField.textProperty().bind(model.numberOfTailsProperty().asString());
        model.gameOverProperty().addListener(this::handleGameOver);
        model.currentPlayerProperty().addListener(
                (observableValue, oldPlayer, newPlayer) -> {
                    System.out.printf("%s's turn!\n", newPlayer);
                }
        );
        Logger.info("name = {}", name1);
        Logger.info("name = {}", name2);
        playerName1.textProperty().bind(Bindings.concat("Hello, ", name1));
        playerName2.textProperty().bind(Bindings.concat("Hello, ", name2));
    }
    public void setName1 (String name1){
            Logger.info("Setting Player 1 name to {}", name1);
            this.name1.set(name1);
        }

    public void setName2 (String name2){
        Logger.info("Setting Player 1 name to {}", name2);
        this.name2.set(name2);
    }

    private void handleGameOver(ObservableValue observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
            gameOverAlert.setTitle("Game Over");
            gameOverAlert.setHeaderText("Game Over");
            gameOverAlert.setContentText("All coins are TAILS side facing up!");
            gameOverAlert.showAndWait();
            Platform.exit();
        }
    }


    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(20);
/*
        piece.fillProperty().bind(Bindings.when(model.squareProperty(i, j).isEqualTo(Square.NONE))
        .then(Color.TRANSPARENT)
        .otherwise(Bindings.when(model.squareProperty(i, j).isEqualTo(Square.HEAD))
        .then(Color.RED)
        .otherwise(Color.BLUE))
        );
*/
        piece.fillProperty().bind(
                new ObjectBinding<Paint>() {
                    {
                        super.bind(model.squareProperty(i, j));
                    }
                    @Override
                    protected Paint computeValue() {
                        return switch (model.squareProperty(i, j).get()) {
                            case HEAD -> Color.RED;
                            case TAIL -> Color.BLUE;
                        };
                    }
                }
        );
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        System.out.printf("Click on square (%d,%d)\n", row, col);
        model.move(row, col);
    }

}
