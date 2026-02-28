import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        
        // Lager skjermen
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake"); // Oppretter vindu: Nytt JFrame-objekt med tittel "Snake"
        frame.setVisible(true); //Gjør vindu synlig: Standardinnstilling for JFrame er at det ikke er synlig når opprettes
        frame.setSize(boardWidth, boardHeight); // Setter vindusstørrelsen
        frame.setLocationRelativeTo(null); // Plasserer vindu i midt av skjermen: Null = midten
        frame.setResizable(false); // Hindrer at vinduet kan endre størrelse
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Avslutter programmet når vindu lukkes

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight); // Lager et nytt objekt med skjermen
        frame.add(snakeGame); //Legger til skjermen på vinudet vårt
        frame.pack(); // setter spillet til fulle dimensjonen i forhold til tittel-baren
        snakeGame.requestFocus();

    }
}
