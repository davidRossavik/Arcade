import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener { // GUI-komponenter, knapper, tekstfelt, etiketter osv...
    private BufferedImage norrlands;

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Food
    Tile food;
    Random random;
    Tile norrlandsTile;

    //Game Logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) throws IOException {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.gray);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();

        //Norlands
        norrlands = loadNorrlandsImage();
        norrlandsTile = new Tile(15,15);
        placeNorrlands();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100,this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) { // innebygd Swing-metode, brukes til å tegne på en JPanel
        super.paintComponent(g); //Sørger for at tegnes riktig
        draw(g); //kaller en metode draw som faktisk tegner ting på skjerm
    }

    public void draw(Graphics g) {
        // Grid
        for (int i = 0; i < boardWidth/tileSize; i++) {
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
        }

        //Food
        // g.setColor(Color.red);
        // g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);

        //norrlands
        if (norrlands != null) {
            g.drawImage(norrlands, norrlandsTile.x * tileSize, norrlandsTile.y * tileSize, tileSize, tileSize, null);
        }
        
        //snake Head
        g.setColor(Color.green); //setter fargen til grønn
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize); // Tegner en firkant(x,y,bredde,høyde)

        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game over: " + String.valueOf(snakeBody.size()), tileSize-16,tileSize);
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public void placeNorrlands() {
        norrlandsTile.x = random.nextInt(boardWidth/tileSize);
        norrlandsTile.y = random.nextInt(boardHeight/tileSize);
    }

    private BufferedImage loadNorrlandsImage() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/norrlands.jpg")) {
            if (inputStream != null) {
                return ImageIO.read(inputStream);
            }
        }

        File[] fallbackPaths = new File[] {
            new File("Snake/bin/norrlands.jpg"),
            new File("Snake/src/norrlands.jpg"),
            new File("src/norrlands.jpg")
        };

        for (File file : fallbackPaths) {
            if (file.exists()) {
                return ImageIO.read(file);
            }
        }

        throw new IOException("Fant ikke norrlands.jpg i classpath eller kjente stier.");
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            // placeFood();
        }

        // Eat norrlands
        if (collision(snakeHead, norrlandsTile)) {
            snakeBody.add(new Tile(norrlandsTile.x, norrlandsTile.y));
            placeNorrlands();
        }

        //Snake Body
        for (int i = snakeBody.size()-1; i >= 0; i --) { // Starter fra halen av slangen og går bakover
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { // første kroppsdelen flyttes til hodets gamle posisjon
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else { // hver andre kroppsdels flyttes til den forrige kroppsdels posisjon
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //Game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //collide with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth ||
        snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //Trenger ikke
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
    

    
}
