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

    private class TransitionOverlay extends JPanel {
        private float opacity = 0.0f;
        private final Timer fadeTimer;
        private final int FADE_DURATION = 800;
        private final int TIMER_INTERVAL = 16;
        private final float OPACITY_INCREMENT = 1.0f / (FADE_DURATION / TIMER_INTERVAL);
        private boolean isTransitioning = false;

        public TransitionOverlay() {
            setOpaque(false);
            setBackground(Color.BLACK);

            fadeTimer = new Timer(TIMER_INTERVAL, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isTransitioning) return;

                    opacity = Math.min(1.0f, opacity + OPACITY_INCREMENT);

                    if (opacity >= 1.0f) {
                        opacity = 1.0f;
                        isTransitioning = false;
                        fadeTimer.stop();

                        // Launch the game directly without preloading
                        launchGame();
                    }
                    repaint();
                }
            });
        }

        private void launchGame() {
            SwingUtilities.invokeLater(() -> {
                MainClass.setPlayerCount(playerNum);
                MainClass.main(new String[0]);
                dispose();
            });
        }

        public void startFadeOut() {
            if (!isTransitioning) {
                opacity = 0.0f;
                isTransitioning = true;
                fadeTimer.start();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            g2d.setComposite(AlphaComposite.SrcOver.derive(opacity));
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }

    private void startGameWithTransition() {
        transitionOverlay.setBounds(0, 0, getWidth(), getHeight());
        transitionOverlay.setVisible(true);
        transitionOverlay.startFadeOut();
    }

    public SplendorHomeScreen() {
        setTitle("Splendor Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1300, 800));

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(null);

        transitionOverlay = new TransitionOverlay();
        transitionOverlay.setVisible(false);
        transitionOverlay.setBounds(0, 0, getWidth(), getHeight());

        infoButton = new ImageIcon(getClass().getResource("/Images/StartMenu/infoButton.png"));
        infoCard = new ImageIcon(getClass().getResource("/Images/StartMenu/Rules.png"));
        exitBtn = new ImageIcon(getClass().getResource("/Images/StartMenu/Close.png"));
        addButton = new ImageIcon(getClass().getResource("/Images/StartMenu/Plus.png"));
        subtractButton = new ImageIcon(getClass().getResource("/Images/StartMenu/Minus.png"));

        initialWidth = (int) (addButton.getIconWidth() * 0.35);
        initialHeight = (int) (addButton.getIconHeight() * 0.34);

        infoLabel = new JLabel(new ImageIcon(
                infoButton.getImage().getScaledInstance(initialHeight + 10, initialHeight, Image.SCALE_SMOOTH)));
        infoCardLabel = new JLabel(new ImageIcon(
                infoCard.getImage().getScaledInstance(getWidth() - 100, getHeight() - 90, Image.SCALE_SMOOTH)));
        exitBtnLabel = new JLabel(
                new ImageIcon(exitBtn.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        addLabel = new JLabel(
                new ImageIcon(addButton.getImage().getScaledInstance(80, 75, Image.SCALE_SMOOTH)));
        subtractLabel = new JLabel(
                new ImageIcon(subtractButton.getImage().getScaledInstance(70, 60, Image.SCALE_SMOOTH)));

        Font font = new Font("Algerian", Font.PLAIN, 35);
        textLabel = new JLabel(playerNum + "");
        textLabel.setFont(font);
        textLabel.setForeground(new Color(237, 220, 199));

        JLabel startLabel = new JLabel("Click anywhere to start");
        startLabel.setFont(font);
        startLabel.setForeground(new Color(237, 220, 199));
        startLabel.setBounds(getWidth() / 2 - 225, getHeight() / 2 + 200, 475, 100);

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustButtonPosition(panel.getWidth(), panel.getHeight());
                transitionOverlay.setBounds(0, 0, panel.getWidth(), panel.getHeight());
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!infoVisible && !isMouseOverInfoButton(e)) {
                    startGameWithTransition();
                }
            }
        });

        infoLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (infoVisible)
                    return;
                infoVisible = true;
                infoCardLabel.setVisible(true);
                exitBtnLabel.setVisible(true);
                infoLabel.setVisible(false);
            }
        });

        exitBtnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!infoVisible)
                    return;
                infoVisible = false;
                infoCardLabel.setVisible(false);
                exitBtnLabel.setVisible(false);
                infoLabel.setVisible(true);
            }
        });

        addLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (playerNum < 4)
                    playerNum++;
                textLabel.setText(playerNum + "");
            }
        });

        subtractLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (playerNum > 2)
                    playerNum--;
                textLabel.setText(playerNum + "");
            }
        });

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

        infoCardLabel.setBounds(0, -10, getWidth(), getHeight() - 10);
        infoCardLabel.setVisible(false);
        exitBtnLabel.setBounds(getWidth() - 75, 10, 50, 50);
        exitBtnLabel.setVisible(false);

        addLabel.setBounds(getWidth() / 2 + 80, getHeight() / 2 + 100, 75, 75);
        subtractLabel.setBounds(getWidth() / 2 - 150, getHeight() / 2 + 100, 75, 75);
        textLabel.setBounds(getWidth() / 2 - 10, getHeight() / 2 + 100, 200, 75);
    }

    private boolean isMouseOverInfoButton(MouseEvent e) {
        Point mousePoint = e.getPoint();
        Rectangle infoButtonBounds = new Rectangle(
                infoLabel.getX(),
                infoLabel.getY(),
                infoLabel.getWidth(),
                infoLabel.getHeight());
        return infoButtonBounds.contains(mousePoint);
    }

    private void adjustButtonPosition(int panelWidth, int panelHeight) {
        int infoButtonX = panelWidth / 2 + 280;
        int infoButtonY = panelHeight / 2 - 200;

        infoLabel.setBounds(infoButtonX, infoButtonY, initialHeight, initialHeight);
    }

    static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon(getClass().getResource("/Images/StartMenu/back.jpeg"));
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 175));
            g.fillRect(getWidth() / 3 + 100, getHeight() / 2 + 135, getWidth() / 4 - 75, 40);
        }
    }

    public static void main(String[] args) {
        new SplendorHomeScreen();
    }
}