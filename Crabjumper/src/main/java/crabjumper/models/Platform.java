package crabjumper.models;

import java.io.IOException;
import java.net.URL;

import crabjumper.interfaces.GameObject;
import static crabjumper.models.GameModel.SCREEN_HEIGHT;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Platform implements GameObject {
    private double x, y;
    private Image sprite; // Bredde: 668, Høyde: 300
    private boolean isActive = true;
    private static final double SCALE = 0.25;
    private static final double PLATFORM_WIDTH = 668 * SCALE;

    public Platform(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.sprite = loadImage("/crabjumper/images/Fish.png", SCALE);
    }


    // PLATTFORM GENERERING //

    public static Platform generateRandomPlatform(double screenWidth, double yPosition) {
        return new Platform(Math.random() * (screenWidth - PLATFORM_WIDTH), yPosition);
    }


    // SPRITE - FEILHÅNDTERINGSMETODE //

    private Image loadImage(String resourcePath, double scale) {
        try {
            URL url = Platform.class.getResource(resourcePath);
            if (url == null) throw new IOException("Fant ikke ressurs: " + resourcePath);

            String imageUrl = url.toExternalForm();
            Image original = new Image(imageUrl);
            if (original.isError()) throw new IOException("Feil ved lasting");

            return new Image(imageUrl, original.getWidth() * scale, original.getHeight() * scale, true, true);
        } catch (Exception e) {
            System.err.println("Kunbe ikke laste bilde: " + resourcePath);
            int width = 150, height = 75;
            WritableImage fallback = new WritableImage(width, height);
            PixelWriter pw = fallback.getPixelWriter();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pw.setColor(x,y,Color.BLUE);
                }
            }
            return fallback;
        } 
    }


    // SPILL-GRAFIKK //

    @Override
    public void update(double deltaTime) {}

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }


    // GETTERS //

    public void updateActiveStatus() { //Scroll() og test
        isActive = y < SCREEN_HEIGHT + 100;
    }
    public void shiftY(double delta) {
        this.y += delta;
    }
    public Bounds getBounds() { // Til kollisjon
        return new BoundingBox(x, y, sprite.getWidth()-40, sprite.getHeight()-75); // valgte 40 og 75 basert på testing
    }
    public boolean isActive() {
        return isActive;
    }
    @Override
    public double getX() {
        return x;
    }
    @Override
    public double getY() {
        return y;
    }
    @Override
    public double getWidth() {
        return sprite.getWidth();
    }
    @Override
    public double getHeight() {
        return sprite.getHeight();
    }
}
