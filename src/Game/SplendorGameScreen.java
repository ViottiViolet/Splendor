package Game;

import java.awt.*;
import javax.swing.*;

public class SplendorGameScreen extends JFrame { 

    public SplendorGameScreen() {
        setTitle("Splendor Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1300, 800));

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null); // Custom layout

        add(panel);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
  
    // Inner class to paint background image
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon("src/Images/GameMenu/GameBackground.png");
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
  
    public static void main(String[] args) {
        new SplendorGameScreen();
    }
}
