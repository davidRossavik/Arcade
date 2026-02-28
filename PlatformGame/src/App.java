import javax.swing.JFrame;

// Her skal appen kjøres
// Sette startparametre osv

public class App {
    public static void main(String[] args) throws Exception {
        MainFrame frame = new MainFrame();

        frame.setSize(700, 700);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Platform Game");


    }
}
