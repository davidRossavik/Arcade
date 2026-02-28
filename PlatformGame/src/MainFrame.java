import java.awt.Color;
import javax.swing.JFrame;

// Hovedvindu
// Skal bytte mellom skjermer som gamepanel og meny (remve() og add())
// Arver JFrame

public class MainFrame extends JFrame {
    MenuScreen screen;
    GamePanel panel;
    LevelScreen levelscreen;
    
    public MainFrame() {
        this.panel = new GamePanel(this);
        this.screen = new MenuScreen(this, 150, 350, 350, 75);
        this.levelscreen = new LevelScreen(this);

        screen.setLocation(0,0);
        screen.setSize(this.getSize());
        screen.setBackground(Color.lightGray);
        screen.setVisible(true);
        add(screen);

        panel.setLocation(0,0);
        panel.setSize(this.getSize());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setVisible(true);
        // this.add(panel);

        levelscreen.setLocation(0,0);
        levelscreen.setSize(this.getSize());
        levelscreen.setBackground(Color.LIGHT_GRAY);
        levelscreen.setVisible(true);

        addKeyListener(new KeyChecker(panel));
    }

    public void showGamePanel() {
        remove(levelscreen);
        add(panel);
        revalidate();
        repaint();
    }
    public void showMenuScreen() {
        panel.stopGameLoop();
        remove(panel);
        add(screen);
        revalidate();
        repaint();
    }
    public void showLevelScreen() {
        panel.stopGameLoop();
        remove(screen);
        add(levelscreen);
        revalidate();
        repaint();
    }

    public void startGame(String mapFile) {
        panel.loadMap(mapFile);
        showGamePanel();
    }
}
