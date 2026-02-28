package crabjumper.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import crabjumper.interfaces.GameOverListener;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;

public class GameModel {
    private Crab crab;
    private List<Platform> platforms;
    private double score = 0;
    public static final double SCREEN_WIDTH = 700;
    public static final double SCREEN_HEIGHT = 700;
    private double cameraOffset = 0;
    private Difficulty difficulty = Difficulty.EASY;
    private double maxHeightReached = 0;
    private boolean isGameOver = false;

    private GameOverListener gameOverListener; // observatør


    public GameModel() {
        this.score = 0;
        this.crab = new Crab(100, 700);
        this.platforms = new ArrayList<>();
        platforms.add(new Platform(100, 750));
        platforms.add(new Platform(100, 600));
        platforms.add(new Platform(100, 500));

        for (int i = 3; i < 8; i++) {
            double y = SCREEN_HEIGHT - i * difficulty.platformGap;
            Platform p = Platform.generateRandomPlatform(SCREEN_WIDTH, y); 
            platforms.add(p);
        }
    }


    // SPILL-GRAFIKK OG OPPDATERNG //

    public void update(double deltaTime, Set<KeyCode> activeKeys) {
        if (isGameOver || deltaTime <= 0) return;
        if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A)) crab.moveLeft(deltaTime);
        if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D)) crab.moveRight(deltaTime);

        crab.update(deltaTime);
    
        scroll();

        updateDifficulty();

        removePlatforms(deltaTime);

        checkCollisions();

        generatePlatforms();

        if (crab.getY() > SCREEN_HEIGHT + 50) gameOver();
    }


    // SPILL-LOGIKK //

    public void setGameOverListener(GameOverListener listener) {
        this.gameOverListener = listener;
    }

    private void gameOver() {
        if (gameOverListener != null) {
            gameOverListener.onGameOver((int) score);
        }
        isGameOver = true;
    }

    private void scroll() {
        double screenCenterY = 300;
        if (crab.getY() < screenCenterY) {
            double delta = screenCenterY - crab.getY();
            cameraOffset += delta;

            crab.setY(screenCenterY); // Hold krabben visuelt i midten
            for (Platform p : platforms) {
                p.shiftY(delta);
                p.updateActiveStatus();
            }
        }
    }

    public void checkCollisions() {
        Bounds crabBox = crab.getBounds();
        for (Platform platform : platforms) {
            if (crabBox.intersects(platform.getBounds()) && crab.getYspeed() > 0) {
                crab.bounce(difficulty.jumpStrength);

                // oppdaterer score basert på scrolling og kollisjon
                double newHeight = cameraOffset;
                if (newHeight > maxHeightReached + 1) {
                    score += 10;
                    maxHeightReached = newHeight;
                } 
                break;
            }
        }
    }

    private void removePlatforms(double deltaTime) {
        Iterator<Platform> iterator = platforms.iterator();
        while (iterator.hasNext()) {
            Platform p = iterator.next();
            if (!p.isActive()) iterator.remove();
        }
    }

    private void generatePlatforms() {
        double highestY = platforms.stream().mapToDouble(Platform::getY).min().orElse(SCREEN_HEIGHT);

        if (highestY > -difficulty.platformGap) {
            double newY = highestY - difficulty.platformGap;
            platforms.add(Platform.generateRandomPlatform(SCREEN_WIDTH, newY));
            highestY = newY;
        }
    }

    private void updateDifficulty() {
        if (score >= 200) {
            difficulty = Difficulty.HARD;
        } else if (score >= 100) {
            difficulty = Difficulty.MEDIUM;
        } else {
            difficulty = Difficulty.EASY;
        }

    }
    

    // GETTERS //

    public List<Platform> getPlatforms() {
        return platforms;
    }
    public boolean getGameOver() {
        return isGameOver;
    }
    public Crab getCrab() {
        return crab;
    }
    public double getScore() {
        return score;
    }
}
