package crabjumper.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import crabjumper.interfaces.GameOverListener;
import crabjumper.io.HighScoreManager;
import crabjumper.models.GameModel;
import crabjumper.models.Platform;
import javafx.animation.AnimationTimer;
import static javafx.application.Platform.runLater;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameController implements GameOverListener {
    @FXML private Canvas gameCanvas;
    @FXML Pane gameOverOverlay;
    @FXML private Label scoreLabel;

    private GameModel model;
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public void initialize() {
        model = new GameModel();
        model.setGameOverListener(this); //observatør

        // Tastetrykk
        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        gameCanvas.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
        gameCanvas.requestFocus();

        gameOverOverlay.setVisible(false);

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        // GAME LOOP //
        AnimationTimer timer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastTime) / 1e9;
                lastTime = now;

                model.update(deltaTime, activeKeys);
                
                gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
                for (Platform platform : model.getPlatforms()) platform.render(gc);
                model.getCrab().render(gc);

                // SCORE-TEKST //
                LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#f3ff00")),
                        new Stop(1, Color.web("#d47415"))); // få riktig farge som i scenebuilder
                gc.setFill(gradient);
                gc.setFont(Font.font("Gill Sans Ultra Bold", 60));
                String scoreText = "" + (int) model.getScore();
                double x = (gameCanvas.getWidth() - gc.getFont().getSize() * scoreText.length())/2; // få i midten
                gc.fillText(scoreText, x, 100);
            }
            
        };
        timer.start();
    }

    @FXML
    private void restartGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crabjumper/Game.fxml")); //FXML lades på nytt
            Parent root = loader.load(); //Laster Game.fxml som JavaFX
            Stage stage = (Stage) gameCanvas.getScene().getWindow(); //finner vindu som knapp befinner seg i
            stage.setScene(new Scene(root)); // bytter scene til ny Game.fxml
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Feil ved restart.", "Kunne ikke starte spillet på nytt.");
        }
    }

    @FXML
    private void goToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crabjumper/StartScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gameCanvas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Feil ved Main Menu", "Kunne ikke laste inn startskjerm");
        }
    }

    @Override
    public void onGameOver(int score) { // observatør
        String name = StartScreenController.getPlayerName();
        boolean saved = HighScoreManager.saveScore(name, score);
        if (!saved) showAlert("Feil ved lagring", "Prøv igjen senere.");

        scoreLabel.setText("Score: " + score);
        gameOverOverlay.setVisible(true);
        activeKeys.clear();
    }

    public void showAlert(String title, String message) {
        runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
