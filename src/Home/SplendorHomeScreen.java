package Home;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import Game.Main.MainClass;

public class SplendorHomeScreen extends JFrame {
    private final JLabel infoLabel, infoCardLabel, exitBtnLabel, addLabel, subtractLabel, textLabel;
    private final ImageIcon infoButton;
    private final ImageIcon infoCard;
    private final ImageIcon exitBtn;
    private final ImageIcon addButton;
    private final ImageIcon subtractButton;
    @SuppressWarnings("unused")
    private final int initialWidth, initialHeight;
    private TransitionOverlay transitionOverlay;

    private static boolean infoVisible = false;
    private static int playerNum = 2;

    // Transition Overlay class
    private class TransitionOverlay extends JPanel {
        private float opacity = 0.0f;
        private final Timer fadeTimer;

        public TransitionOverlay() {
            setOpaque(false);
            setBackground(Color.WHITE);

            // Create a timer for smooth fade-out
            fadeTimer = new Timer(20, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    opacity += 0.03f;
                    if (opacity >= 1.0f) {
                        // Start the game after full fade
                        SwingUtilities.invokeLater(() -> {
                            MainClass.setPlayerCount(playerNum);
                            MainClass.main(new String[0]);
                            dispose();
                        });
                        fadeTimer.stop();
                    }
                    repaint();
                }
            });
        }

        public void startFadeOut() {
            fadeTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.SrcOver.derive(opacity));
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }

    public SplendorHomeScreen() {
        setTitle("Splendor Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1300, 800));

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null); // Custom layout

        // Create transition overlay
        transitionOverlay = new TransitionOverlay();
        transitionOverlay.setVisible(false);
        transitionOverlay.setBounds(0, 0, getWidth(), getHeight());

        // Load image icons
        infoButton = new ImageIcon("src/Images/StartMenu/infoButton.png");
        infoCard = new ImageIcon("src/Images/StartMenu/Rules.png");
        exitBtn = new ImageIcon("src/Images/StartMenu/Close.png");
        addButton = new ImageIcon("src/Images/StartMenu/Plus.png");
        subtractButton = new ImageIcon("src/Images/StartMenu/Minus.png");

        initialWidth = (int) (addButton.getIconWidth() * 0.35);
        initialHeight = (int) (addButton.getIconHeight() * 0.34);

        // Create labels
        infoLabel = new JLabel(
                new ImageIcon(
                        infoButton.getImage().getScaledInstance(initialHeight + 10, initialHeight,
                                Image.SCALE_SMOOTH)));
        infoCardLabel = new JLabel(
                new ImageIcon(infoCard.getImage().getScaledInstance(getWidth() - 100, getHeight() - 90,
                        Image.SCALE_SMOOTH)));
        exitBtnLabel = new JLabel(
                new ImageIcon(exitBtn.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        addLabel = new JLabel(
                new ImageIcon(addButton.getImage().getScaledInstance(80, 75, Image.SCALE_SMOOTH)));
        subtractLabel = new JLabel(
                new ImageIcon(subtractButton.getImage().getScaledInstance(70, 60, Image.SCALE_SMOOTH)));

        // Player count label
        Font font = new Font("Algerian", Font.PLAIN, 35);
        textLabel = new JLabel(playerNum + "");
        textLabel.setFont(font);
        textLabel.setForeground(new Color(237, 220, 199));

        JLabel startLabel = new JLabel("Click anywhere to start");
        startLabel.setFont(font);
        startLabel.setForeground(new Color(237, 220, 199));
        startLabel.setBounds(getWidth()/2 - 225, getHeight()/2 + 200, 475, 100);

        // Dynamically adjust the position and size of the buttons based on screen size
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustButtonPosition(panel.getWidth(), panel.getHeight());
                // Update transition overlay size
                transitionOverlay.setBounds(0, 0, panel.getWidth(), panel.getHeight());
            }
        });

        // Add global mouse listener to start the game when clicked (except on info
        // button)
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if we're not on the info button or in info view
                if (!infoVisible && !isMouseOverInfoButton(e)) {
                    startGameWithTransition();
                }
            }
        });

        // Info button functionality
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

                // Show the info card and close button, hide info button
                infoCardLabel.setVisible(true);
                exitBtnLabel.setVisible(true);
                infoLabel.setVisible(false);
            }
        });

        // Close button for info card
        exitBtnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateImage(exitBtnLabel, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateImage(exitBtnLabel, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!infoVisible)
                    return;
                infoVisible = false;

                // Hide the info card and close button, show info button again
                infoCardLabel.setVisible(false);
                exitBtnLabel.setVisible(false);
                infoLabel.setVisible(true);
            }
        });

        // Add player count buttons
        addLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateImage(addLabel, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateImage(addLabel, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (playerNum < 4)
                    playerNum++;
                textLabel.setText(playerNum + "");
            }
        });

        subtractLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateImage(subtractLabel, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateImage(subtractLabel, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (playerNum > 2)
                    playerNum--;
                textLabel.setText(playerNum + "");
            }
        });

        // Add components to panel
        panel.add(transitionOverlay);
        panel.add(infoLabel);
        panel.add(exitBtnLabel);
        panel.add(infoCardLabel);
        panel.add(addLabel);
        panel.add(subtractLabel);
        panel.add(textLabel);
        panel.add(startLabel);

        add(panel);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);

        // Setting initial bounds
        infoCardLabel.setBounds(0, -10, getWidth(), getHeight() - 10);
        infoCardLabel.setVisible(false);
        exitBtnLabel.setBounds(getWidth() - 75, 10, 50, 50);
        exitBtnLabel.setVisible(false);

        addLabel.setBounds(getWidth() / 2 + 80, getHeight() / 2 + 100, 75, 75);
        subtractLabel.setBounds(getWidth() / 2 - 150, getHeight() / 2 + 100, 75, 75);
        textLabel.setBounds(getWidth() / 2 - 10, getHeight() / 2 + 100, 200, 75);
    }

    // Method to start game with transition
    private void startGameWithTransition() {
        // Make transition overlay visible and start fade-out
        transitionOverlay.setVisible(true);
        transitionOverlay.startFadeOut();
    }

    // Method to check if mouse is over the info button to prevent game start
    private boolean isMouseOverInfoButton(MouseEvent e) {
        Point mousePoint = e.getPoint();
        Rectangle infoButtonBounds = new Rectangle(
                infoLabel.getX(),
                infoLabel.getY(),
                infoLabel.getWidth(),
                infoLabel.getHeight());
        return infoButtonBounds.contains(mousePoint);
    }

    // Method to adjust button size and position based on window size
    private void adjustButtonPosition(int panelWidth, int panelHeight) {
        int infoButtonX = panelWidth / 2 + 280;
        int infoButtonY = panelHeight / 2 - 200;

        infoLabel.setBounds(infoButtonX, infoButtonY, initialHeight, initialHeight);
    }

    // Method to animate the image enlarging and shrinking
    private void animateImage(JLabel label, boolean enlarge) {
        int currentWidth = label.getWidth();
        int currentHeight = label.getHeight();
        int newWidth = (int) (enlarge ? currentWidth * 1.2 : currentWidth / 1.2);
        int newHeight = (int) (enlarge ? currentHeight * 1.2 : currentHeight / 1.2);
        int xPosition = label.getX() - (newWidth - currentWidth) / 2;
        int yPosition = label.getY() - (newHeight - currentHeight) / 2;

        label.setBounds(xPosition, yPosition, newWidth, newHeight);

        // Adjust image scaling based on which label is being hovered
        Image scaledImage;
        if (label == infoLabel) {
            scaledImage = infoButton.getImage().getScaledInstance(newHeight + 10, newHeight, Image.SCALE_SMOOTH);
        } else if (label == exitBtnLabel) {
            scaledImage = exitBtn.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        } else if (label == addLabel) {
            scaledImage = addButton.getImage().getScaledInstance(newWidth, newHeight - 10, Image.SCALE_SMOOTH);
        } else if (label == subtractLabel) {
            scaledImage = subtractButton.getImage().getScaledInstance(newWidth - 10, newHeight - 20,
                    Image.SCALE_SMOOTH);
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
            g.setColor(new Color(0, 0, 0, 175));
            g.fillRect(getWidth() / 3 + 100, getHeight() / 2 + 135, getWidth() / 4 - 75, 40);
        }
    }

    public static void main(String[] args) {
        new SplendorHomeScreen();
    }
}