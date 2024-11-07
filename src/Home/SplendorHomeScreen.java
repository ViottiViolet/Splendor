package Home;

import Game.SplendorGameScreen;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class SplendorHomeScreen extends JFrame {

    private final JLabel startLabel, infoLabel, infoCardLabel, closeBtnLabel;
    private final ImageIcon startButton, infoButton, closeBtn, infoCard;
    private final int initialWidth, initialHeight;

    private static boolean infoVisible = false;

    public SplendorHomeScreen() {
        setTitle("Splendor Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1300, 800));

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null); // Custom layout

        startButton = new ImageIcon("src/Images/StartMenu/Start.png");
        infoButton = new ImageIcon("src/Images/StartMenu/Info.png");
        infoCard = new ImageIcon("src/Images/StartMenu/Rules.png");
        closeBtn = new ImageIcon("src/Images/StartMenu/Close.png");

        initialWidth = (int) (startButton.getIconWidth() * 0.35);
        initialHeight = (int) (startButton.getIconHeight() * 0.34);

        startLabel = new JLabel(
                new ImageIcon(
                        startButton.getImage().getScaledInstance(initialWidth, initialHeight, Image.SCALE_SMOOTH)));
        infoLabel = new JLabel(
                new ImageIcon(
                        infoButton.getImage().getScaledInstance(initialWidth, initialHeight, Image.SCALE_SMOOTH)));
        infoCardLabel = new JLabel(
                new ImageIcon(infoCard.getImage().getScaledInstance(getWidth()-100, getHeight()-90,
                        Image.SCALE_SMOOTH)));
        closeBtnLabel = new JLabel(
                new ImageIcon(closeBtn.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));

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
                if (infoVisible)
                    return;
                SplendorGameScreen gameScreen = new SplendorGameScreen();
                gameScreen.setVisible(true);
                dispose();
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
                if (infoVisible)
                    return;
                infoVisible = true;

                // Show the info card and close button, hide start and info labels
                infoCardLabel.setVisible(true);
                closeBtnLabel.setVisible(true);
                startLabel.setVisible(false);
                infoLabel.setVisible(false);
            }
        });

        // Add hover effect with animation for the close button
        closeBtnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateImage(closeBtnLabel, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateImage(closeBtnLabel, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!infoVisible)
                    return;
                infoVisible = false;

                // Hide the info card and close button, show start and info labels again
                infoCardLabel.setVisible(false);
                closeBtnLabel.setVisible(false);
                startLabel.setVisible(true);
                infoLabel.setVisible(true);
            }
        });

        //adding the element to the screen
        panel.add(startLabel);
        panel.add(infoLabel);
        panel.add(closeBtnLabel);
        panel.add(infoCardLabel);

        add(panel);
        pack();
        setVisible(true);

        //setting the coordinates and bounds for the INFO PANEL elements
        infoCardLabel.setBounds(0, -10, getWidth(), getHeight()-10);
        infoCardLabel.setVisible(false);
        closeBtnLabel.setBounds(getWidth() - 75, 10, 50, 50);
        closeBtnLabel.setVisible(false);

        // Center the window on the screen
        setLocationRelativeTo(null);
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
    
        // Adjust image scaling based on which label is being hovered
        Image scaledImage;
        if (label == startLabel) {
            scaledImage = startButton.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        } else if (label == infoLabel) {
            scaledImage = infoButton.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        } else if (label == closeBtnLabel) {
            scaledImage = closeBtn.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        } else {
            return;
        }
    
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