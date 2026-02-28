
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Player {
    int x;
    int y;
    double xspeed;
    double yspeed;
    int width;
    int height;
    GamePanel panel;

    Rectangle hitBox;

    boolean keyLeft;
    boolean keyRight;
    boolean keyUp;
    boolean keyDown;

    //crouching
    int originalHeight;
    boolean isCrouching;

    public Player(int x, int y, GamePanel panel) {
        this.x = x;
        this.y = y;
        this.panel = panel;
        width = 50;
        height = 100;
        hitBox = new Rectangle(x, y, width, height);

        //crouching
        originalHeight = height;
        isCrouching = false;
    }

    public void set() {

        // Horisontal bevegelse
        if (keyLeft && keyRight || !keyLeft && !keyRight) xspeed *= 0.8;
        if (keyLeft && !keyRight) xspeed --;
        if (!keyLeft && keyRight) xspeed ++;

        //stoppe fart
        if (xspeed < 0 && xspeed > -0.75) xspeed = 0.0;
        if (xspeed > 0 && xspeed < 0.75) xspeed = 0.0;

        //Max fart
        if (xspeed > 7) xspeed = 7;
        if (xspeed < -7) xspeed = -7;
        
        // Vertikal bevegelse
        if (keyUp) {
            hitBox.y ++; //beveger hitBox en ned for å sjekke for kollisjon
            for (Wall wall : panel.walls) {
                if (wall.hitBox.intersects(hitBox)) yspeed = -9.5;
            }
            hitBox.y --;
        }
        
        if (isCrouching) {
            yspeed += 0.6;
        } else {
            yspeed += 0.3; //Tyngdekraft baby
        }
        

        // Kollisjon med bakke og tak
        hitBox.y += yspeed;
        for (Wall wall : panel.walls) {
            if (hitBox.intersects(wall.hitBox)) {
                hitBox.y -= yspeed;
                while (!hitBox.intersects(wall.hitBox)) hitBox.y += Math.signum(yspeed);
                hitBox.y -= Math.signum(yspeed);
                yspeed = 0.0;
                y = hitBox.y;
            }
        }

        // Kollisjon med vegg
        hitBox.x += xspeed;
        for (Wall wall : panel.walls) {
            if (hitBox.intersects(wall.hitBox)) {
                hitBox.x -= xspeed;
                while (!hitBox.intersects(wall.hitBox)) hitBox.x += Math.signum(xspeed);
                hitBox.x -= Math.signum(xspeed);
                xspeed = 0;
                x = hitBox.x;
            }   
        }

        //Kollisjon med fiende i x- og y-retning
        hitBox.x += xspeed;
        for (Enemy enemy : panel.enemies) {
            if (hitBox.intersects(enemy.hitBox)) {
                hitBox.x -= xspeed;
                while (!hitBox.intersects(enemy.hitBox)) hitBox.x += 2*Math.signum(xspeed);
                //hitBox.x -= Math.signum(xspeed);
                xspeed = 0;
                x = hitBox.x;
            }
        }

        hitBox.y += yspeed;
        for (Enemy enemy : panel.enemies) {
            if (hitBox.intersects(enemy.hitBox)) {
                hitBox.y -= yspeed;
                while (!hitBox.intersects(enemy.hitBox)) hitBox.y += 2*Math.signum(yspeed);
                //hitBox.y -= Math.signum(yspeed);
                yspeed = 0.0;
                y = hitBox.y;
            }
        }

        // crouching
        if (keyDown && !isCrouching) {
            height = originalHeight / 2;
            y += originalHeight - height;
            hitBox.height = height;
            hitBox.y = y;
            isCrouching = true;
        } else if (!keyDown && isCrouching) {
            y -= originalHeight - height;
            height = originalHeight;
            hitBox.height = height;
            hitBox.y = y;
            isCrouching = false;
        }

        // Map - bevegelse
        if (hitBox.x > 350) {
            int moveAmount = (int) xspeed;

            for (Wall walls : panel.walls) { //vegger
                walls.hitBox.x -= moveAmount;
                walls.x -= moveAmount;
            }

            for (Enemy enemy : panel.enemies) { //fiende
                enemy.updatePosition(-moveAmount);
            }

            x -= moveAmount; //spiller
            hitBox.x -= moveAmount;
        } else if (hitBox.x < 350) {
            int moveAmount = (int) -xspeed;

            for (Wall walls : panel.walls) { // vegger
                walls.hitBox.x += moveAmount;
                walls.x += moveAmount;
            }
            for (Enemy enemy : panel.enemies) { //fiende
                enemy.updatePosition(moveAmount);
            }

            x += moveAmount; //spiller
            hitBox.x += moveAmount;
        }

        y += yspeed;
        x += xspeed;
        hitBox.x = x;
        hitBox.y = y;
    }

    public void setPosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
        this.xspeed = 0;
        this.yspeed = 0;
        this.hitBox.x = newX;
        this.hitBox.y = newY;
    }

    public void draw(Graphics2D gtd) {
        gtd.setColor(Color.BLACK);
        gtd.fillRect(x, y, width, height);
    }
}
