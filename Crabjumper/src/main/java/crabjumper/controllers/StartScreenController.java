package crabjumper.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import crabjumper.io.HighScoreManager;
import static javafx.application.Platform.runLater;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartScreenController implements Initializable{
    @FXML private Button play;
    @FXML TextField nameInput;
    @FXML private ListView<String> highScoreList;

    private static String playerName;

    public void switchToGame() throws IOException { //laster IOException om innlasting av FXML-fil feiler
        String input = nameInput.getText().trim();
        if (input.length() > 8) {
            showAlert("Navn for langt.", "Vennligst bruk max 8 tegn");
            return;
        }

        playerName = input.isEmpty() ? "Unknown" : input;

        Stage stage = (Stage) play.getScene().getWindow(); //henter vindu hvor scene er ved å hente scene hvor knapp er
        Parent root = FXMLLoader.load(getClass().getResource("/crabjumper/Game.fxml")); //laster inn ny fil og setter en ny rotnode på den
        stage.setTitle("Crab Jumper: Get To The Surface"); 
        stage.setScene(new Scene(root)); //setter en ny scene med nytt FXML innhold (root)
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hent topp 5 highscores og vis i Listview
        List<String> scores = HighScoreManager.getTop5();
        ObservableList<String> observableScores = FXCollections.observableArrayList(scores);
        highScoreList.setItems(observableScores);
        highScoreList.getSelectionModel().clearSelection(); // fjerner default-markering
    }

    public void showAlert(String title, String message) {
        runLater(() -> { //runlater lar meg kjøre kode på GUI-tråden (en egen tråd som oppdaterer GUI-komponenter)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    // GETTERS

    public static String getPlayerName() {
        return playerName;
    }
}
