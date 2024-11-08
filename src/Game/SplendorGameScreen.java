package Game;

import java.awt.*;
import javax.swing.*;

public class SplendorGameScreen extends JFrame { 

    public SplendorGameScreen() {
        setTitle("Splendor Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1500, 900));

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null); // Custom layout

        add(panel);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }
  
    // Inner class to paint background image
    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon("src/Images/GameMenu/GameBackground.png");
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);

            g.setColor(new Color(0, 0, 0, 65));
            g.fillRect(0, getHeight() - 200, getWidth(), 200); // Inventory space
            g.fillRect(getWidth() / 2 - 400, 50, 800, 100); // Token space
            g.fillRect(getWidth() / 2 - 500, 160, 850, 450); // Deck space
            g.fillRect(getWidth() / 2 + 360, 160, 120, 450); // Nobles space
        }
    }
}
