
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class MenuScreen extends JPanel {
    Rectangle rekt;
    MainFrame frame;
    boolean hovered = false;

    public MenuScreen(MainFrame frame, int x, int y, int width, int height) {
        this.frame = frame;
        this.rekt = new Rectangle(x, y, width, height);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (rekt.contains(e.getPoint())) {
                    frame.showLevelScreen(); // her skal det egentlig stå showLevelScreen
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (rekt.contains(e.getPoint())) {
                    hovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean nowHovered = rekt.contains(e.getPoint());
                if (nowHovered != hovered) {
                    hovered = nowHovered;
                    repaint();
                }
            }
        });
            
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (hovered) {
            g.setColor(Color.darkGray);
        } else {
            g.setColor(Color.black);
        }
        g.fillRect(rekt.x, rekt.y, rekt.width, rekt.height);
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Start Spillet", 210, 400);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        g.drawString("Block Jumper", 110, 250);

        g.setColor(Color.black);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
        g.drawString("By Big Dave", 250, 300);

    }
}
