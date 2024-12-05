package Game.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEndScreen extends JFrame {
    private final ImageIcon backgroundImage;

    public GameEndScreen(int winningPlayerNumber) {
        this(winningPlayerNumber, MainClass.getPlayerCount());
    }

    public GameEndScreen(int winningPlayerNumber, int playerCount) {
        // Set up the frame
        setTitle("Splendor - Game Over");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a custom JPanel for background painting
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Load background image
        backgroundImage = new ImageIcon("src/Images/GameMenu/GameBackground.png");

        // Create winner label
        JLabel winnerLabel = new JLabel("Player " + winningPlayerNumber + " Wins!");
        winnerLabel.setFont(new Font("Gothic", Font.BOLD, 48));
        winnerLabel.setForeground(Color.YELLOW);
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        winnerLabel.setVerticalAlignment(JLabel.CENTER);

        // Create play again button
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Gothic", Font.BOLD, 24));
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the end screen
                dispose();
                
                // Create a new MainClass instance and restart the game
                MainClass game = new MainClass();
                game.preloadAssets();
                game.startGame();
            }
        });

        // Create exit button
        JButton exitButton = new JButton("Exit Game");
        exitButton.setFont(new Font("Gothic", Font.BOLD, 24));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.add(playAgainButton);
        buttonPanel.add(exitButton);

        // Add components to the background panel
        backgroundPanel.add(winnerLabel, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set the background panel as the content pane
        setContentPane(backgroundPanel);
    }

    // Optional: Maintain the main method for direct launching
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainClass.setPlayerCount(2);  // Default to 2 players
            MainClass game = new MainClass();
            game.preloadAssets();
            game.startGame();
        });
    }
}