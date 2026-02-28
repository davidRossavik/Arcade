package crabjumper.models;

import java.io.IOException;
import java.net.URL;

import crabjumper.interfaces.GameObject;
import static crabjumper.models.GameModel.SCREEN_WIDTH;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Crab implements GameObject {
    private double x, y;
    private double yspeed;
    private Image sprite; // Bredde: 1024, Høyde: 1024
    private final Image jumpSprite;
    private final Image normalSprite;

    private final double SCALE = 0.1;
    private static final double GRAVITY = 800;
    private static final double SPEED = 300;

    public Crab(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        this.yspeed = 0;
        this.jumpSprite = loadImage("/crabjumper/images/CrabJump.png", SCALE);
        this.normalSprite = loadImage("/crabjumper/images/StandingCrab.png", SCALE);
        this.sprite = normalSprite;
    }


    // BEVEGELSE //

    public void moveLeft(double deltaTime) {
        x -= SPEED * deltaTime;
        if (x + getWidth() < 0) x = SCREEN_WIDTH; // wrap-around bredde
    }
    public void moveRight(double deltaTime) {
        x += SPEED * deltaTime;
        if (x > SCREEN_WIDTH) x = - getWidth(); // wrap-around bredde
    }
    public void bounce(double jumpStrength) {
        this.yspeed = jumpStrength;
    }

    // SPRITE - FEILHÅNDTERINGSMETODE //

    private Image loadImage(String resourcePath, double scale) {
        try {
            URL url = Crab.class.getResource(resourcePath);
            if (url == null) throw new IOException("Fant ikke ressurs: " + resourcePath);

            String imageUrl = url.toExternalForm();
            Image original = new Image(imageUrl);
            if (original.isError()) throw new IOException("Feil ved lasting");

            return new Image(imageUrl, original.getWidth() * scale, original.getHeight() * scale, true, true);
        } catch (Exception e) {
            System.err.println("Kunne ikke laste bilde: " + resourcePath);
            int width = 75, height = 75;
            WritableImage fallback = new WritableImage(width, height);
            PixelWriter pw = fallback.getPixelWriter();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pw.setColor(x,y,Color.RED);
                }
            }
            return fallback;
        } 
    }
    

    // SPILL-GRAFIKK //

    @Override
    public void update(double deltaTime) { 
        yspeed += GRAVITY * deltaTime; //tyngdekraft
        y += yspeed * deltaTime; // opdaterer y-pos

        if (yspeed < 0) {
            sprite = jumpSprite;
        } else {
            sprite = normalSprite;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(sprite, x, y);
    }


    // GETTERS //

    public void setY(double newY) {
        this.y = newY;
    }
    public Bounds getBounds() {
        return new BoundingBox(x, y, sprite.getWidth()-40, sprite.getHeight()-50);
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
    public double getYspeed() {
        return yspeed;
    }
    
}
