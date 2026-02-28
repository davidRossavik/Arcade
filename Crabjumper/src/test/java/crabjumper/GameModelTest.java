package crabjumper;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import crabjumper.io.HighScoreManager;
import crabjumper.models.Crab;
import crabjumper.models.GameModel;
import crabjumper.models.Platform;
import javafx.geometry.Bounds;
import javafx.print.PrintResolution;

public class GameModelTest {

    //Tester at tyngdekraft fungerer
    //Tester gameOver
    //Tester lese og skrive til fil?
    // Teste at highscore oppdateres ved hvert hopp?
    //Teste at nye platformer oppstår

    @Test
    public void testGravity() {
        GameModel model = new GameModel();
        Crab crab = model.getCrab();

        model.getPlatforms().clear();

        crab.setY(100);
        crab.update(0.1);
        assertTrue(crab.getYspeed() > 0, "Tyngdekraften bør fungere");
    }

    @Test
    public void testCollisionTriggersBounce() {
        GameModel model = new GameModel();
        Crab crab = model.getCrab();
        crab.setY(100);

        model.getPlatforms().clear();
        Platform platform = new Platform(crab.getX(), 200);
        model.getPlatforms().add(platform);

        
        for (int i = 0; i < 10; i++) {
            model.update(0.1, Set.of());
            if (crab.getBounds().intersects(platform.getBounds())) break;
        }

        assertTrue(crab.getYspeed() < 0, "Krabben burde sprette, og yspeed reduseres");
    }

    @Test
    public void testGameOver() {
        GameModel model = new GameModel();
        Crab crab = model.getCrab();
        crab.setY(600);

        model.getPlatforms().clear();

        for (int i = 0; i < 10; i++) {
            model.update(0.1, Set.of());
        }

        assertTrue(model.getGameOver(), "Spillet burde være i GameOver tilstand");
    }

    @Test
    public void testPlatformGenerating() {
        GameModel model = new GameModel();

        model.getPlatforms().clear();
        Platform platform = new Platform(100, 100);
        model.getPlatforms().add(platform);

        int before = model.getPlatforms().size();
        model.update(0.1,Set.of());
        int after = model.getPlatforms().size();

        assertTrue(before < after, "Det burde vært generert en platform");
    }

    @Test
    public void testPlatformRemoving() {
        GameModel model = new GameModel();
        Crab crab = model.getCrab();
        crab.setY(300);
        
        Platform active = new Platform(100,100);
        Platform inactive = new Platform(100, GameModel.SCREEN_HEIGHT + 200);

        model.getPlatforms().clear();
        model.getPlatforms().add(active);
        model.getPlatforms().add(inactive);

        model.getPlatforms().forEach(p -> p.updateActiveStatus());
        assertFalse(inactive.isActive(), "Forventet at plattformen er inaktiv");

        model.update(0.016, Set.of());

        Iterator<Platform> iterator = model.getPlatforms().iterator(); //fjerner øverste genererte plattform
        while (iterator.hasNext()) {
            Platform p = iterator.next();
            if (p.getY() < 100) iterator.remove();
        }

        assertEquals(1, model.getPlatforms().size(), "Skal bare være igjen en platform");
        assertTrue(model.getPlatforms().contains(active), "Den aktive plattformen bør være igjen");
    }

    @Test
    public void testHighScoreFile() {
        File tempFile = new File("src/main/resources/crabjumper/data/HighscoresTest2.txt");
        tempFile.deleteOnExit();

        HighScoreManager.setFilePath(tempFile.getPath());

        //Skrive
        boolean saved = HighScoreManager.saveScore("JUnitTester", 1234);
        assertTrue(saved, "Score skal lagres uten feil");

        //Lese
        List<String> scores = HighScoreManager.getTop5();
        boolean found = scores.stream().anyMatch(line -> line.contains("JUnitTester") && line.contains(String.valueOf(1234)));
        assertTrue(found, "TestScore burde være lagret og lest tilbake");
    }

    @Test
    public void testScoreCounter() {
        GameModel model = new GameModel();
        Crab crab = model.getCrab();
        crab.setY(600);

        model.getPlatforms().clear();

        for (int i = 0; i < 6; i++) {
            model.getPlatforms().add(new Platform(100, 700 - 50 * i));
        }

        for (int i = 0; i < 200; i++) {
            model.update(0.1, Set.of());
            if (model.getScore() > 0) break;
        }

        assertTrue(model.getScore() == 10, "Krabben bør ha 10 i score");
    }

}
