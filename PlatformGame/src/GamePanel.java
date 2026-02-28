import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

// Gamepanel er panelet til selve spillet
// Arver JPanel, håndterer tegning av spiller, vegger osv

public class GamePanel extends JPanel {
    Player player;
    //Enemy enemy;
    Timer gameLoop;
    ArrayList<Wall> walls = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    boolean gameOver;
    Rectangle homeScreen;
    MainFrame frame;
    boolean hovered = false;
    String[] map;

    public GamePanel(MainFrame frame) {
        this.player = new Player(300, 450, this);
        this.map = MapLoader.loadMap("C:\\Users\\david\\Favorites\\spill_kode\\PlatformSpill\\PlatformGame\\src\\Maps\\map0.txt");
        makeWalls();

        gameLoop = new Timer();
        gameLoop.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!gameOver) {
                    player.set();
                    checkGameOver();
                    for (Enemy enemy : enemies) enemy.move();
                    repaint();
                }
            } 
        },0,17);

        this.homeScreen = new Rectangle(10,10,40,30);
        this.frame = frame;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (homeScreen.contains(e.getPoint())) {
                    frame.showMenuScreen();
                    resetGame();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hovered = homeScreen.contains(e.getPoint());
                repaint();
            }
        });
    }

    public void loadMap(String mapFile) {
        this.map = MapLoader.loadMap(mapFile);
        walls.clear();
        enemies.clear();
        makeWalls();
        resetGame();
    }

    public void makeWalls() {                        
        int blockSize = 50;
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length(); col++) {
                if (map[row].charAt(col) == 'X') { //vegger
                    int x = col * blockSize;
                    int y = row * blockSize;
                    walls.add(new Wall(x,y,50,50));
                }
                if (map[row].charAt(col) == 'O') { //fiende
                    int x = col * blockSize;
                    int y = row * blockSize;
                    enemies.add(new Enemy(x, y, this));
                }
            }
        }
        
    }

    // GameOver
    public void checkGameOver() {
        if (player.y > 800) {
            gameOver = true;
            gameLoop.cancel();
        }
        for (Enemy enemy : enemies) {
            if (player.hitBox.intersects(enemy.hitBox)) {
                gameOver = true;
                gameLoop.cancel();
            }
        }
        
    }
    public void resetGame() {
        gameOver = false;
        player.setPosition(300, 400);
        for (Wall wall : walls) {
            wall.resetPosition();
        }
        for (Enemy enemy : enemies) {
            enemy.resetPosition();
        }

        gameLoop = new Timer();
        gameLoop.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!gameOver) {
                    player.set();
                    for (Enemy enemy : enemies) enemy.move();
                    checkGameOver();
                    repaint();
                }
            }
        }, 0,17);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gtd = (Graphics2D) g;
        player.draw(gtd);
        for (Wall wall : walls) wall.draw(gtd);
        for (Enemy enemy : enemies) enemy.draw(gtd);

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.drawRect(75,150, 525, 75);
            g.fillRect(75, 150, 525, 75);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over! Trykk R for å restarte", 100, 200);
        }

        //hjemknapp
        if (hovered) {
            g.setColor(Color.darkGray);
        } else {
            g.setColor(Color.black);
        }
        g.fillRect(homeScreen.x, homeScreen.y, homeScreen.width, homeScreen.height);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Home",homeScreen.x+2,homeScreen.y + 20);
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.cancel();
            gameLoop.purge(); //fjerner alle oppgaver
        }
    }

    // TASTATUR //
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'a') player.keyLeft = true;
        if (e.getKeyChar() == 'd') player.keyRight = true;
        if (e.getKeyChar() == 'w') player.keyUp = true;
        if (e.getKeyChar() == 's') player.keyDown = true;
        if (e.getKeyChar() == 'r' && gameOver) resetGame(); //GameOver 
    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'a') player.keyLeft = false;
        if (e.getKeyChar() == 'd') player.keyRight = false;
        if (e.getKeyChar() == 'w') player.keyUp = false;
        if (e.getKeyChar() == 's') player.keyDown = false;
    }


}
