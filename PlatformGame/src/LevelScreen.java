import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class LevelScreen extends JPanel{
    Rectangle level_0;
    Rectangle level_1;
    Rectangle level_2;
    Rectangle level_3;
    List<Rectangle> levels = new ArrayList<>();
    private int hoveredIndex;

    
    public LevelScreen(MainFrame frame) {

        this.level_0 = new Rectangle(90,200,500,75);
        levels.add(level_0);
        this.level_1 = new Rectangle(90,300,500,75);
        levels.add(level_1);
        this.level_2 = new Rectangle(90,400,500,75);
        levels.add(level_2);
        this.level_3 = new Rectangle(90,500,500,75);
        levels.add(level_3);

        addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (level_0.contains(e.getPoint())) {
                    frame.startGame("C:\\Users\\david\\Favorites\\spill_kode\\PlatformSpill\\PlatformGame\\src\\Maps\\map0.txt");
                }  else if (level_1.contains(e.getPoint())) {
                    frame.startGame("C:\\Users\\david\\Favorites\\spill_kode\\PlatformSpill\\PlatformGame\\src\\Maps\\map1.txt");
                } else if (level_2.contains(e.getPoint())) {
                    frame.startGame("C:\\Users\\david\\Favorites\\spill_kode\\PlatformSpill\\PlatformGame\\src\\Maps\\map2.txt");
                } else if (level_3.contains(e.getPoint())) {
                    frame.startGame("C:\\Users\\david\\Favorites\\spill_kode\\PlatformSpill\\PlatformGame\\src\\Maps\\map3.txt");
                }
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoveredIndex = -1;
                for (int i = 0; i < levels.size(); i++) {
                    if (levels.get(i).contains(e.getPoint())) {
                        hoveredIndex = i;
                        break;
                    }
                }
                repaint();
            }
        });
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);

        for (int i = 0; i < levels.size(); i++) {
            if (i == hoveredIndex) {
                g.setColor(Color.darkGray);
            } else {
                g.setColor(Color.black);
            }
            Rectangle level = levels.get(i);
            g.fillRect(level.x, level.y, level.width, level.height);
        }
        
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 100));
        g.drawString("Levels", 175, 150);
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 40));
        g.drawString("Toturial", 250, 250);
        g.drawString("Level 1", 250, 350);
        g.drawString("Level 2", 250, 450);
        g.drawString("Level 3", 250, 550);
    }
}
