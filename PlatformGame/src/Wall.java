import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Wall {
    int x;
    int y;
    int width;
    int height;
    int originalX, originalY;
    Rectangle hitBox;
    
    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
        this.width = width;
        this.height = height;

        hitBox = new Rectangle(x, y, width, height);
    }

    public void resetPosition() {
        this.x = originalX;
        this.y = originalY;
        hitBox.x = originalX;
        hitBox.y = originalY;
    }

    public void draw(Graphics2D gtd) {
        gtd.setColor(Color.black);
        gtd.drawRect(x, y, width, height);
        gtd.setColor(Color.white);
        gtd.fillRect(x+1, y+1, width-1 ,height-1);
    }
}
