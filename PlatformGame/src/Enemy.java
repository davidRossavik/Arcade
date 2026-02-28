import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Enemy {
    int x;
    int y;
    int originalX, originalY;
    int width;
    int height;
    Rectangle hitBox;
    int xspeed;
    int limit;
    int relativeX;
    int startX;

    public Enemy(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        width = 50;
        height = 50;
        limit = x + 2 * 50;
        this.relativeX = 0;
        this.startX = x;

        hitBox = new Rectangle(x, y, width, height);
    }

    public void move() {
        if (relativeX >= 2 * 50) {
            xspeed = -1;
        } else if (relativeX <= 0) {
            xspeed = 1;
        }

        relativeX += xspeed;
        x = startX + relativeX;
        hitBox.x = x; 
    }

    public void updatePosition(int dx) {
        startX += dx;
        // x += dx;
        // hitBox.x += dx;
    }

    public void resetPosition() {
        this.x = originalX;
        this.y = originalY;
        hitBox.x = originalX;
        hitBox.y = originalY;
        this.relativeX = 0;
        this.startX = originalX;
        this.xspeed = 1;
    }

    public void draw(Graphics2D gtd) {
        gtd.setColor(Color.RED);
        gtd.fillRect(x, y, width, height);
    }
}
