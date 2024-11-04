package Home;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SplendorHomeScreen extends JFrame {

    private final JLabel startLabel, infoLabel;
    private final ImageIcon startIcon, infoIcon;
    private final int initialWidth, initialHeight;

    public SplendorHomeScreen() {
        setTitle("Splendor Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 700));

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null); // Custom layout

        startIcon = new ImageIcon("src/Images/StartMenu/Start.png");
        infoIcon = new ImageIcon("src/Images/StartMenu/Info.png");

        initialWidth = (int) (startIcon.getIconWidth() * 0.35);
        initialHeight = (int) (startIcon.getIconHeight() * 0.34);

        startLabel = new JLabel(
                new ImageIcon(startIcon.getImage().getScaledInstance(initialWidth, initialHeight, Image.SCALE_SMOOTH)));
        infoLabel = new JLabel(
                new ImageIcon(infoIcon.getImage().getScaledInstance(initialWidth, initialHeight, Image.SCALE_SMOOTH)));

        // Dynamically adjust the position and size of the buttons based on screen size
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustButtonPosition(panel.getWidth(), panel.getHeight());
            }
        });

        // Add hover effect with animation for the start button
        startLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateImage(startLabel, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateImage(startLabel, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Start button clicked!");
            }
        });

        // Add hover effect with animation for the info button
        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateImage(infoLabel, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateImage(infoLabel, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Info button clicked!");
            }
        });

        panel.add(startLabel);
        panel.add(infoLabel);

        add(panel);
        setVisible(true);
    }

    // Method to adjust button size and position based on window size
    private void adjustButtonPosition(int panelWidth, int panelHeight) {
        int startButtonX = panelWidth / 2 - 125; // Center horizontally
        int startButtonY = panelHeight / 2 + 200; // Place slightly below center

        int infoButtonX = panelWidth / 2 - 125; // Center horizontally
        int infoButtonY = panelHeight / 2 + 100; // Place above the start button with a gap

        startLabel.setBounds(startButtonX, startButtonY, initialWidth, initialHeight);
        infoLabel.setBounds(infoButtonX, infoButtonY, initialWidth, initialHeight);
    }

    // Method to animate the image enlarging and shrinking
    private void animateImage(JLabel label, boolean enlarge) {
        int currentWidth = label.getWidth();
        int currentHeight = label.getHeight();
        int newWidth = enlarge ? currentWidth + 20 : currentWidth - 20; // Animation size
        int newHeight = enlarge ? currentHeight + 10 : currentHeight - 10; // Animation size
        int xPosition = label.getX() - (newWidth - currentWidth) / 2; // Keep centered
        int yPosition = label.getY() - (newHeight - currentHeight) / 2;

        label.setBounds(xPosition, yPosition, newWidth, newHeight);

        Image scaledImage = (label == startLabel ? startIcon : infoIcon).getImage().getScaledInstance(newWidth,
                newHeight, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaledImage));
    }

    // Inner class to paint background image
    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon("src/Images/StartMenu/back.jpeg");
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        new SplendorHomeScreen();
    }
}
