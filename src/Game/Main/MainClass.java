package Game.Main;

import javax.swing.*;
import Cards.CardLoader;
import java.awt.*;

public class MainClass {
    private static int playerCount = 2;
    private CardLoader cardLoader;
    private JFrame frame;
    private SplendorGameScreen gameScreen;
    private boolean assetsPreloaded = false;

    // Constructor for pre-initialization
    public MainClass() {
        // Initialize basic components that can be done early
        frame = new JFrame("Splendor Game Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Preload assets and initialize heavy components
    public void preloadAssets() {
        if (assetsPreloaded) return;

        try {
            // Pre-load card data
            cardLoader = new CardLoader("src/Cards/CardData.txt");
            
            // Initialize game screen
            gameScreen = new SplendorGameScreen(cardLoader, playerCount);
            
            // Setup frame
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            @SuppressWarnings("unused")
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BorderLayout());
            
            assetsPreloaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Start the pre-initialized game
    public void startGame() {
        if (!assetsPreloaded) {
            preloadAssets();
        }

        SwingUtilities.invokeLater(() -> {
            // Add game screen to frame
            frame.add(gameScreen, BorderLayout.CENTER);
            
            // Make the frame visible
            frame.setVisible(true);
        });
    }

    // Static methods
    public static void setPlayerCount(int count) {
        playerCount = count;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    // Modified main method to use new structure
    public static void main(String[] args) {
        MainClass game = new MainClass();
        game.preloadAssets();
        game.startGame();
    }
}